package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PieceInfo;
import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Controller of the board. Draws the pieces of the snapshot and lets the
 * deciding player
 * choose one of the available {@link MovePiece} or {@link TargetPiece} actions
 * by clicking on the board.
 * <p>
 * Every board location has the fx:id {@code "tile" + tile id}, so tile ids from
 * the snapshot
 * are mapped directly to their shape without knowing how the board is numbered.
 * </p>
 */
public class BoardViewController {
    /**
     * Width and height of the board in the FXML file, used to scale it to the
     * available space.
     */
    private static final double BOARD_SIZE = 720;
    /** Prefix of the fx:id of every board location, followed by the tile id. */
    private static final String TILE_ID_PREFIX = "tile";

    /** Largest radius of a piece, used when a tile holds a single piece. */
    private static final double MAX_PIECE_RADIUS = 10;
    /** Smallest radius of a piece, so pieces stay visible on crowded tiles. */
    private static final double MIN_PIECE_RADIUS = 4;
    /** Largest distance between the centers of two pieces on the same tile. */
    private static final double MAX_PIECE_SPACING = 36;
    /** Radius of a piece relative to the spacing between pieces. */
    private static final double RADIUS_TO_SPACING_RATIO = 0.35;

    private static final String PIECE_STYLE = "piece";
    private static final String CLICKABLE_STYLE = "clickable";
    private static final String SELECTED_SOURCE_STYLE = "selected-source";
    private static final String MOVE_TARGET_STYLE = "move-target";
    private static final String ACTIVE_HOME_STYLE = "active-home";
    private static final String MOVABLE_PIECE_STYLE = "movable";

    private final Map<Integer, Shape> tiles = new HashMap<>();
    private final Map<PlayerColor, Shape> homes = new EnumMap<>(PlayerColor.class);

    @FXML
    private StackPane boardRoot;
    @FXML
    private Pane boardPane;
    @FXML
    private Pane piecePane;
    @FXML
    private Shape redHome;
    @FXML
    private Shape yellowHome;
    @FXML
    private Shape greenHome;
    @FXML
    private Shape blueHome;

    private GameViewModel viewModel;

    /**
     * Creates the controller. Called by the FXML loader.
     */
    public BoardViewController() {
    }

    /**
     * Called by the FXML loader. Collects the board locations and scales the board
     * to the available space.
     */
    @FXML
    public void initialize() {
        collectTiles();
        homes.putAll(Map.of(
                PlayerColor.RED, redHome,
                PlayerColor.YELLOW, yellowHome,
                PlayerColor.GREEN, greenHome,
                PlayerColor.BLUE, blueHome));
        bindBoardScale();
    }

    /**
     * Connects the board to the view model and redraws it after every change of the
     * game state.
     *
     * @param viewModel the view model of the game to display
     */
    public void setViewModel(GameViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.snapshotProperty().addListener((observable, oldSnapshot, newSnapshot) -> render(newSnapshot));
        render(viewModel.snapshotProperty().get());
    }

    private void render(GameSnapshot snapshot) {
        highlightActiveHome(snapshot.currentPlayer());
        drawPieces(snapshot.pieces(), movablePieceTiles(snapshot), snapshot.decision().decider());
        enableInteractions(snapshot);
    }

    /**
     * Returns the tiles of the pieces the deciding player can move with the
     * available {@link MovePiece} actions, e.g. after rolling the die.
     */
    private static Set<Integer> movablePieceTiles(GameSnapshot snapshot) {
        return availableActions(snapshot, MovePiece.class).stream()
                .map(MovePiece::startTile)
                .collect(Collectors.toSet());
    }

    private void highlightActiveHome(PlayerColor currentPlayer) {
        homes.forEach((color, home) -> setStyle(home, ACTIVE_HOME_STYLE, color == currentPlayer));
    }

    private void drawPieces(List<PieceInfo> pieces, Set<Integer> movableTiles, PlayerColor decider) {
        piecePane.getChildren().clear();
        Map<Integer, List<PieceInfo>> piecesByTile = pieces.stream()
                .collect(Collectors.groupingBy(PieceInfo::tile, LinkedHashMap::new, Collectors.toList()));
        piecesByTile.forEach((tileId, piecesOnTile) -> drawPiecesOnTile(tile(tileId), piecesOnTile,
                piece -> piece.color() == decider && movableTiles.contains(piece.tile())));
    }

