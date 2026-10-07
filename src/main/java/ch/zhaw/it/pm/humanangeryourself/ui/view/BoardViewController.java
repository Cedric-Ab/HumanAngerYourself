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

    private static @NotNull List<Object> sortPieces(Set<Object> pieces) {
        return pieces.stream()
                .toList();
    }

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

    // region Piece rendering

//     public void registerPieces(List<Piece> pieces) {
//
//     }

    // TODO: add @NotNull etc.
    // TODO: add parameter checks etc.

    public void renderPieces() {

    }

    // will actually require: Map<Rectangle, Set<Piece>> tileOccupancyMap
    // will actually return: Map<Piece, Point>
    private void calculatePositions(Map<Rectangle, Set<Object>> tileOccupancyMap) {
        for (Rectangle tile : tileOccupancyMap.keySet()) {
            if (tiles.contains(tile) || finishLanes.stream().anyMatch(lane -> lane.contains(tile))) {
                calculatePositionOnTile(tile, tileOccupancyMap.get(tile));
            } else if (finishArea.equals(tile) || homes.contains(tile)) {
                calculatePositionOnSquare(tile, tileOccupancyMap.get(tile));
            }

        }
    }

    // will actually return: Map<Piece, Point>
    private void calculatePositionOnTile(Rectangle tile, Set<Object> pieces) {
        List<Object> piecesSorted = sortPieces(pieces);

        double width = tile.getWidth();
        double height = tile.getHeight();

        double sideDistance = Math.max(width, height) / pieces.size();

        DoubleFunction<Point> pointCreator;
        if (width > height) {
            pointCreator = x -> new Point(x, height / 2);
        } else {
            pointCreator = y -> new Point(width / 2, y);
        }

        Map<Object, Point> result = new HashMap<>();
        for (int i = 0; i < pieces.size(); i++) {
            Point point = pointCreator.apply((i + 1) * sideDistance);
            result.put(piecesSorted.get(i), point);
        }

//         return result;
    }

    // will actually return: Map<Piece, Point>
    private void calculatePositionOnSquare(Rectangle tile, Set<Object> pieces) {
        List<Object> piecesSorted = sortPieces(pieces);

        int numPieces = pieces.size();

        int[] numPiecesPerAxis = calculateNumPiecesPerAxis(numPieces);

        double[] spacings = {
                tile.getWidth() / (numPiecesPerAxis[0] + 1),
                tile.getHeight() / (numPiecesPerAxis[1] + 1)
        };

        Map<Object, Point> result = new HashMap<>();
        for (int i_y = 0; i_y < numPiecesPerAxis[1]; i_y++) {
            for (int i_x = 0; i_x < numPiecesPerAxis[0]; i_x++) {
                int pieceIndex = i_y * numPiecesPerAxis[0] + i_x;

                if (pieceIndex >= numPieces) break;

                Point point = new Point(
                        (i_x + 1) * spacings[0],
                        (i_y + 1) * spacings[1]
                );
                result.put(piecesSorted.get(pieceIndex), point);
            }
        }
//         return result;
    }

    private int[] calculateNumPiecesPerAxis(int numPieces) {
        int x = (int) Math.ceil(Math.sqrt(numPieces));
        @SuppressWarnings({"ReassignedVariable", "SuspiciousNameCombination"})
        int y = x;

        while (x * y > numPieces) {
            y--;
        }
        y++;

        return new int[]{x, y};
    }

    // endregion Piece rendering

    public record Point(double x, double y) {
    }
}
