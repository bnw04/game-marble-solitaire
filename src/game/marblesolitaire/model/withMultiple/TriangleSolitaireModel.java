package game.marblesolitaire.model.withMultiple;

import game.marblesolitaire.model.single.MarbleSolitaireModel;

/**
 * This class represents a game of Triangular solitaire model and
 * implementation of the MarbleSolitaireModel interface and extends
 * the Marble Abstract class. It offers all the operations
 * mandated by the MarbleSolitaireModel interface.
 */
public class TriangleSolitaireModel extends AbstractSolitaireModel
        implements MarbleSolitaireModel {

  /**
   * Initializes the Triangular Solitaire game with a size of 5, and the empty
   * slot is set at the top of the board (0, 0).
   */
  public TriangleSolitaireModel() {
    this(5, 0, 0);
  }

  /**
   * Initializes the Triangular Solitaire game with the specified size.
   * The game board is initialized with the empty slot at the top of
   * the board (0, 0). It throws an IllegalArgumentException if the size
   * is not a positive number.
   *
   * @param armLength The size of the board.
   * @throws IllegalArgumentException if the size is not a positive number.
   */
  public TriangleSolitaireModel(int armLength) {
    this(armLength, 0, 0);
  }

  /**
   * Initializes the Triangular Solitaire game with a given empty slot position.
   * The game board is initialized with a size of 5, and the empty
   * slot is set at the position (sRow, sCol). If the specified position is
   * invalid, it throws an IllegalArgumentException.
   *
   * @param sRow The row index of the empty slot position.
   * @param sCol The column index of the empty slot position.
   * @throws IllegalArgumentException if the specified position is out of
   *     the board or the position is invalid.
   */
  public TriangleSolitaireModel(int sRow, int sCol) {
    this(5, sRow, sCol);
  }

  /**
   * Initializes the Triangular Solitaire game with the specified size(arm)
   * and empty slot position. The game board is initialized with the size,
   * and the empty slot is set at the position (sRow, sCol). It
   * throws an IllegalArgumentException if the size is not a
   * positive number, or the specified empty cell position is invalid.
   *
   * @param armLength The thickness of the arms in the game board.
   * @param sRow The row index of the empty slot position.
   * @param sCol The column index of the empty slot position.
   * @throws IllegalArgumentException if the arm thickness is not a positive
   *     odd number, or the specified position is invalid.
   */
  public TriangleSolitaireModel(int armLength, int sRow, int sCol) {
    super(armLength, sRow, sCol);
  }

  // helper method for the triangular game, throws illegal argument
  // exception if size is not a positive number and the size of
  // game is the arm number provided.
  @Override
  protected int modelSize(int arm) {
    if (arm < 1) {
      throw new IllegalArgumentException("Invalid arm length.");
    }
    return arm;
  }

  // helper method to initialize the Triangular Model board.
  @Override
  protected void initialBoard(int size) {
    board = new SlotState[size][size];
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (c <= r) {
          board[r][c] = SlotState.Marble;
        } else {
          board[r][c] = SlotState.Invalid;
        }
      }
    }
  }

  // possible move helper method to see if the move is possible horizontally
  // in its own row two columns away and diagonally in four directions two
  // rows above and below based on the triangular view of the board.
  @Override
  protected boolean moveHelper(int fromRow, int fromCol, int toRow, int toCol) {
    boolean squareShapedMove = super.moveHelper(fromRow, fromCol, toRow, toCol);
    boolean diagonalMove = (fromRow - toRow) == (fromCol - toCol)
            && Math.abs(fromRow - toRow) == 2;
    return squareShapedMove || diagonalMove;
  }

  // helper method to see if there's a move for the given slot
  @Override
  protected boolean hasMove(int row, int col) {
    boolean squareShapedHas = super.hasMove(row, col);
    return squareShapedHas || possibleMove(row, col, row + 2, col + 2)
            || possibleMove(row, col, row - 2, col - 2);
  }

  /**
   * Returns the size of the game board. The size is calculated based
   * on the number of slots in the bottom-most row, the arm of the board.
   *
   * @return The size of the game board.
   */
  @Override
  public int getBoardSize() {
    return arm;
  }
}
