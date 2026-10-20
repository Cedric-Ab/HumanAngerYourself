package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class BoardViewController {
    public static final int NUM_TILES = 60;
    public static final int NUM_COLORS = 4;
    public static final int LENGHT_FINISH_LANE = 10;

    private final List<Rectangle> homes = new ArrayList<>(NUM_COLORS);
    private final List<Rectangle> tiles = new ArrayList<>(NUM_TILES);
    private final List<List<Rectangle>> finishLanes = new ArrayList<>(NUM_COLORS);

    @FXML
    private Pane boardPane;
    @FXML
    private Rectangle finishArea;

    // region Initialization

    @FXML
    public void initialize() {
        initializeTiles();
        initializeFinishLanes();
        initializeHomes();
    }

    private void initializeTiles() {
        for (int i = 0; i < NUM_TILES; i++) {
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

    private void initializeFinishLanes() {
        for (int i = 0; i < NUM_COLORS; i++) {
            finishLanes.add(new ArrayList<>(LENGHT_FINISH_LANE));
            for (int j = 0; j < LENGHT_FINISH_LANE; j++) {
                String tileId = "#finish%d_%d".formatted(i, j);
                Rectangle tile = (Rectangle) boardPane.lookup(tileId);

                if (tile == null) {
                    throw new IllegalStateException("Missing tile: " + tileId);
                }

                finishLanes.get(i).add(tile);
            }
        }
        if (finishLanes.size() != NUM_COLORS) {
            throw new IllegalStateException(
                    "Missing finish lanes, expected: %d, found: %d".formatted(NUM_COLORS, finishLanes.size()));
        }
        for (int i = 0; i < NUM_COLORS; i++) {
            if (finishLanes.get(i).size() != LENGHT_FINISH_LANE) {
                throw new IllegalStateException(
                        "Missing finish lane tiles for color %d, expected: %d, found: %d".formatted(
                                i, LENGHT_FINISH_LANE, finishLanes.get(i).size()));
            }
        }
    }

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
