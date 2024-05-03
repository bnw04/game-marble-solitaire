import game.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import game.marblesolitaire.model.single.EnglishSolitaireModel;
import game.marblesolitaire.model.single.MarbleSolitaireModel;
import game.marblesolitaire.view.MarbleSolitaireTextView;

/**
 * Do not modify this file. This file should compile correctly with your code!
 */
public class Hw06TypeChecks {

  /**
   * A sample main method.
   *
   * @param args the program arguments
   */
  public static void main(String[] args) {
    Readable rd = null;
    Appendable ap = null;
    helper(new EnglishSolitaireModel(),
            rd, ap);
    helper(new EnglishSolitaireModel(3, 3), rd, ap);
  }

  private static void helper(MarbleSolitaireModel model,
                             Readable rd, Appendable ap) {
    new MarbleSolitaireControllerImpl(model,
            new MarbleSolitaireTextView(model,ap),rd);
  }

}
