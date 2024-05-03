package game.marblesolitaire.view;

import game.marblesolitaire.model.single.MarbleSolitaireModelState;

/**
 * This class represents a TriangleSolitaireTextView. It offers all the operations
 * mandated by the MarbleSolitaireView interface.
 */
public class TriangleSolitaireTextView extends AbstractSolitaireView
        implements MarbleSolitaireView {

  /**
   * Constructs a TriangleSolitaireTextView object with the provided
   * MarbleSolitaireModelState.
   *
   * @param s The MarbleSolitaireModelState object representing the game.
   * @throws IllegalArgumentException if the provided model is null.
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState s) {
    this(s, System.out);
  }

  /**
   * Constructs a TriangleSolitaireTextView object with the provided
   * MarbleSolitaireModelState and Appendable.
   *
   * @param s The MarbleSolitaireModelState object representing the game.
   * @param out The Appendable object representing the state and any message
   *            of the game.
   * @throws IllegalArgumentException if the provided model is null.
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState s, Appendable out) {
    super(s, out);
  }

  /**
   * Returns a string representation of the current Triangular Solitaire game
   * state. The string represents the game board. Each slot on the game
   * board is a single character (O, _ or space for a marble, empty and
   * invalid position respectively). Slots in a row should be separated by a
   * space. Rows have leading spaces(size - row number) before the first slot
   * and no space after the last slot to form the triangular shape view.
   * Each row has no extra space after the last marble or empty slot.
   * Each row has a new line.
   *
   * @return A string representation of the Triangular Marble Solitaire game state.
   */
  @Override
  public String toString() {
    int size = this.marbleS.getBoardSize();
    StringBuilder sb = new StringBuilder();
    for (int r = 0; r < size; r++) {
      if (r > 0) {
        sb.append("\n");
      }
      sb.append(" ".repeat(size - r - 1));
      for (int c = 0; c <= r; c++) {
        if (c > 0) {
          sb.append(" ");
        }
        MarbleSolitaireModelState.SlotState marble = this.marbleS.getSlotAt(r, c);
        switch (marble) {
          case Empty:
            sb.append("_");
            break;
          case Marble:
            sb.append("O");
            break;

          // No action intended for this case
          default:
            break;
        }
      }
    }
    return sb.toString();
  }
}