    /**
     * Draws the pieces of one tile in a square grid centered on the tile and
     * highlights the pieces that can be moved.
     * <p>
     * The grid is laid out in the tile's own (unrotated) coordinates and then
     * transformed to the board, so rotated tiles (the diagonal track tiles) get
     * the same piece size as straight ones instead of being measured by their
     * larger axis-aligned bounding box.
     * </p>
     */
    private void drawPiecesOnTile(Shape tile, List<PieceInfo> piecesOnTile, Predicate<PieceInfo> isMovable) {
        Bounds bounds = tile.getBoundsInLocal();
        int gridSize = (int) Math.ceil(Math.sqrt(piecesOnTile.size()));
        double spacing = Math.min(MAX_PIECE_SPACING,
                Math.min(bounds.getWidth(), bounds.getHeight()) / (gridSize + 1));
        double radius = Math.clamp(spacing * RADIUS_TO_SPACING_RATIO, MIN_PIECE_RADIUS, MAX_PIECE_RADIUS);
        double gridCenter = (gridSize - 1) / 2.0;

        for (int slot = 0; slot < piecesOnTile.size(); slot++) {
            Point2D center = tile.localToParent(
                    bounds.getCenterX() + (slot % gridSize - gridCenter) * spacing,
                    bounds.getCenterY() + (slot / gridSize - gridCenter) * spacing);
            PieceInfo pieceInfo = piecesOnTile.get(slot);
            Circle piece = createPiece(pieceInfo.color(), center.getX(), center.getY(), radius);
            setStyle(piece, MOVABLE_PIECE_STYLE, isMovable.test(pieceInfo));
            piecePane.getChildren().add(piece);
        }
    }

    private Circle createPiece(PlayerColor color, double centerX, double centerY, double radius) {
        Circle piece = new Circle(centerX, centerY, radius);
        piece.getStyleClass().addAll(PIECE_STYLE, styleClassOf(color));
        return piece;
    }

    /**
     * Makes the start tile of every available move and every targetable piece
     * clickable.
     */
    private void enableInteractions(GameSnapshot snapshot) {
        resetInteractions();

        availableActions(snapshot, MovePiece.class).stream()
                .collect(Collectors.groupingBy(MovePiece::startTile))
                .forEach((startTile, moves) -> {
                    Shape source = tile(startTile);
                    onClick(source, () -> selectSource(source, moves));
                });

        availableActions(snapshot, TargetPiece.class)
                .forEach(target -> onClick(tile(target.targetTile()), () -> viewModel.submit(target)));
    }

    /**
     * Highlights the selected start tile and lets the player click one of its move
     * targets.
     */
    private void selectSource(Shape source, List<MovePiece> moves) {
        enableInteractions(viewModel.snapshotProperty().get());
        setStyle(source, SELECTED_SOURCE_STYLE, true);
        for (MovePiece move : moves) {
            Shape target = tile(move.targetTile());
            setStyle(target, MOVE_TARGET_STYLE, true);
            onClick(target, () -> viewModel.submit(move));
        }
    }

    private void resetInteractions() {
        for (Shape tile : tiles.values()) {
            tile.setOnMouseClicked(null);
            tile.getStyleClass().removeAll(CLICKABLE_STYLE, SELECTED_SOURCE_STYLE, MOVE_TARGET_STYLE);
        }
    }

    private void onClick(Shape shape, Runnable action) {
        setStyle(shape, CLICKABLE_STYLE, true);
        shape.setOnMouseClicked(event -> action.run());
    }

    private static <A extends Action> List<A> availableActions(GameSnapshot snapshot, Class<A> type) {
        return snapshot.decision().availableActions().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }

    private Shape tile(int tileId) {
        Shape tile = tiles.get(tileId);
        if (tile == null) {
            throw new IllegalStateException("The board has no location for tile id " + tileId);
        }
        return tile;
    }

    private static void setStyle(Node node, String styleClass, boolean enabled) {
        node.getStyleClass().remove(styleClass);
        if (enabled) {
            node.getStyleClass().add(styleClass);
        }
    }

    private static String styleClassOf(PlayerColor color) {
        return color.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Maps every node with the fx:id {@code "tile<id>"} to its tile id.
     */
    private void collectTiles() {
        for (Node node : boardPane.getChildren()) {
            String fxId = node.getId();
            if (fxId != null && fxId.startsWith(TILE_ID_PREFIX)) {
                tiles.put(Integer.parseInt(fxId.substring(TILE_ID_PREFIX.length())), (Shape) node);
            }
        }
    }

    private void bindBoardScale() {
        NumberBinding scale = Bindings.min(
                boardRoot.widthProperty().divide(BOARD_SIZE),
                boardRoot.heightProperty().divide(BOARD_SIZE));
        boardPane.scaleXProperty().bind(scale);
        boardPane.scaleYProperty().bind(scale);
    }
}
