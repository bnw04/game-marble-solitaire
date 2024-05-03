package game.marblesolitaire;

import java.io.InputStreamReader;
import game.marblesolitaire.controller.MarbleSolitaireController;
import game.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import game.marblesolitaire.model.single.EnglishSolitaireModel;
import game.marblesolitaire.model.single.MarbleSolitaireModel;
import game.marblesolitaire.model.withMultiple.EuropeanSolitaireModel;
import game.marblesolitaire.model.withMultiple.TriangleSolitaireModel;
import game.marblesolitaire.view.MarbleSolitaireTextView;
import game.marblesolitaire.view.MarbleSolitaireView;
import game.marblesolitaire.view.TriangleSolitaireTextView;

/**
 * Main class for Marble Solitaire to run a game.
 */
public final class MarbleSolitaire {

  /**
   * Creates a model, view, controller for the marble solitaire game and runs the game.
   * @param args by convention.
   */
  public static void main(String[] args) {
    String type = args[0];
    MarbleSolitaireModel model = null;
    MarbleSolitaireView view = null;
    IParser p = null;
    try {
      switch (type) {
        case "english":
          p = new Parser(args, 3, 3, 3);
          model = new EnglishSolitaireModel(p.getArm(), p.getSRow(), p.getSCol());
          view = new MarbleSolitaireTextView(model);
          break;
        case "european":
          p = new Parser(args, 3, 3, 3);
          model = new EuropeanSolitaireModel(p.getArm(), p.getSRow(), p.getSCol());
          view = new MarbleSolitaireTextView(model);
          break;
        case "triangular":
          p = new Parser(args, 5, 0, 0);
          model = new TriangleSolitaireModel(p.getArm(), p.getSRow(), p.getSCol());
          view = new TriangleSolitaireTextView(model);
          break;
        default:
          break;
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid model" + e.getMessage());
    }
    try {
      MarbleSolitaireController c = new MarbleSolitaireControllerImpl(model, view,
              new InputStreamReader(System.in));
      c.playGame();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }
}
