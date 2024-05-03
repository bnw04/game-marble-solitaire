package game.marblesolitaire.model.withMultiple;

import game.marblesolitaire.model.single.MarbleSolitaireModel;

/**
 * This class represents a game of European solitaire model and
 * implementation of the MarbleSolitaireModel interface and extends
 * the SquareShaped Abstract class. It offers all the operations
 * mandated by the MarbleSolitaireModel interface.
 */
public class EuropeanSolitaireModel extends AbstractSquareShapedModels
        implements MarbleSolitaireModel {

  /**
   * Initializes the European Solitaire game with an arm thickness of 3, and the empty
   * slot is set at center of the board (3, 3).
   */
  public EuropeanSolitaireModel() {
    this(3, 3, 3);
  }

  /**
   * Initializes the European Solitaire game with the specified arm thickness.
   * The game board is initialized with the empty slot at the center of
   * the board. It throws an IllegalArgumentException if the arm thickness
   * is not a positive odd number.
   *
   * @param armLength The thickness of the arms in the game board.
   * @throws IllegalArgumentException if the arm thickness is not a positive odd number.
   */
  public EuropeanSolitaireModel(int armLength) {
    this(armLength, 3 * armLength / 2 - 1, 3 * armLength / 2 - 1);
  }

  /**
   * Initializes the European Solitaire game with a given empty slot position.
   * The game board is initialized with an arm thickness of 3, and the empty
   * slot is set at the position (sRow, sCol). If the specified position is
   * invalid, it throws an IllegalArgumentException.
   *
   * @param sRow The row index of the empty slot position.
   * @param sCol The column index of the empty slot position.
   * @throws IllegalArgumentException if the specified position is out of
   *     the board or the position is invalid.
   */
  public EuropeanSolitaireModel(int sRow, int sCol) {
    this(3, sRow, sCol);
  }

  /**
   * Initializes the European Solitaire game with the specified arm thickness
   * and empty slot position. The game board is initialized with the arm
   * thickness, and the empty slot is set at the position (sRow, sCol). It
   * throws an IllegalArgumentException if the arm thickness is not a
   * positive odd number, or the specified empty cell position is invalid.
   *
   * @param armLength The thickness of the arms in the game board.
   * @param sRow The row index of the empty slot position.
   * @param sCol The column index of the empty slot position.
   * @throws IllegalArgumentException if the arm thickness is not a positive
   *     odd number, or the specified position is invalid.
   */
  public EuropeanSolitaireModel(int armLength, int sRow, int sCol) {
    super(armLength, sRow, sCol);
  }

  // helper method to initialize the European Model board.
  @Override
  protected void initialBoard(int size) {
    int midS = arm - 1;
    int midE = (2 * arm) - 2;
    board = new SlotState[size][size];
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if ((r < midS)
                && (c <= midS - 1 - r || c > midE + r)) {
          board[r][c] = SlotState.Invalid;
        } else if ((r > midE)
                && (c <= r - 1 - midE || c > midE + size - 1 - r)) {
          board[r][c] = SlotState.Invalid;
        } else {
          board[r][c] = SlotState.Marble;
        }
      }
    }
  }
}
