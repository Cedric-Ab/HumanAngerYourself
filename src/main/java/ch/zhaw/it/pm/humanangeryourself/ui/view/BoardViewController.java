package ch.zhaw.it.pm.humanangeryourself.ui.view;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.GameSnapshot;
import ch.zhaw.it.pm.humanangeryourself.api.snapshot.PieceInfo;
import ch.zhaw.it.pm.humanangeryourself.ui.viewmodel.GameViewModel;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Controller of the board view defined in {@code BoardView.fxml}.
 * <p>
 * On initialization it looks up the rectangles for the main tiles, the finish lanes and the homes
 * by their FXML ids and keeps them for later use.
 */
public class BoardViewController {
    /** Number of tiles on the main track. */
    public static final int NUM_TILES = 60;
    /** Number of player colors, and therefore of homes and staircases. */
    public static final int NUM_COLORS = 4;
    /** Number of tiles in each staircase. */
    public static final int LENGTH_STAIRCASE = 10;

    /** Home areas, one per color, in the order of their ids. */
    private final List<Rectangle> homes = new ArrayList<>(NUM_COLORS);
    /** Tiles of the main track, in the order of their ids. */
    private final List<Rectangle> tiles = new ArrayList<>(NUM_TILES);
    /** Staircases, one list of tiles per color. */
    private final List<List<Rectangle>> staircases = new ArrayList<>(NUM_COLORS);

    /** Pane containing all board elements, injected by the FXML loader. */
    @FXML
    private Pane boardPane;

    /** Finish area in the middle of the board, injected by the FXML loader. */
    @FXML
    private Pane piecePane;
    @FXML
    private StackPane boardRoot;
    @FXML
    private Rectangle finishArea;
    private GameViewModel viewModel;
    private MovePiece selectedMove;
    private Circle selectedToken;

    // region Initialization

    /**
     * Called by the FXML loader after the FXML file has been loaded. Collects all tiles,
     * finish lanes and homes from the board pane.
     *
     * @throws IllegalStateException if an expected element is missing in the FXML file
     */
    @FXML
    public void initialize() {
        initializeTiles();
        initializeStaircases();
        initializeHomes();
        boardPane.scaleXProperty()
                .bind(Bindings.min(boardRoot.widthProperty().divide(790), boardRoot.heightProperty().divide(790)));
        boardPane.scaleYProperty()
                .bind(Bindings.min(boardRoot.widthProperty().divide(790), boardRoot.heightProperty().divide(790)));
    }

    public void setViewModel(GameViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.snapshotProperty().addListener((ignored, oldSnapshot, newSnapshot) -> render(newSnapshot));
        render(viewModel.snapshotProperty().get());
    }

    private void render(GameSnapshot snapshot) {
        selectedMove = null;
        selectedToken = null;
        clearPreview();
        piecePane.getChildren().clear();

        if (snapshot == null) {
            return;
        }

        List<PieceInfo> pieces = snapshot.pieces();

        for (int index = 0; index < pieces.size(); index++) {
            PieceInfo piece = pieces.get(index);

            Rectangle location = locationFor(piece);
            if (location == null) {
                continue;
            }

            Circle token = createToken(piece.color());

            positionToken(
                    token,
                    location,
                    indexOnLocation(pieces, piece, index),
                    countOnLocation(pieces, piece));

            MovePiece move = movableMove(snapshot, piece);

            if (move != null) {
                enableMoveInteraction(token, move);
            }

            piecePane.getChildren().add(token);
        }
    }

    private int indexOnLocation(List<PieceInfo> pieces, PieceInfo piece, int index) {
        int slot = 0;
        for (int i = 0; i < index; i++) {
            PieceInfo previous = pieces.get(i);
            if (sameLocation(previous, piece))
                slot++;
        }
        return slot;
    }

    private int countOnLocation(List<PieceInfo> pieces, PieceInfo piece) {
        return (int) pieces.stream().filter(other -> sameLocation(other, piece)).count();
    }

    private boolean sameLocation(PieceInfo first, PieceInfo second) {
        return first.tile() == second.tile()
                && ((first.tile() >= 0 && first.tile() < NUM_TILES) || first.color() == second.color());
    }

    private Circle createToken(PlayerColor color) {
        Circle token = new Circle(10, colorFor(color));
        token.setStroke(Color.BLACK);
        token.setStrokeWidth(1.5);
        return token;
    }

    private void positionToken(Circle token, Rectangle tile, int slot, int total) {
        var bounds = tile.getBoundsInParent();
        int sideLength = (int) Math.ceil(Math.sqrt(total));
        int column = slot % sideLength;
        int row = slot / sideLength;
        double spacing = Math.min(36, Math.min(bounds.getWidth(), bounds.getHeight()) / (sideLength + 1));
        token.setRadius(Math.min(10, Math.max(4, spacing * 0.35)));
        token.setCenterX(bounds.getCenterX() + (column - (sideLength - 1) / 2.0) * spacing);
        token.setCenterY(bounds.getCenterY() + (row - (sideLength - 1) / 2.0) * spacing);
    }

    private MovePiece movableMove(GameSnapshot snapshot, PieceInfo piece) {
        if (snapshot.decision() == null || piece.color() != snapshot.decision().decider())
            return null;
        return snapshot.decision().availableActions().stream()
                .filter(MovePiece.class::isInstance)
                .map(MovePiece.class::cast)
                .filter(move -> move.startingTile() == piece.tile())
                .findFirst().orElse(null);
    }

