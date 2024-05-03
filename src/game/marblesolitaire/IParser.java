package game.marblesolitaire;

/**
 * To represent Parser for the marble solitaire game that reads
 * the command line.
 */
public interface IParser {

  /**
   * Return if the arm/size of the game based on the command line.
   *
   * @return the arm/size of the game as an integer.
   */
  int getArm();

  /**
   * Return the row of the starting empty cell of the game based
   * on the command line.
   *
   * @return the row of the starting empty cell of the game as an integer.
   */
  int getSRow();

  /**
   * Return the column of the starting empty cell of the game based
   * on the command line.
   *
   * @return the column of the starting empty cell of the game as an integer.
   */
  int getSCol();
}
