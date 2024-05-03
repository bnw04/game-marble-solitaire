package game.marblesolitaire;

/**
 * This class represents a Parser and implementation of the IParser interface.
 * It offers all the operations mandated by the IParser interface.
 */
public class Parser implements IParser {
  private final String[] args;
  private int arm;
  private int sRow;
  private int sCol;

  /**
   * Parse the given command line with provided default arm/size, starting
   * empty cell row and col value. Constructs the Parse object by reading
   * the command line and updating arm/size and sRow/sCol.
   *
   * @param args The command line in format of a number of string values.
   * @param arm The default arm/size value of the game board when no size value
   *            can be read from the cmd.
   * @param sRow The default starting row value of the game board when no empty cell
   *            value can be read from the cmd.
   * @param sCol The default starting row value of the game board when no empty cell
   *             value can be read from the cmd.
   */
  public Parser(String[] args, int arm, int sRow, int sCol) {
    this.args = args;
    this.arm = arm;
    this.sRow = sRow;
    this.sCol = sCol;
    parseInput();
  }

  // helper method that reads if any arm/size, starting empty cell.
  private void parseInput() {
    for (int i = 1; i < this.args.length; i++) {
      if (this.args[i].equals("-size")) {
        try {
          this.arm = Integer.parseInt(this.args[i + 1]);
          // if not Triangle type, update the start hole to the middle
          if (this.sRow != 0 || this.sCol != 0) {
            this.sRow = 3 * this.arm / 2 - 1;
            this.sCol = 3 * this.arm / 2 - 1;
          }
        } catch (Exception e) {
          System.out.println("Size cannot be found.");
        }
      }
      if (this.args[i].equals("-hole")) {
        try {
          this.sRow = Integer.parseInt(this.args[i + 1]) - 1;
          this.sCol = Integer.parseInt(this.args[i + 2]) - 1;
        } catch (Exception e) {
          System.out.println("Start hole is invalid.");
        }
      }
    }
  }

  @Override
  public int getArm() {
    return this.arm;
  }

  @Override
  public int getSRow() {
    return this.sRow;
  }

  @Override
  public int getSCol() {
    return this.sCol;
  }
}
