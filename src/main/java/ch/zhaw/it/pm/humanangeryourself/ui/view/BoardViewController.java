package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleFunction;

public class BoardViewController {
    public static final int NUM_TILES = 60;
    public static final int NUM_COLORS = 10;
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

        verifyInitialization();
    }

    private void initializeTiles() {
        for (int i = 0; i < NUM_TILES; i++) {
            Rectangle tile = (Rectangle) boardPane.lookup("#tile" + i);

            if (tile == null) {
                throw new IllegalStateException("Missing tile" + i);
            }

            tiles.add(tile);
        }
    }

    private void initializeFinishLanes() {
        for (int i = 0; i < NUM_COLORS; i++) {
            finishLanes.add(new ArrayList<>(LENGHT_FINISH_LANE));
            for (int j = 0; j < LENGHT_FINISH_LANE; j++) {
                Rectangle tile = (Rectangle) boardPane.lookup("#finish%d_%d".formatted(i, j));

                if (tile == null) {
                    throw new IllegalStateException("Missing tile: " + "#finish%d_%d".formatted(i, j));
                }

                finishLanes.get(i).add(tile);
            }
        }
    }

    private void initializeHomes() {
        for (int i = 0; i < NUM_COLORS; i++) {
            Rectangle tile = (Rectangle) boardPane.lookup("#home" + i);

            if (tile == null) {
                throw new IllegalStateException("Missing home" + i);
            }

            homes.add(tile);
        }
    }

    private void verifyInitialization() {
        if (tiles.size() != NUM_TILES) {
            throw new IllegalStateException("Missing tiles, expected: " + NUM_TILES + ", found: " + tiles.size());
        }
        if (finishLanes.size() != NUM_COLORS) {
            throw new IllegalStateException("Missing finish lanes, expected: " + NUM_COLORS + ", found: " + finishLanes.size());
        }
        for (int i = 0; i < NUM_COLORS; i++) {
            if (finishLanes.get(i).size() != LENGHT_FINISH_LANE) {
                throw new IllegalStateException("Missing finish lane tiles for color " + i + ", expected: " + LENGHT_FINISH_LANE + ", found: " + finishLanes.get(i).size());
            }
        }
        if (homes.size() != NUM_COLORS) {
            throw new IllegalStateException("Missing homes, expected: " + NUM_COLORS + ", found: " + homes.size());
        }
    }

    // endregion initializing
}
