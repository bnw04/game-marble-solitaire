import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import game.marblesolitaire.controller.MarbleSolitaireController;
import game.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import game.marblesolitaire.model.single.MarbleSolitaireModel;
import game.marblesolitaire.model.withMultiple.EuropeanSolitaireModel;
import game.marblesolitaire.model.withMultiple.TriangleSolitaireModel;
import game.marblesolitaire.view.MarbleSolitaireTextView;
import game.marblesolitaire.view.MarbleSolitaireView;
import game.marblesolitaire.view.TriangleSolitaireTextView;

import static org.junit.Assert.assertEquals;

/**
 * Additional JUnit test class for the MarbleSolitaireControllerImpl class.
 */
public class ModelsControllerTest {
  private MarbleSolitaireModel one;
  private Appendable gameLogE;
  private MarbleSolitaireView view1;
  private MarbleSolitaireModel t;
  private Appendable gameLogT;
  private MarbleSolitaireView view2;

  @Before
  public void setUp() {
    one = new EuropeanSolitaireModel();
    gameLogE = new StringBuilder();
    view1 = new MarbleSolitaireTextView(one, gameLogE);

    t = new TriangleSolitaireModel();
    gameLogT = new StringBuilder();
    view2 = new TriangleSolitaireTextView(t, gameLogT);
  }

  @Test
  public void testViewRenderIOException() {
    Appendable log = new FailingAppendable();
    MarbleSolitaireView view = new TriangleSolitaireTextView(one, log);

    try {
      view.renderMessage("Fail");
    } catch (IOException e) {
      assertEquals("Fail!", e.getMessage());
    }
    try {
      view.renderBoard();
    } catch (IOException e) {
      assertEquals("Fail!", e.getMessage());
    }
  }

  @Test
  public void testRenderMsgNull() {
    try {
      view2.renderMessage(null);
    } catch (IOException e) {
      assertEquals("Invalid message.", e.getMessage());
    }
  }

  @Test
  public void testViewRender() {
    try {
      view2.renderMessage("Game quit!\n");
    } catch (IOException e) {
      assertEquals("Fail!", e.getMessage());
    }
    assertEquals("Game quit!\n", gameLogT.toString());
    try {
      view2.renderBoard();
    } catch (IOException e) {
      assertEquals("", e.getMessage());
    }
    assertEquals("Game quit!\n" +
            "    _\n" +
            "   O O\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O", gameLogT.toString());
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayOffBoardNoQ() {
    Readable rd = new StringReader("3 1 1 1");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(t, view2, rd);
    c.playGame();
  }

  @Test
  public void testEQuit() {
    Readable in = new StringReader("q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view1, in);
    c.playGame();
    assertEquals("    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O\n" +
            "Score: 36\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O\n" +
            "Score: 36", gameLogE.toString());
  }

  @Test
  public void testTQuit() {
    Readable in = new StringReader("1 2 1 Q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(t, view2, in);
    c.playGame();
    assertEquals("    _\n" +
            "   O O\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 14\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    _\n" +
            "   O O\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 14", gameLogT.toString());
  }

  @Test
  public void testEOneMoveQ() {
    Readable in = new StringReader("2 4 4 4 q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view1, in);
    c.playGame();
    assertEquals("    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O\n" +
            "Score: 36\n" +
            "    O O O\n" +
            "  O O _ O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O\n" +
            "Score: 35\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "  O O _ O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O\n" +
            "Score: 35", gameLogE.toString());
  }

  @Test
  public void testTOneMoveQ() {
    Readable in = new StringReader("%^ # 0 } 3 1 1 1 q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(t, view2, in);
    c.playGame();
    assertEquals("    _\n" +
            "   O O\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 14\n" +
            "    O\n" +
            "   _ O\n" +
            "  _ O O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 13\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O\n" +
            "   _ O\n" +
            "  _ O O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 13", gameLogT.toString());
  }

  @Test
  public void testTGameOver() {
    MarbleSolitaireModel game = new TriangleSolitaireModel(3);
    StringBuilder gameLog = new StringBuilder();
    MarbleSolitaireView view = new TriangleSolitaireTextView(game, gameLog);
    Readable in = new StringReader("3 1 1 1 as 3 3 %^*( 3 1 1 1 3 3 ");

    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(game, view, in);
    c.playGame();
    assertEquals("  _\n" +
            " O O\n" +
            "O O O\n" +
            "Score: 5\n" +
            "  O\n" +
            " _ O\n" +
            "_ O O\n" +
            "Score: 4\n" +
            "  O\n" +
            " _ O\n" +
            "O _ _\n" +
            "Score: 3\n" +
            "Game over!\n" +
            "  _\n" +
            " _ _\n" +
            "O _ O\n" +
            "Score: 2", gameLog.toString());
  }

  @Test
  public void testEGameOver() {
    MarbleSolitaireModel game = new EuropeanSolitaireModel(1);
    StringBuilder gameLog = new StringBuilder();
    MarbleSolitaireView view = new TriangleSolitaireTextView(game, gameLog);
    Readable in = new StringReader("3 1 1 1 q");

    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(game, view, in);
    c.playGame();
    assertEquals("_\n" +
            "Score: 0\n" +
            "Game over!\n" +
            "_\n" +
            "Score: 0", gameLog.toString());
  }
}
