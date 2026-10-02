package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class BoardViewController {
    public static final int NUM_TILES = 60;
    public static final int NUM_COLORS = 10;
    public static final int LENGHT_FINISH_LANE = 10;

    @FXML
    private Pane boardPane;

    @FXML
    private Rectangle redHome;
    @FXML
    private Rectangle greenHome;
    @FXML
    private Rectangle yellowHome;
    @FXML
    private Rectangle blueHome;
    @FXML
    private Rectangle finishArea;

    private final Rectangle[] tiles = new Rectangle[NUM_TILES];
    private final Rectangle[][] finishLanes = new Rectangle[NUM_COLORS][LENGHT_FINISH_LANE];

    @FXML
    public void initialize() {
        initializeTiles();
        initializeFinishLanes();
    }

    private void initializeTiles() {
        for (int i = 0; i < NUM_TILES; i++) {
            Rectangle tile = (Rectangle) boardPane.lookup("#tile" + i);

            if (tile == null) {
                throw new IllegalStateException("Missing tile" + i);
            }

            tiles[i] = tile;
        }
    }

    private void initializeFinishLanes() {
        for (int i = 0; i < NUM_COLORS; i++) {
            for (int j = 0; j < LENGHT_FINISH_LANE; j++) {
                Rectangle tile = (Rectangle) boardPane.lookup("#finish%d_%d".formatted(i, j));

                if (tile == null) {
                    throw new IllegalStateException("Missing tile: " + "#finish%d_%d".formatted(i, j));
                }

                finishLanes[i][j] = tile;
            }
        }
    }
}
