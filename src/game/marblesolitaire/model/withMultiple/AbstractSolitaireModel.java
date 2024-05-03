package game.marblesolitaire.model.withMultiple;

import game.marblesolitaire.model.single.MarbleSolitaireModel;

/**
 * Abstract Class representing an abstract marble solitaire model.
 */
public abstract class AbstractSolitaireModel implements MarbleSolitaireModel {

  protected SlotState[][] board;
  protected final int arm;

  /**
   * Constructor which takes in values for arm thickness and the empty slot row and column.
   * @param armLength the arm thickness.
   * @param sRow row of the empty slot.
   * @param sCol column of the empty slot.
   * @throws IllegalArgumentException when the given values are invalid.
   */
  public AbstractSolitaireModel(int armLength, int sRow, int sCol)
          throws IllegalArgumentException {

    int size = modelSize(armLength);
    arm = armLength;

    if (checkIndex(size, sRow, sCol)) {
      throw new IllegalArgumentException("Invalid empty cell position ("
              + sRow + ", " + sCol + ")");
    }
    initialBoard(size);
    if (checkInvalid(sRow,sCol)) {
      throw new IllegalArgumentException("Invalid empty cell position ("
              + sRow + ", " + sCol + ")");
    }
    board[sRow][sCol] = SlotState.Empty;
  }

  // Creates a board based on the given values.
  protected abstract void initialBoard(int size);

  // helper method that returns the int value of the size of the game.
  protected abstract int modelSize(int arm);

  // helper method that returns boolean value
  // if the index of the given cell in on game board.
  protected boolean checkIndex(int size, int row, int col) {
    return (row < 0 || col < 0 || row >= size || col >= size);
  }

  // helper method that returns boolean value
  // if the index of the given cell is invalid on board.
  protected boolean checkInvalid(int row, int col) {
    return board[row][col].equals(SlotState.Invalid);
  }

  /**
   * Move a single marble from a given position to another given position.
   * A move is valid only if both position are valid positions on board and
   * there is a marble at the specified 'from' position, empty at the specified
   * 'to' position, the 'to' and 'from' positions are exactly two positions
   * away (horizontally or vertically or diagonally based on the model class),
   * and a marble in the slot between the 'to' and 'from' positions.
   * After the move, the 'from' and middle slot will be empty and the 'to'
   * slot will have a marble.
   *
   * @param fromRow the row number of the position to be moved from (starts at 0).
   * @param fromCol the column number of the position to be moved from (starts at 0).
   * @param toRow the row number of the position to be moved to (starts at 0).
   * @param toCol the column number of the position to be moved to (starts at 0).
   * @throws IllegalArgumentException if the move is not possible or the
   *     positions are invalid.
   */
  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    if (checkIndex(getBoardSize(), fromRow, fromCol)
            || checkIndex(getBoardSize(), toRow, toCol)
            || checkInvalid(fromRow, fromCol) || checkInvalid(toRow, toCol)) {
      throw new IllegalArgumentException("Invalid move position.");
    }

    if (possibleMove(fromRow, fromCol, toRow, toCol)) {
      board[fromRow][fromCol] = SlotState.Empty;
      board[(fromRow + toRow) / 2][(fromCol + toCol) / 2] = SlotState.Empty;
      board[toRow][toCol] = SlotState.Marble;
    } else {
      throw new IllegalArgumentException("Not valid move.");
    }
  }

  // possible move helper method to see
  // if the move is possible vertically and horizontally
  protected boolean moveHelper(int fromRow, int fromCol, int toRow, int toCol) {
    return ((fromRow == toRow) && (Math.abs(fromCol - toCol) == 2))
            || ((fromCol == toCol) && Math.abs(fromRow - toRow) == 2);
  }

  // possible move method to see if the move is possible
  protected boolean possibleMove(int fromRow, int fromCol, int toRow, int toCol) {
    int midRow = (fromRow + toRow) / 2;
    int midCol = (fromCol + toCol) / 2;
    boolean validMarble;
    try {
      validMarble = board[fromRow][fromCol].equals(SlotState.Marble)
              && board[midRow][midCol].equals(SlotState.Marble)
              && board[toRow][toCol].equals(SlotState.Empty);
      return moveHelper(fromRow, fromCol, toRow, toCol) && validMarble;
    }
    catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  // helper method to see if there's a move for the given slot
  protected boolean hasMove(int row, int col) {
    if (!board[row][col].equals(SlotState.Marble)) {
      return false;
    }
    return possibleMove(row, col, row + 2, col)
            || possibleMove(row, col, row - 2, col)
            || possibleMove(row, col, row, col + 2)
            || possibleMove(row, col, row, col - 2);
  }

  @Override
  public boolean isGameOver() {
    for (int row = 0; row < getBoardSize(); row++) {
      for (int col = 0; col < getBoardSize(); col++) {
        if (hasMove(row, col)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    if (checkIndex(getBoardSize(), row, col)) {
      throw new IllegalArgumentException("Out of size position.");
    }
    return board[row][col];
  }

  @Override
  public int getScore() {
    int count = 0;
    for (SlotState[] marbles : board) {
      for (SlotState marble : marbles) {
        if (marble.equals(SlotState.Marble)) {
          count ++;
        }
      }
    }
    return count;
  }
}