    private void enableMoveInteraction(Circle token, MovePiece move) {
        token.setCursor(Cursor.HAND);
        token.setOnMouseEntered(event -> token.setEffect(new DropShadow(12, Color.GOLD)));
        token.setOnMouseExited(
                event -> token.setEffect(selectedMove != move ? null : new DropShadow(14, Color.ORANGE)));
        token.setOnMouseClicked(event -> selectOrSubmit(token, move));
    }

    private void selectOrSubmit(Circle token, MovePiece move) {
        if (selectedMove != null && selectedMove.equals(move)) {
            viewModel.submit(move);
            return;
        }
        if (selectedToken != null)
            selectedToken.setEffect(null);
        selectedMove = move;
        selectedToken = token;
        token.setEffect(new DropShadow(14, Color.ORANGE));
        clearPreview();
        Rectangle target = locationForTarget(move);
        if (target != null) {
            target.setStroke(Color.GOLD);
            target.setStrokeWidth(4);
        }
    }

    private void clearPreview() {
        for (Rectangle tile : tiles) {
            tile.setStroke(Color.BLACK);
            tile.setStrokeWidth(1);
        }
        for (List<Rectangle> lane : finishLanes) {
            for (Rectangle tile : lane) {
                tile.setStroke(Color.BLACK);
                tile.setStrokeWidth(1);
            }
        }
        finishArea.setStroke(Color.SADDLEBROWN);
        finishArea.setStrokeWidth(4);
    }

    private Rectangle locationFor(PieceInfo piece) {
        int tile = piece.tile();
        if (tile >= 0 && tile < NUM_TILES)
            return tiles.get(tile);
        if (tile >= 60 && tile < 100)
            return finishLanes.get(tileColorIndex(piece.color())).get(tile % 10);
        if (tile == 100)
            return finishArea;
        return homes.get(tileColorIndex(piece.color()));
    }

    private Rectangle locationForTarget(MovePiece move) {
        int tile = move.targetTile();
        if (tile >= 0 && tile < NUM_TILES)
            return tiles.get(tile);
        if (tile >= 60 && tile < 100) {
            // The target action is already legal; use its colour-independent lane encoding
            // if supplied.
            return finishLanes.get(Math.min(3, (tile - 60) / 10)).get(tile % 10);
        }
        return tile == 100 ? finishArea : null;
    }

    private int tileColorIndex(PlayerColor color) {
        return switch (color) {
            case RED -> 0;
            case YELLOW -> 1;
            case GREEN -> 2;
            case BLUE -> 3;
        };
    }

    private Color colorFor(PlayerColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case YELLOW -> Color.GOLD;
            case GREEN -> Color.LIMEGREEN;
            case BLUE -> Color.DODGERBLUE;
        };
    }

    /**
     * Looks up the tiles {@code #tile1} to {@code #tile60} of the main track.
     *
     * @throws IllegalStateException if a tile is missing
     */
    private void initializeTiles() {
        for (int i = 1; i <= NUM_TILES; i++) {
            String tileId = "#tile" + i;
            Rectangle tile = (Rectangle) boardPane.lookup(tileId);

            if (tile == null) {
                throw new IllegalStateException("Missing tile: " + tileId);
            }

            tiles.add(tile);
        }
        if (tiles.size() != NUM_TILES) {
            throw new IllegalStateException("Missing tiles, expected: " + NUM_TILES + ", found: " + tiles.size());
        }
    }

    /**
     * Looks up the staircase tiles of every color. The tile ids are built as
     * {@code (colorIndex + 1) * 100 + 5 + position}, for example {@code #tile105} to {@code #tile114}
     * for the first color.
     *
     * @throws IllegalStateException if a staircase or one of its tiles is missing
     */
    private void initializeStaircases() {
        for (int i = 0; i < NUM_COLORS; i++) {
            staircases.add(new ArrayList<>(LENGTH_STAIRCASE));
            for (int j = 0; j < LENGTH_STAIRCASE; j++) {
                String tileId = "#tile%d".formatted((i + 1) * 100 + 5 + j);
                Rectangle tile = (Rectangle) boardPane.lookup(tileId);

                if (tile == null) {
                    throw new IllegalStateException("Missing tile: " + tileId);
                }

                staircases.get(i).add(tile);
            }
        }
        if (staircases.size() != NUM_COLORS) {
            throw new IllegalStateException(
                    "Missing finish lanes, expected: %d, found: %d".formatted(NUM_COLORS, staircases.size()));
        }
        for (int i = 0; i < NUM_COLORS; i++) {
            if (staircases.get(i).size() != LENGTH_STAIRCASE) {
                throw new IllegalStateException(
                        "Missing finish lane tiles for color %d, expected: %d, found: %d".formatted(
                                i, LENGTH_STAIRCASE, staircases.get(i).size()));
            }
        }
    }

    /**
     * Looks up the home areas {@code #home0} to {@code #home3}, one per color.
     *
     * @throws IllegalStateException if a home is missing
     */
    private void initializeHomes() {
        for (int i = 0; i < NUM_COLORS; i++) {
            String tileId = "#home" + i;
            Rectangle tile = (Rectangle) boardPane.lookup(tileId);

            if (tile == null) {
                throw new IllegalStateException("Missing home: " + tileId);
            }

            homes.add(tile);
        }
        if (homes.size() != NUM_COLORS) {
            throw new IllegalStateException("Missing homes, expected: " + NUM_COLORS + ", found: " + homes.size());
        }
    }

    // endregion initializing
}
