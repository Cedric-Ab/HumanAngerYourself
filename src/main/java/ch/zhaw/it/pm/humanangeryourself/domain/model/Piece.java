package ch.zhaw.it.pm.humanangeryourself.domain.model;

public class Piece {
    private final Tile homeTile;
    private final Tile currentTile;

    public Piece(Tile homeTile) {
        this.homeTile = homeTile;
        this.currentTile = homeTile;
    }

    public void moveTo(Tile tile) {
        // TODO: implement
    }

    public void sendHome() {
        // TODO: implement
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Tile getHomeTile() {
        return homeTile;
    }
}
