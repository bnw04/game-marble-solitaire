package game.marblesolitaire.model.withMultiple;

/**
 * Abstract Class representing an abstract square shaped marble
 * solitaire model, including English and European models for now.
 */
public abstract class AbstractSquareShapedModels extends AbstractSolitaireModel {

  /**
   * Constructor which takes in values for arm thickness and the empty slot row and column.
   * @param armLength the arm thickness.
   * @param sRow row of the empty slot.
   * @param sCol column of the empty slot.
   * @throws IllegalArgumentException when the given values are invalid.
   */
  public AbstractSquareShapedModels(int armLength, int sRow, int sCol)
          throws IllegalArgumentException {
    super(armLength, sRow, sCol);
  }

  // helper method for the square shaped game, throws illegal argument
  // exception if arm value is not a positive odd number and the size of
  // game calculated based on arm length.
  @Override
  protected int modelSize(int arm) {
    if (arm < 1 || arm % 2 == 0) {
      throw new IllegalArgumentException("Invalid arm length.");
    }
    return (3 * arm) - 2;
  }

  /**
   * Returns the size of the game board. The size is calculated based
   * on the current arm length of the game.
   *
   * @return The size of the game board.
   */
  @Override
  public int getBoardSize() {
    return (3 * arm) - 2;
  }
}
