package ch.zhaw.it.pm.humanangeryourself.domain.logic;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.CardOffer;
import ch.zhaw.it.pm.humanangeryourself.config.GameConfig;
import ch.zhaw.it.pm.humanangeryourself.config.PropertyPrice;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Board;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Npc;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Piece;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Property;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Tile;
import ch.zhaw.it.pm.humanangeryourself.domain.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EconomyTest {
    private GameConfig config;
    private Economy economy;
    private Board board;
    private Player player;
    private Player otherPlayer;

    @BeforeEach
    void setUp() {
        GameConfig defaults = GameConfig.defaults();
        config = new GameConfig(1_000, 100, 10, 2, defaults.gambling(), defaults.cards(), Map.of(
                PropertySize.SMALL, new PropertyPrice(100, 10),
                PropertySize.MEDIUM, new PropertyPrice(200, 20),
                PropertySize.LARGE, new PropertyPrice(500, 50)));
        economy = new Economy(config);
        board = new Board();
        player = newPlayer(PlayerColor.RED, 1_000);
        otherPlayer = newPlayer(PlayerColor.BLUE, 750);
    }

    @ParameterizedTest
    @CsvSource({"SMALL, 100", "MEDIUM, 200", "LARGE, 500"})
    void buyPropertyChargesPrice(PropertySize size, long price) {
        Tile tile = board.getTileById(1);

        economy.buyProperty(player, tile, size);

        assertEquals(1_000 - price, player.getMoney());
    }

    @ParameterizedTest
    @EnumSource(PropertySize.class)
    void buyPropertyAssignsOwner(PropertySize size) {
        Tile tile = board.getTileById(1);

        economy.buyProperty(player, tile, size);

        assertSame(player, tile.getProperty().orElseThrow().getOwner());
    }

    @ParameterizedTest
    @EnumSource(PropertySize.class)
    void buyPropertySetsSize(PropertySize size) {
        Tile tile = board.getTileById(1);

        economy.buyProperty(player, tile, size);

        assertEquals(size, tile.getProperty().orElseThrow().getSize());
    }

    @Test
    void buyCardChargesPrice() {
        player.addCard(CardType.SWITCH);
        CardOffer offer = new CardOffer(CardType.TRY_AGAIN, 125);

        economy.buyCard(player, offer);

        assertEquals(875, player.getMoney());
    }

    @Test
    void buyCardAddsToHand() {
        player.addCard(CardType.SWITCH);
        CardOffer offer = new CardOffer(CardType.TRY_AGAIN, 125);

        economy.buyCard(player, offer);

        assertEquals(List.of(CardType.SWITCH, CardType.TRY_AGAIN), player.getCards());
    }

    @ParameterizedTest
    @CsvSource({"SMALL, 10", "MEDIUM, 20", "LARGE, 50"})
    void collectRentDebitsPayer(PropertySize size, long rent) {
        Tile tile = board.getTileById(1);
        tile.setProperty(new Property(otherPlayer, size));

        economy.collectRent(player, tile);

        assertEquals(1_000 - rent, player.getMoney());
    }

    @ParameterizedTest
    @CsvSource({"SMALL, 10", "MEDIUM, 20", "LARGE, 50"})
    void collectRentCreditsOwner(PropertySize size, long rent) {
        Tile tile = board.getTileById(1);
        tile.setProperty(new Property(otherPlayer, size));

        economy.collectRent(player, tile);

        assertEquals(750 + rent, otherPlayer.getMoney());
    }

    @Test
    void unownedTileDoesNotChargeRent() {
        economy.collectRent(player, board.getTileById(1));

        assertEquals(1_000, player.getMoney());
    }

    @Test
    void ownPropertyDoesNotChargeRent() {
        Tile tile = board.getTileById(1);
        tile.setProperty(new Property(player, PropertySize.MEDIUM));

        economy.collectRent(player, tile);

        assertEquals(1_000, player.getMoney());
    }

    @ParameterizedTest
    @CsvSource({
            "-101, true", "-100, false", "-99, false", "-1, false",
            "0, false", "1, false", "99, false", "100, false", "101, false"
    })
    void debtLimitUsesThreshold(long balance, boolean expected) {
        player = newPlayer(PlayerColor.RED, balance);

        assertEquals(expected, economy.isOverDebtLimit(player));
    }

    @ParameterizedTest
    @CsvSource({"-1, true", "0, false", "1, false"})
    void zeroDebtLimitUsesThreshold(long balance, boolean expected) {
        GameConfig noDebt = new GameConfig(config.startMoney(), 0, config.maxHandSize(),
                config.maxPropertiesPerQuarter(), config.gambling(), config.cards(), config.propertyPrices());
        economy = new Economy(noDebt);
        player = newPlayer(PlayerColor.RED, balance);

        assertEquals(expected, economy.isOverDebtLimit(player));
    }

    @ParameterizedTest
    @ValueSource(longs = {-300, 0, 500})
    void bankruptSendsPiecesHome(long balance) {
        prepareBankruptcy(balance);

        economy.bankrupt(board, player);

        player.getPieces().forEach(piece -> assertSame(piece.getHomeTile(), piece.getCurrentTile()));
    }

    @ParameterizedTest
    @ValueSource(longs = {-300, 0, 500})
    void bankruptResetsBalance(long balance) {
        prepareBankruptcy(balance);

        economy.bankrupt(board, player);

        assertEquals(1_000, player.getMoney());
    }

    @ParameterizedTest
    @ValueSource(longs = {-300, 0, 500})
    void bankruptRemovesProperties(long balance) {
        prepareBankruptcy(balance);

        economy.bankrupt(board, player);

        assertTrue(board.getTileById(1).getProperty().isEmpty());
        assertTrue(board.getTileById(16).getProperty().isEmpty());
        assertTrue(board.getPropertiesOfPlayer(player).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {-300, 0, 500})
    void bankruptKeepsOtherPiece(long balance) {
        prepareBankruptcy(balance);
        Piece otherPiece = otherPlayer.getPieces().get(0);
        Tile otherPosition = board.getTileById(21);

        economy.bankrupt(board, player);

        assertSame(otherPosition, otherPiece.getCurrentTile());
    }

    @ParameterizedTest
    @ValueSource(longs = {-300, 0, 500})
    void bankruptKeepsOtherBalance(long balance) {
        prepareBankruptcy(balance);

        economy.bankrupt(board, player);

        assertEquals(750, otherPlayer.getMoney());
    }

    @ParameterizedTest
    @ValueSource(longs = {-300, 0, 500})
    void bankruptKeepsOtherProperty(long balance) {
        prepareBankruptcy(balance);
        Tile otherPropertyTile = board.getTileById(2);
        Property otherProperty = otherPropertyTile.getProperty().orElseThrow();

        economy.bankrupt(board, player);

        assertSame(otherProperty, otherPropertyTile.getProperty().orElseThrow());
    }

    @Test
    void cardOffersMatchBudget() {
        visitNpcs();

        List<BuyCard> offers = economy.getBuyableCards(player);

        assertEquals(3, offers.size());

        assertTrue(offers.contains(new BuyCard(new CardOffer(CardType.TRY_AGAIN, 40))));
        assertTrue(offers.contains(new BuyCard(new CardOffer(CardType.SPEED_BOOST, 100))));
        assertFalse(offers.contains(new BuyCard(new CardOffer(CardType.SWITCH, 101))));
    }

    @Test
    void cardOffersIncludeEachSeller() {
        visitNpcs();

        List<BuyCard> offers = economy.getBuyableCards(player);

        assertTrue(offers.contains(new BuyCard(new CardOffer(CardType.TRY_AGAIN, 40))));
        assertTrue(offers.contains(new BuyCard(new CardOffer(CardType.MONEY_TIGER, 75))));
    }

    @Test
    void cardOffersKeepBalance() {
        visitNpcs();

        economy.getBuyableCards(player);

        assertEquals(100, player.getMoney());
    }

    @Test
    void cardOffersKeepHand() {
        visitNpcs();

        economy.getBuyableCards(player);

        assertTrue(player.getCards().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0, 99})
    void cardOffersRequireFunds(long balance) {
        player = newPlayer(PlayerColor.RED, balance);
        Tile npcTile = new Tile(1, TileType.TRACK, 0,
                new Npc("Seller", List.of(new CardOffer(CardType.TRY_AGAIN, 100))));
        player.getPieces().get(0).moveTo(npcTile);

        assertTrue(economy.getBuyableCards(player).isEmpty());
    }

    @Test
    void cardOffersRequireSeller() {
        assertTrue(economy.getBuyableCards(player).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("propertyBudgets")
    void propertyOffersMatchBudget(long balance, List<PropertySize> affordableSizes) {
        Tile tile = visitPropertyTile(balance);
        Set<BuyProperty> expected = affordableSizes.stream()
                .map(size -> new BuyProperty(tile.getId(), size))
                .collect(Collectors.toSet());

        List<BuyProperty> offers = economy.getBuyableProperties(player, board);

        assertEquals(expected.size(), offers.size());
        assertEquals(expected, Set.copyOf(offers));
    }

    private static Stream<Arguments> propertyBudgets() {
        return Stream.of(
                Arguments.of(-1L, List.of()),
                Arguments.of(99L, List.of()),
                Arguments.of(100L, List.of(PropertySize.SMALL)),
                Arguments.of(199L, List.of(PropertySize.SMALL)),
                Arguments.of(200L, List.of(PropertySize.SMALL, PropertySize.MEDIUM)),
                Arguments.of(499L, List.of(PropertySize.SMALL, PropertySize.MEDIUM)),
                Arguments.of(500L, List.of(PropertySize.SMALL, PropertySize.MEDIUM, PropertySize.LARGE)));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 99, 100, 199, 200, 499, 500})
    void propertyOffersKeepBalance(long balance) {
        visitPropertyTile(balance);

        economy.getBuyableProperties(player, board);

        assertEquals(balance, player.getMoney());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 99, 100, 199, 200, 499, 500})
    void propertyOffersKeepTileEmpty(long balance) {
        Tile tile = visitPropertyTile(balance);

        economy.getBuyableProperties(player, board);

        assertTrue(tile.getProperty().isEmpty());
    }

    @Test
    void propertyOffersCombineTiles() {
        visitPropertyTiles();

        List<BuyProperty> offers = economy.getBuyableProperties(player, board).stream()
                .filter(offer -> offer.tile() == 1 || offer.tile() == 16)
                .toList();

        assertEquals(4, offers.size());
        assertEquals(Set.of(
                new BuyProperty(1, PropertySize.SMALL),
                new BuyProperty(1, PropertySize.MEDIUM),
                new BuyProperty(16, PropertySize.SMALL),
                new BuyProperty(16, PropertySize.MEDIUM)), Set.copyOf(offers));
    }

    @Test
    void propertyOffersSkipSafeTiles() {
        visitPropertyTiles();

        List<BuyProperty> offers = economy.getBuyableProperties(player, board);

        assertTrue(offers.stream().noneMatch(offer -> offer.tile() == 5));
    }

    @Test
    void propertyOffersSkipOccupiedTiles() {
        visitPropertyTiles();

        List<BuyProperty> offers = economy.getBuyableProperties(player, board);

        assertTrue(offers.stream().noneMatch(offer -> offer.tile() == 2));
    }

    @Test
    void propertyOffersUseVisitedTiles() {
        visitPropertyTiles();
        Set<Integer> visited = Set.of(1, 2, 5, 16);

        List<BuyProperty> offers = economy.getBuyableProperties(player, board);

        assertTrue(offers.stream().allMatch(offer -> visited.contains(offer.tile())));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    void propertyOffersSkipFullQuarters(int propertyCount) {
        visitQuarters(propertyCount);

        List<BuyProperty> offers = economy.getBuyableProperties(player, board);

        assertTrue(offers.stream().noneMatch(offer -> offer.tile() == 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    void propertyOffersAllowOpenQuarters(int propertyCount) {
        visitQuarters(propertyCount);

        List<BuyProperty> offers = economy.getBuyableProperties(player, board).stream()
                .filter(offer -> offer.tile() == 16)
                .toList();

        assertEquals(3, offers.size());
        assertEquals(Set.of(
                new BuyProperty(16, PropertySize.SMALL),
                new BuyProperty(16, PropertySize.MEDIUM),
                new BuyProperty(16, PropertySize.LARGE)), Set.copyOf(offers));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    void propertyOffersUseVisitedTracks(int propertyCount) {
        visitQuarters(propertyCount);

        List<BuyProperty> offers = economy.getBuyableProperties(player, board);

        assertTrue(offers.stream().allMatch(offer -> offer.tile() == 1 || offer.tile() == 16));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 4})
    void propertyOffersRejectInvalidQuarter(int quarter) {
        Tile tile = new Tile(999, TileType.TRACK, quarter, null);
        player.getPieces().get(0).moveTo(tile);

        assertThrows(IllegalArgumentException.class, () -> economy.getBuyableProperties(player, board));
    }

    @Test
    void cardOffersRequirePieces() {
        Player playerWithoutPieces = new Player("No pieces", PlayerColor.RED, 1_000, List.of());

        assertTrue(economy.getBuyableCards(playerWithoutPieces).isEmpty());
    }

    @Test
    void propertyOffersRequirePieces() {
        Player playerWithoutPieces = new Player("No pieces", PlayerColor.RED, 1_000, List.of());

        assertTrue(economy.getBuyableProperties(playerWithoutPieces, board).isEmpty());
    }

    private Tile visitPropertyTile(long balance) {
        player = newPlayer(PlayerColor.RED, balance);
        Tile tile = board.getTileById(1);
        player.getPieces().get(0).moveTo(tile);
        return tile;
    }

    private void visitPropertyTiles() {
        player = newPlayer(PlayerColor.RED, 200);
        Tile occupied = board.getTileById(2);
        occupied.setProperty(new Property(otherPlayer, PropertySize.SMALL));
        player.getPieces().get(0).moveTo(board.getTileById(1));
        player.getPieces().get(1).moveTo(board.getTileById(16));
        player.getPieces().get(2).moveTo(board.getTileById(5));
        player.getPieces().get(3).moveTo(occupied);
    }

    private void visitQuarters(int propertyCount) {
        for (int i = 0; i < propertyCount; i++) {
            Player owner = i == 0 ? player : otherPlayer;
            board.getTileById(2 + i).setProperty(new Property(owner, PropertySize.SMALL));
        }
        player.getPieces().get(0).moveTo(board.getTileById(1));
        player.getPieces().get(1).moveTo(board.getTileById(16));
    }

    private void visitNpcs() {
        player = newPlayer(PlayerColor.RED, 100);
        Tile firstNpcTile = new Tile(1, TileType.TRACK, 0, new Npc("First", List.of(
                new CardOffer(CardType.TRY_AGAIN, 40),
                new CardOffer(CardType.SPEED_BOOST, 100),
                new CardOffer(CardType.SWITCH, 101))));
        Tile secondNpcTile = new Tile(16, TileType.TRACK, 1,
                new Npc("Second", List.of(new CardOffer(CardType.MONEY_TIGER, 75))));
        player.getPieces().get(0).moveTo(firstNpcTile);
        player.getPieces().get(1).moveTo(secondNpcTile);
        player.getPieces().get(2).moveTo(board.getTileById(2));
    }

    private void prepareBankruptcy(long balance) {
        player = newPlayer(PlayerColor.RED, balance);
        board.getTileById(1).setProperty(new Property(player, PropertySize.SMALL));
        board.getTileById(16).setProperty(new Property(player, PropertySize.LARGE));
        board.getTileById(2).setProperty(new Property(otherPlayer, PropertySize.MEDIUM));
        player.getPieces().get(0).moveTo(board.getTileById(1));
        player.getPieces().get(1).moveTo(board.getTileById(16));
        player.getPieces().get(2).moveTo(board.getTileById(31));
        otherPlayer.getPieces().get(0).moveTo(board.getTileById(21));
    }

    private Player newPlayer(PlayerColor color, long money) {
        List<Tile> homeTiles = board.getHomeTiles(color);
        List<Piece> pieces = IntStream.range(0, homeTiles.size())
                .mapToObj(index -> new Piece(index, homeTiles.get(index)))
                .toList();
        return new Player(color.name(), color, money, pieces);
    }
}
