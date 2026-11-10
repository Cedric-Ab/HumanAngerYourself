package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

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
    private Rectangle finishArea;

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
