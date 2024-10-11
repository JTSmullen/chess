package com.chess.engine.classic.pieces;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.BoardUtils;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Move.MajorAttackMove;
import com.chess.engine.classic.board.Move.MajorMove;

import java.util.*;

public final class King extends Piece {

    // Positions a King could move
    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

    //
    private final static Map<Integer, int[]> PRECOMPUTED_CANDIDATES = computeCandidates();

    // if true cannot castle
    private final boolean isCastled;

    // whether a king side castle is possible or not this turn
    private final boolean kingSideCastleCapable;

    // whether a queen side castle is possible or not this turn
    private final boolean queenSideCastleCapable;

    // Create an instance of King
    public King(final Alliance alliance, final int piecePosition, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {

        super(PieceType.KING, alliance, piecePosition, true);

        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;

    }

    // Create an instance of King, (can only create one of each alliance)
    public King(final Alliance alliance, final int piecePosition, final boolean isFirstMove, final boolean isCastled, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {

        super(PieceType.KING, alliance, piecePosition, isFirstMove);

        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;

    }

    private static Map<Integer, int[]> computeCandidates() {

        final Map<Integer, int[]> candidates = new HashMap<>();

        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {

            int[] legalOffsets = new int[CANDIDATE_MOVE_COORDINATES.length];
            int numLegalOffsets = 0;

            for (int offset : CANDIDATE_MOVE_COORDINATES) {

                if (isFirstColumnExclusion(position, offset) ||
                        isEighthColumnExclusion(position, offset)) {
                    continue;
                }

                int destination = position + offset;

                if (BoardUtils.isValidTileCoordinate(destination)) {
                    legalOffsets[numLegalOffsets++] = offset;
                }

            }

            if (numLegalOffsets > 0) {
                candidates.put(position, Arrays.copyOf(legalOffsets, numLegalOffsets));
            }

        }

        return Collections.unmodifiableMap(candidates);

    }

    public boolean isCastled() {
        return this.isCastled;
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }

    /**
     * Given the current board state, calculates the legal moves
     * @param board the current board state of the game
     * @return a list with all possible legal moves for the king
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);

            if (pieceAtDestination == null) {
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else {

                final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();

                if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                    legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                            pieceAtDestination));
                }

            }

        }

        return Collections.unmodifiableList(legalMoves);

    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    // Returns position of the King to calculate board state and positional bonuses during the current board state
    @Override
    public int locationBonus() {
        return this.pieceAlliance.kingBonus(this.piecePosition);
    }

    // Moves the King to the desired location
    @Override
    public King movePiece(final Move move) {
        return new King(this.pieceAlliance, move.getDestinationCoordinate(), false, move.isCastlingMove(), false, false);
    }

    // Finds whether the instance of King has castled or not.
    @Override
    public boolean equals(final Object other) {

        if (this == other) {
            return true;
        }

        if (!(other instanceof King)) {
            return false;
        }

        if (!super.equals(other)) {
            return false;
        }

        final King king = (King) other;
        return isCastled == king.isCastled;

    }

    @Override
    public int hashCode() {
        return (31 * super.hashCode()) + (isCastled ? 1 : 0);
    }

    // Edge case so the King cannot warp around the board
    private static boolean isFirstColumnExclusion(final int currentCandidate, final int candidateDestinationCoordinate) {

        return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentCandidate) && ((candidateDestinationCoordinate == -9) || (candidateDestinationCoordinate == -1) || (candidateDestinationCoordinate == 7));

    }

    // Edge case so the King cannot warp around the board
    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentCandidate)
                && ((candidateDestinationCoordinate == -7) || (candidateDestinationCoordinate == 1) ||
                (candidateDestinationCoordinate == 9));
    }
}