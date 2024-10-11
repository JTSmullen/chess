
package com.chess.engine.classic.pieces;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.BoardUtils;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Move.MajorAttackMove;
import com.chess.engine.classic.board.Move.MajorMove;
import com.chess.engine.classic.board.MoveUtils;

import java.util.*;

public final class Bishop extends Piece {

    // Positions a Bishop could move diagonally (-9 is up to the left : 9 is bottom to the right)
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

    //
    private final static Map<Integer, MoveUtils.Line[]> PRECOMPUTED_CANDIDATES = computeCandidates();

    // Create a bishop instance (Can create new instance after game start)
    public Bishop(final Alliance alliance,
                  final int piecePosition) {
        super(PieceType.BISHOP, alliance, piecePosition, true);
    }

    // Creates a new instance of bishop
    public Bishop(final Alliance alliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.BISHOP, alliance, piecePosition, isFirstMove);
    }

    /**
     * Finds possibles lines after a bishop moves first
     * @return a map with all possible combinations of legal lines
     */
    private static Map<Integer, MoveUtils.Line[]> computeCandidates() {

        Map<Integer, MoveUtils.Line[]> candidates = new HashMap<>();

        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {

            List<MoveUtils.Line> lines = new ArrayList<>();

            for (int offset : CANDIDATE_MOVE_COORDINATES) {

                int destination = position;
                MoveUtils.Line line = new MoveUtils.Line();

                while (BoardUtils.isValidTileCoordinate(destination)) {

                    if (isFirstColumnExclusion(destination, offset) ||
                            isEighthColumnExclusion(destination, offset)) {
                        break;
                    }

                    destination += offset;

                    if (BoardUtils.isValidTileCoordinate(destination)) {
                        line.addCoordinate(destination);
                    }

                }

                if (!line.isEmpty()) {
                    lines.add(line);
                }

            }

            if (!lines.isEmpty()) {
                candidates.put(position, lines.toArray(new MoveUtils.Line[0]));
            }

        }

        return Collections.unmodifiableMap(candidates);

    }

    /**
     * Given the current board state, calculates the legal moves for a certain piece
     * @param board the current board state of the game
     * @return a list with all possible legal moves for the bishop
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final MoveUtils.Line line : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {

            for (final int candidateDestinationCoordinate : line.getLineCoordinates()) {

                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);

                if (pieceAtDestination == null) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {

                    final Alliance pieceAlliance = pieceAtDestination.getPieceAllegiance();

                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }

                    break;
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);

    }

    // Returns position of bishop to calculate board state and positionally bonuses during board state
    @Override
    public int locationBonus() {
        return this.pieceAlliance.bishopBonus(this.piecePosition);
    }

    // Moves the piece to desired location
    @Override
    public Bishop movePiece(final Move move) {
        return PieceUtils.INSTANCE.getMovedBishop(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    // Edge case so the bishop cannot warp around the board
    private static boolean isFirstColumnExclusion(final int position,
                                                  final int offset) {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(position) &&
                ((offset == -9) || (offset == 7)));
    }

    // Edge case so bishop cannot warp around the board
    private static boolean isEighthColumnExclusion(final int position,
                                                   final int offset) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(position) &&
                ((offset == -7) || (offset == 9));
    }

}
