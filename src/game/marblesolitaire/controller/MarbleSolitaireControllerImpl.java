package game.marblesolitaire.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import game.marblesolitaire.model.single.MarbleSolitaireModel;
import game.marblesolitaire.view.MarbleSolitaireView;

/**
 * This class represents the controller of solitaire model game and
 * implementation of the MarbleSolitaireController interface. It offers all
 * the operations mandated by the MarbleSolitaireController interface.
 */
public class MarbleSolitaireControllerImpl implements MarbleSolitaireController {
  private final MarbleSolitaireModel m;
  private final MarbleSolitaireView view;
  private final Readable in;

  /**
   * Constructs an instance of a controller for the marble solitaire game.
   * @param m the model of the solitaire game.
   * @param view the view of the model of the game.
   * @param in the input coming from the user.
   * @throws IllegalArgumentException if any of the model, view or input is null.
   */
  public MarbleSolitaireControllerImpl(MarbleSolitaireModel m,
                                       MarbleSolitaireView view, Readable in) {
    if (m == null || view == null || in == null) {
      throw new IllegalArgumentException("Invalid Controller.");
    }
    this.m = m;
    this.view = view;
    this.in = in;
  }

  /** Sets the game in motion for the user to play the game.
   *
   * @throws IllegalStateException If the Appendable object is unable to
   *     transmit output or the Readable object is unable to provide inputs
   *     or the user doesn't quit game and the game is not over
   */
  @Override
  public void playGame() throws IllegalStateException {
    boardHelper();
    presentHelper("\nScore: " + this.m.getScore() + "\n");

    Scanner scan = new Scanner(this.in);
    ArrayList<Integer> pos = new ArrayList<>();

    while (scan.hasNext()) {
      String input = scan.next();

      // see if quitting game
      if (input.equalsIgnoreCase("q")) {
        presentHelper("Game quit!\nState of game when quit:\n");
        boardHelper();
        presentHelper("\nScore: " + this.m.getScore());
        return;
      }

      // see if game is over
      if (this.m.isGameOver()) {
        break;
      }

      // see if any valid move
      if (isInteger(input) && Integer.parseInt(input) > 0) {
        pos.add(Integer.parseInt(input));
        if (pos.size() == 4) {
          moveHelper(pos.get(0), pos.get(1), pos.get(2), pos.get(3));
          pos.clear();
          if (this.m.isGameOver()) {
            break;
          }
          boardHelper();
          presentHelper("\nScore: " + this.m.getScore() + "\n");
        }
      }
    }

    // see if game is over, otherwise throw exception
    if (this.m.isGameOver()) {
      presentHelper("Game over!\n");
      boardHelper();
      presentHelper("\nScore: " + this.m.getScore());
    } else {
      throw new IllegalStateException("No more input.");
    }
  }

  private void moveHelper(int fromR, int fromC, int toR, int toC) {
    try {
      this.m.move(fromR - 1, fromC - 1,
              toR - 1, toC - 1);
    } catch (IllegalArgumentException e) {
      presentHelper("Invalid move. Play again.\n");
    }
  }

  private void presentHelper(String str) throws IllegalStateException {
    try {
      this.view.renderMessage(str);
    } catch (IOException e) {
      throw new IllegalStateException("Fail to append message.");
    }
  }

  private void boardHelper() throws IllegalStateException {
    try {
      this.view.renderBoard();
    } catch (IOException e) {
      throw new IllegalStateException("Fail to append board.");
    }
  }

  private boolean isInteger(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
