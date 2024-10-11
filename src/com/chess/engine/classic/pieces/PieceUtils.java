package com.chess.engine.classic.pieces;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.BoardUtils;

import java.util.HashMap;
import java.util.Map;

enum PieceUtils {

    INSTANCE;

    private final Map<Alliance, Map<Integer, Pawn>> ALL_POSSIBLE_PAWNS = createAllPossibleMovedPawns();
    private final Map<Alliance, Map<Integer, Knight>> ALL_POSSIBLE_KNIGHTS = createAllPossibleMovedKnights();
    private final Map<Alliance, Map<Integer, Bishop>> ALL_POSSIBLE_BISHOPS = createAllPossibleMovedBishops();
    private final Map<Alliance, Map<Integer, Rook>> ALL_POSSIBLE_ROOKS = createAllPossibleMovedRooks();
    private final Map<Alliance, Map<Integer, Queen>> ALL_POSSIBLE_QUEENS = createAllPossibleMovedQueens();

    Pawn getMovedPawn(final Alliance alliance,
                      final int destinationCoordinate) {
        return ALL_POSSIBLE_PAWNS.get(alliance).get(destinationCoordinate);
    }

    Knight getMovedKnight(final Alliance alliance,
                          final int destinationCoordinate) {
        return ALL_POSSIBLE_KNIGHTS.get(alliance).get(destinationCoordinate);
    }

    Bishop getMovedBishop(final Alliance alliance,
                          final int destinationCoordinate) {
        return ALL_POSSIBLE_BISHOPS.get(alliance).get(destinationCoordinate);
    }

    Rook getMovedRook(final Alliance alliance,
                      final int destinationCoordinate) {
        return ALL_POSSIBLE_ROOKS.get(alliance).get(destinationCoordinate);
    }

    Queen getMovedQueen(final Alliance alliance,
                        final int destinationCoordinate) {
        return ALL_POSSIBLE_QUEENS.get(alliance).get(destinationCoordinate);
    }

    private Map<Alliance, Map<Integer, Pawn>> createAllPossibleMovedPawns() {
        final Map<Alliance, Map<Integer, Pawn>> pieces = new HashMap<>();
        for (final Alliance alliance : Alliance.values()) {
            final Map<Integer, Pawn> pawnMap = new HashMap<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                pawnMap.put(i, new Pawn(alliance, i, false));
            }
            pieces.put(alliance, pawnMap);
        }
        return pieces;
    }

    private Map<Alliance, Map<Integer, Knight>> createAllPossibleMovedKnights() {
        final Map<Alliance, Map<Integer, Knight>> pieces = new HashMap<>();
        for (final Alliance alliance : Alliance.values()) {
            final Map<Integer, Knight> knightMap = new HashMap<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                knightMap.put(i, new Knight(alliance, i, false));
            }
            pieces.put(alliance, knightMap);
        }
        return pieces;
    }

    private Map<Alliance, Map<Integer, Bishop>> createAllPossibleMovedBishops() {
        final Map<Alliance, Map<Integer, Bishop>> pieces = new HashMap<>();
        for (final Alliance alliance : Alliance.values()) {
            final Map<Integer, Bishop> bishopMap = new HashMap<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                bishopMap.put(i, new Bishop(alliance, i, false));
            }
            pieces.put(alliance, bishopMap);
        }
        return pieces;
    }

    private Map<Alliance, Map<Integer, Rook>> createAllPossibleMovedRooks() {
        final Map<Alliance, Map<Integer, Rook>> pieces = new HashMap<>();
        for (final Alliance alliance : Alliance.values()) {
            final Map<Integer, Rook> rookMap = new HashMap<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                rookMap.put(i, new Rook(alliance, i, false));
            }
            pieces.put(alliance, rookMap);
        }
        return pieces;
    }

    private Map<Alliance, Map<Integer, Queen>> createAllPossibleMovedQueens() {
        final Map<Alliance, Map<Integer, Queen>> pieces = new HashMap<>();
        for (final Alliance alliance : Alliance.values()) {
            final Map<Integer, Queen> queenMap = new HashMap<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                queenMap.put(i, new Queen(alliance, i, false));
            }
            pieces.put(alliance, queenMap);
        }
        return pieces;
    }
}