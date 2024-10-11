package com.chess.engine.classic.board;
import com.chess.engine.classic.pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class Tile {

    protected final int tileCoordinate;

    private static final Map<Integer, emptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Integer,emptyTile> createAllPossibleEmptyTiles() {

        final Map<Integer, emptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new emptyTile(i));
        }

        return Collections.unmodifiableMap(emptyTileMap);
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new occupiedTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate);
    }
    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract Boolean isTileOccupied();

    public abstract Piece getPiece();

    public static final class emptyTile extends Tile{

        private emptyTile(int coordinate){
            super(coordinate);
        }

        @Override
        public Boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

    }

    public static final class occupiedTile extends Tile{

        private final Piece pieceOnTile;

        private occupiedTile(int coordinate, Piece pieceOnTile){
            super(coordinate);

            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public Boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }

}
