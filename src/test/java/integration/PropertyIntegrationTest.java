package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PropertyInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Property Integration Test")
public class PropertyIntegrationTest extends BaseIntegrationTest {

    private Action getFirstBuyProperty() {
        return availableActions().stream()
                .filter(BuyProperty.class::isInstance)
                .findFirst()
                .orElseThrow();
    }

    @Test
    @DisplayName("Paying Rent")
    void payingRent() {
        setPieceOntoTile(getPieces().getFirst(), 6);
        rushThroughPrimaryAction();
        submitChecked(getFirstBuyProperty());
        submitChecked(new EndTurn());

        long moneyBefore = getSnapshot().players().stream()
                .filter(playerInfo -> playerInfo.color() == decider())
                .findFirst()
                .orElseThrow()
                .money();

        rushThroughTurn();

        long moneyAfter = getSnapshot().players().stream()
                .filter(playerInfo -> playerInfo.color() == decider())
                .findFirst()
                .orElseThrow()
                .money();

        assertTrue(moneyAfter < moneyBefore);
    }

    @Nested
    @DisplayName("Purchasing")
    class Purchasing {
        private static final int TILE_ID_SAFE_SPACE = 5;
        private static final int[] TILE_ID_NORMAL_TILES = new int[]{6, 7, 8, 9, 11, 12};
        private static final int TILE_ID_NORMAL_TILE_OTHER_QUARTER = 22;

        @BeforeEach
        void setUp() {
            // provide all players with a lot of money so they can buy properties without worrying about their balance
            for (int i = 0; i < getGame().getPlayers().size(); i++) {
                setGambleResult(1_000_000);
                submitChecked(new Gamble());
                submitChecked(new EndTurn());
            }
        }

        @Test
        @DisplayName("Can buy normal Tile")
        void canBuyNormalTile() {
            setPieceOntoTile(getFirstPiece(), TILE_ID_NORMAL_TILES[0]);

            rushThroughPrimaryAction();

            assertTrue(availableActions().contains(new BuyProperty(TILE_ID_NORMAL_TILES[0], PropertySize.SMALL)));
            assertTrue(availableActions().contains(new BuyProperty(TILE_ID_NORMAL_TILES[0], PropertySize.MEDIUM)));
            assertTrue(availableActions().contains(new BuyProperty(TILE_ID_NORMAL_TILES[0], PropertySize.LARGE)));
        }

        @Test
        @DisplayName("Cannot buy Safe Space")
        void cannotBuySafeSpace() {
            setPieceOntoTile(getFirstPiece(), TILE_ID_SAFE_SPACE);

            rushThroughPrimaryAction();

            assertTrue(availableActions().stream().noneMatch(BuyProperty.class::isInstance));
        }

        @Test
        @DisplayName("Only 5 Properties per Quarter can be bought")
        void onlyFivePropertiesPerQuarter() {
            for (int i = 0; i < 5; i++) {
                setPieceOntoTile(getFirstPiece(), TILE_ID_NORMAL_TILES[i]);

                rushThroughPrimaryAction();

                submitChecked(getFirstBuyProperty());

                submitChecked(new EndTurn());
            }

            setPieceOntoTile(getFirstPiece(), TILE_ID_NORMAL_TILES[5]);

            rushThroughPrimaryAction();

            assertTrue(availableActions().stream().noneMatch(BuyProperty.class::isInstance));
        }

        @Test
        @DisplayName("More than 5 Properties can be bought if they are in different Quarters")
        void moreThanFivePropertiesDifferentQuarters() {
            for (int i = 0; i < 5; i++) {
                setPieceOntoTile(getFirstPiece(), TILE_ID_NORMAL_TILES[i]);

                rushThroughPrimaryAction();

                submitChecked(getFirstBuyProperty());

                submitChecked(new EndTurn());
            }

            setPieceOntoTile(getFirstPiece(), TILE_ID_NORMAL_TILE_OTHER_QUARTER);

            rushThroughPrimaryAction();

            assertTrue(availableActions().stream().anyMatch(BuyProperty.class::isInstance));
        }

        @Test
        @DisplayName("Buying a Property")
        void buyingAProperty() {
            setPieceOntoTile(getFirstPiece(), TILE_ID_NORMAL_TILES[0]);

            rushThroughPrimaryAction();

            submitChecked(getFirstBuyProperty());

            assertTrue(getSnapshot().properties().contains(
                    new PropertyInfo(TILE_ID_NORMAL_TILES[0], decider(), PropertySize.SMALL)));
        }

        @Test
        @DisplayName("Can buy multiple Properties on the same Turn")
        void canBuyMultiplePropertiesSameTurn() {
            setPieceOntoTile(getPieces().get(0), TILE_ID_NORMAL_TILES[0]);
            setPieceOntoTile(getPieces().get(1), TILE_ID_NORMAL_TILES[1]);

            rushThroughPrimaryAction();

            assertTrue(availableActions().contains(new BuyProperty(TILE_ID_NORMAL_TILES[0], PropertySize.SMALL)));
            assertTrue(availableActions().contains(new BuyProperty(TILE_ID_NORMAL_TILES[1], PropertySize.SMALL)));

            submitChecked(getFirstBuyProperty());

            assertTrue(availableActions().stream().anyMatch(BuyProperty.class::isInstance));
        }
    }
}
