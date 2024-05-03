package game.marblesolitaire.view;

import game.marblesolitaire.model.single.MarbleSolitaireModelState;

/**
 * This class represents a MarbleSolitaireTextView. It offers all the operations
 * mandated by the MarbleSolitaireView interface.
 */
public class MarbleSolitaireTextView extends AbstractSolitaireView
        implements MarbleSolitaireView {

  /**
   * Constructs a MarbleSolitaireTextView object with the provided
   * MarbleSolitaireModelState.
   *
   * @param s The MarbleSolitaireModelState object representing the game.
   * @throws IllegalArgumentException if the provided model is null.
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState s) {
    this(s, System.out);
  }

  /**
   * Constructs a MarbleSolitaireTextView object with the provided
   * MarbleSolitaireModelState and Appendable.
   *
   * @param s The MarbleSolitaireModelState object representing the game.
   * @param out The Appendable object representing the state and any message
   *            of the game.
   * @throws IllegalArgumentException if the provided model is null.
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState s, Appendable out) {
    super(s, out);
  }

  /**
   * Returns a string representation of the current Marble Solitaire game
   * state. The string represents the game board. Each slot on the game
   * board is a single character (O, _ or space for a marble, empty and
   * invalid position respectively). Slots in a row should be separated by a
   * space. Each row has no space before the first slot and after the last
   * slot and each row has no extra space after the last marble or empty slot.
   * Each row has a new line.
   *
   * @return A string representation of the Marble Solitaire game state.
   */
  @Override
  public String toString() {
    int size = this.marbleS.getBoardSize();
    StringBuilder sb = new StringBuilder();
    for (int r = 0; r < size; r++) {
      if (r > 0) {
        sb.append("\n");
      }
      int lastValid = 0;
      for (int c = 0; c < size; c++) {
        if (c > 0) {
          sb.append(" ");
        }
        MarbleSolitaireModelState.SlotState marble = this.marbleS.getSlotAt(r, c);
        switch (marble) {
          case Invalid:
            sb.append(" ");
            break;
          case Empty:
            lastValid = c;
            sb.append("_");
            break;
          case Marble:
            lastValid = c;
            sb.append("O");
            break;

          // No action intended for this case
          default:
            break;
        }
      }
      sb.setLength(sb.length()
              - 2 * (size - lastValid - 1));
    }
    return sb.toString();
  }
}
