package game.marblesolitaire.controller;

/**
 * To represent controllers for the marble solitaire game.
 */
public interface MarbleSolitaireController {

  /** Sets the game in motion for the user to play the game.
   *
   * @throws IllegalStateException If the Appendable object is unable to
   *     transmit output or the Readable object is unable to provide inputs
   *     or the user doesn't quit game and the game is not over
   */
  void playGame();
}
