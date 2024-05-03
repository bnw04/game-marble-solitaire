package game.marblesolitaire.view;

import java.io.IOException;
import game.marblesolitaire.model.single.MarbleSolitaireModelState;

/**
 * Abstract Class representing an abstract marble solitaire model view.
 */
public abstract class AbstractSolitaireView implements MarbleSolitaireView {
  protected MarbleSolitaireModelState marbleS;
  protected Appendable out;

  /**
   * Constructs a MarbleSolitaireTextView object with the provided
   * MarbleSolitaireModelState and Appendable.
   *
   * @param s The MarbleSolitaireModelState object representing the game.
   * @param out The Appendable object representing the state and any message
   *            of the game.
   * @throws IllegalArgumentException if the provided model is null.
   */
  public AbstractSolitaireView(MarbleSolitaireModelState s, Appendable out) {
    if (s == null || out == null) {
      throw new IllegalArgumentException("Null Solitaire Model or Appendable.");
    }
    this.marbleS = s;
    this.out = out;
  }

  /**
   * Render the board to the provided data destination. The board should be rendered exactly
   * in the format produced by the toString method above
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderBoard() throws IOException {
    this.out.append(toString());
  }

  /**
   * Render a specific message to the provided data destination.
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   *     and the message is null.
   */
  @Override
  public void renderMessage(String message) throws IOException {
    if (message == null) {
      throw new IOException("Invalid message.");
    }
    this.out.append(message);
  }
}
