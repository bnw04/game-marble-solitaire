import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import game.marblesolitaire.controller.MarbleSolitaireController;
import game.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import game.marblesolitaire.model.single.EnglishSolitaireModel;
import game.marblesolitaire.model.single.MarbleSolitaireModel;
import game.marblesolitaire.view.MarbleSolitaireTextView;
import game.marblesolitaire.view.MarbleSolitaireView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the MarbleSolitaireControllerImpl class.
 */
public class MarbleSolitaireControllerImplTest {

  private MarbleSolitaireModel one;
  private Appendable gameLog;
  private MarbleSolitaireView view1;

  @Before
  public void setUp() {
    one = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    view1 = new MarbleSolitaireTextView(one, gameLog);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testViewNullModel() {
    new MarbleSolitaireTextView(null, gameLog);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testViewNullAppendable() {
    new MarbleSolitaireTextView(one, null);
  }

  @Test
  public void testViewRenderIOException() {
    Appendable log = new FailingAppendable();
    MarbleSolitaireView view = new MarbleSolitaireTextView(one, log);

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
      view1.renderMessage(null);
    } catch (IOException e) {
      assertEquals("Invalid message.", e.getMessage());
    }
  }

  @Test
  public void testViewRender() {
    try {
      view1.renderMessage("Game quit!\n");
    } catch (IOException e) {
      assertEquals("Fail!", e.getMessage());
    }
    assertEquals("Game quit!\n", gameLog.toString());
    try {
      view1.renderBoard();
    } catch (IOException e) {
      assertEquals("", e.getMessage());
    }
    assertEquals("Game quit!\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O", gameLog.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testControllerFailingAppendable() {
    Readable input = new StringReader("2 4 4 4 q");
    Appendable log = new FailingAppendable();
    MarbleSolitaireView view = new MarbleSolitaireTextView(one, log);
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view, input);
    c.playGame();
  }

  @Test
  public void testMockModelInputs() {
    StringBuilder log = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaireModel(log);
    MarbleSolitaireView view =
            new MarbleSolitaireTextView(mock, new StringBuilder());

    // quit first, no move
    Readable in = new StringReader("q 4 2 4 4");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(mock, view, in);
    c.playGame();
    assertEquals("", log.toString());

    // quit with Capital Q first, no move
    Readable inQ = new StringReader("Q 4 2 4 4");
    MarbleSolitaireController c2 =
            new MarbleSolitaireControllerImpl(mock, view, inQ);
    c2.playGame();
    assertEquals("", log.toString());

    // quit after move
    Readable in2 = new StringReader("4 2 4 4 q");
    MarbleSolitaireController c3 =
            new MarbleSolitaireControllerImpl(mock, view, in2);
    c3.playGame();
    assertEquals("(3, 1, 3, 3)\n", log.toString());

    // from Row non-integer garbage
    log.setLength(0);
    Readable in3 = new StringReader("&%2 2 4 4 4 q");
    MarbleSolitaireController c4 =
            new MarbleSolitaireControllerImpl(mock, view, in3);
    c4.playGame();
    assertEquals("(1, 3, 3, 3)\n", log.toString());

    // zero number and from Col non-integer garbage
    log.setLength(0);
    Readable in4 = new StringReader("0 &%^ 2 4 4 1 q");
    MarbleSolitaireController c5 =
            new MarbleSolitaireControllerImpl(mock, view, in4);
    c5.playGame();
    assertEquals("(1, 3, 3, 0)\n", log.toString());

    // to Row non-integer garbage
    log.setLength(0);
    Readable in5 = new StringReader("2 4 &%^ 2 4 q");
    MarbleSolitaireController c6 =
            new MarbleSolitaireControllerImpl(mock, view, in5);
    c6.playGame();
    assertEquals("(1, 3, 1, 3)\n", log.toString());

    // to Col non-integer garbage and negative number
    log.setLength(0);
    Readable in6 = new StringReader("5 4 4 4 &%^ -4 q");
    MarbleSolitaireController c7 =
            new MarbleSolitaireControllerImpl(mock, view, in6);
    c7.playGame();
    assertEquals("(4, 3, 3, 3)\n", log.toString());

    // extra non-integer garbage
    log.setLength(0);
    Readable in7 = new StringReader("2 4 4 4 m q");
    MarbleSolitaireController c8 =
            new MarbleSolitaireControllerImpl(mock, view, in7);
    c8.playGame();
    assertEquals("(1, 3, 3, 3)\n", log.toString());

    // quit before four ints
    log.setLength(0);
    Readable inq1 = new StringReader("2 q 4 4 4");
    MarbleSolitaireController c9 =
            new MarbleSolitaireControllerImpl(mock, view, inq1);
    c9.playGame();
    assertEquals("", log.toString());
    Readable inq2 = new StringReader("2 4 q 4 4");
    MarbleSolitaireController c10 =
            new MarbleSolitaireControllerImpl(mock, view, inq2);
    c10.playGame();
    assertEquals("", log.toString());
    Readable inq3 = new StringReader("2 4 4 q 4");
    MarbleSolitaireController c11 =
            new MarbleSolitaireControllerImpl(mock, view, inq3);
    c11.playGame();
    assertEquals("", log.toString());

    // new line instead of space and non-integer garbage in-between
    log.setLength(0);
    Readable in8 = new StringReader("2\n3\na\ne\n0\n\np\n4\n4\nq");
    MarbleSolitaireController c12 = new MarbleSolitaireControllerImpl(mock, view, in8);
    c12.playGame();
    assertEquals("(1, 2, 3, 3)\n", log.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testControllerNullM() {
    new MarbleSolitaireControllerImpl(null, view1, new StringReader("q"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testControllerNullV() {
    new MarbleSolitaireControllerImpl(one, null, new StringReader("q"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testControllerNullI() {
    new MarbleSolitaireControllerImpl(one, view1, null);
  }

  @Test
  public void testStarterOne() {
    Readable in = new StringReader("q");
    MarbleSolitaireModel model = new EnglishSolitaireModel(5);
    MarbleSolitaireView view = new MarbleSolitaireTextView(model, gameLog);
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(model, view, in);
    c.playGame();

    assertEquals("        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O _ O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "Score: 104\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O _ O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "Score: 104", gameLog.toString());
  }

  @Test
  public void testStarterTwo() {
    Readable in = new StringReader(" 12\nq");
    MarbleSolitaireModel model = new EnglishSolitaireModel(2, 3);
    MarbleSolitaireView view = new MarbleSolitaireTextView(model, gameLog);
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(model, view, in);
    c.playGame();
    assertEquals("    O O O\n" +
            "    O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32", gameLog.toString());
  }

  @Test
  public void testStarterMessageWithThree() {
    Readable in = new StringReader("q 23 0 -8");
    MarbleSolitaireModel model = new EnglishSolitaireModel(1, 0, 0);
    MarbleSolitaireView view = new MarbleSolitaireTextView(model, gameLog);
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(model, view, in);

    c.playGame();
    assertEquals("_\nScore: 0\n" +
            "Game quit!\n" +
            "State of game when quit:\n_\nScore: 0", gameLog.toString());
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayEmpty() {
    Readable rd = new StringReader("");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayNoNext() {
    Readable rd = new StringReader("2 4 4");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayNoNext2() {
    Readable rd = new StringReader("2 \n");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayOffBoardNoQ() {
    Readable rd = new StringReader("8 4 4 4");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayZeroNumberNoQ() {
    Readable rd = new StringReader("0 0 0 0");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayGarbageNoQuit() {
    Readable rd = new StringReader("^&% BBB 4.0 alie");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayGarbageNoQuit2() {
    Readable rd = new StringReader("^&% 4 4 alie");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayGarbageNoQuit3() {
    Readable rd = new StringReader("2 2\n3 1q\n");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayPossibleMoveNoQuit() {
    Readable rd = new StringReader("2 4 4 4");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test (expected = IllegalStateException.class)
  public void testPlayPossibleMovesNoQuit() {
    Readable rd = new StringReader("2 4 4 4 5 4 3 4");
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
  }

  @Test
  public void testQuitWithNoMove() {
    String noMoveQuitStr = "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32";

    // single q/Q with new line and spaces
    Readable rd1 = new StringReader("q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view1, rd1);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    Readable rd2 = new StringReader("\nQ                          ");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view2 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view2, rd2);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q with one int
    Readable rd3 = new StringReader("1 Q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view3 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view3, rd3);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q with two ints
    Readable rd4 = new StringReader("2 4 Q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view4 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view4, rd4);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q with three ints
    Readable rd5 = new StringReader("2 4 4 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view5 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view5, rd5);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q in-between valid move ints
    Readable rd6 = new StringReader("2 4 4 q 4");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view6 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view6, rd6);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q in front of valid move ints
    Readable rd7 = new StringReader("Q 2 4 4 4");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view7 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view7, rd7);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q in between valid move ints
    Readable rd8 = new StringReader("2 Q 4 4 4");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view8 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view8, rd8);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q in between valid move ints
    Readable rd9 = new StringReader("2 4 q 4 4");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view9 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view9, rd9);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q with non int garbage inputs
    Readable rd10 = new StringReader("0\n&*%$\n2*%\n*(^*BBB Q\n\n");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view10 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view10, rd10);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // fromRow garbage input
    Readable rd11 = new StringReader("abc 2 2 4 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view11 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view11, rd11);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // fromCol garbage input
    Readable rd12 = new StringReader("2 *)* 4 4\nq");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view12 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view12, rd12);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // toRow garbage input
    Readable rd13 = new StringReader("2 4 QQQ 4 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view13 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view13, rd13);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // toCol garbage input
    Readable rd14 = new StringReader("2 4 4 &*^^(*&qqQQ12 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view14 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view14, rd14);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q with following garbage
    Readable rd15 = new StringReader("q qqq       &*5t    *&* DGe37");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view15 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view15, rd15);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // q with in between four garbage
    Readable rd16 = new StringReader("qqq &*5t  \nq\n  *IWPEdak 37       ");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view16 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view16, rd16);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // all 0s with q
    Readable rd17 = new StringReader("0 0 0 0 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view17 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view17, rd17);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());

    // negative numbers with q
    Readable rd18 = new StringReader("-3 -1 1 2 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view18 =
            new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view18, rd18);
    c.playGame();
    assertEquals(noMoveQuitStr, gameLog.toString());
  }

  @Test
  public void testOnePlayQuit() {
    String onePlay = "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "    O _ O\n" +
            "    O O O\n" +
            "Score: 31\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "    O _ O\n" +
            "    O O O\n" +
            "Score: 31";

    // quit after one play
    Readable in = new StringReader("6 4 4 4 q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view1, in);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // toRow garbage input
    Readable in2 = new StringReader("6 4 ^7* 4 4 q");
    MarbleSolitaireModel m2 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view2 = new MarbleSolitaireTextView(m2, gameLog);
    c = new MarbleSolitaireControllerImpl(m2, view2, in2);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // fromRow garbage input
    Readable in3 = new StringReader("*&^B 6 4 4 4 Q");
    MarbleSolitaireModel m3 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view3 = new MarbleSolitaireTextView(m3, gameLog);
    c = new MarbleSolitaireControllerImpl(m3, view3, in3);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // one valid play with starting zeros invalid inputs
    Readable in4 = new StringReader("0 0 0 0 6 4 4 4 q");
    MarbleSolitaireModel m4 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view4 = new MarbleSolitaireTextView(m4, gameLog);
    c = new MarbleSolitaireControllerImpl(m4, view4, in4);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // one valid play with spaces and garbage input after quit
    Readable in5 = new StringReader("6 4 4 4                          " +
            "         q %^$ QQQ ddd *^&%12 ");
    MarbleSolitaireModel m5 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view5 = new MarbleSolitaireTextView(m5, gameLog);
    c = new MarbleSolitaireControllerImpl(m5, view5, in5);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // fromCol garbage input
    Readable in6 = new StringReader("6 DDD 4 4 4 q");
    MarbleSolitaireModel m6 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view6 = new MarbleSolitaireTextView(m6, gameLog);
    c = new MarbleSolitaireControllerImpl(m6, view6, in6);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // toCol garbage input
    Readable in7 = new StringReader("6 4 4 qqq 4 q");
    MarbleSolitaireModel m7 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view7 = new MarbleSolitaireTextView(m7, gameLog);
    c = new MarbleSolitaireControllerImpl(m7, view7, in7);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // garbage input in between ints and q
    Readable in8 = new StringReader("6 4 4 4 &6) -3 ccc QQQ q");
    MarbleSolitaireModel m8 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view8 = new MarbleSolitaireTextView(m8, gameLog);
    c = new MarbleSolitaireControllerImpl(m8, view8, in8);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // three extra ints
    Readable in9 = new StringReader("6 4 4 4 3 3 3 q");
    MarbleSolitaireModel m9 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view9 = new MarbleSolitaireTextView(m9, gameLog);
    c = new MarbleSolitaireControllerImpl(m9, view9, in9);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());

    // extra negative ints/double/non-int
    Readable in10 = new StringReader("6 4 4 4 0.02 -4 9&() q");
    MarbleSolitaireModel m10 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view10 = new MarbleSolitaireTextView(m10, gameLog);
    c = new MarbleSolitaireControllerImpl(m10, view10, in10);
    c.playGame();
    assertEquals(onePlay, gameLog.toString());
  }

  @Test
  public void testInGamePlays() {

    // in game consecutive plays
    // move-down play
    Readable in = new StringReader("QQQ 2 4 4 4 q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view1, in);
    c.playGame();
    assertEquals("    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 31\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 31", gameLog.toString());

    // move-up play, with non-int/zero/negative
    Readable in2 =
            new StringReader("5 qq QQ &^&% &*(^&%23 0 -5 4 3 4 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view2 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view2, in2);
    c.playGame();
    assertEquals("    O O O\n" +
            "    O _ O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 31\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O _ O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 30\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O _ O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 30", gameLog.toString());

    // move-right play
    Readable in3 = new StringReader("qq     5 0 2\n^&(OH\n5 *(^656Bu 4 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view3 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view3, in3);
    c.playGame();
    assertEquals("    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O _ O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 30\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O _ _ O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 29\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O _ _ O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 29", gameLog.toString());

    // move-left play, with garbage input and extra int
    Readable in4 =
            new StringReader("4\nqq\n6\n&*%\n4\n^&(OH\n4 *(^656Bu\n45\n23 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view4 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view4, in4);
    c.playGame();
    assertEquals("    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O _ _ O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 29\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O O _ _ O\n" +
            "O _ _ O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 28\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O O _ _ O\n" +
            "O _ _ O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 28", gameLog.toString());
  }

  @Test
  public void testQuitWithInvalidMove() {
    String invalidMoveQuitStr = "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "Invalid move. Play again.\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32";

    // off Board fromRow number
    Readable rd1 = new StringReader("8 4 4 4 q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view1, rd1);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // off Board toRow number
    Readable rd2 = new StringReader("2 4 8 4 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view, rd2);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // off Board fromCol number
    Readable rd3 = new StringReader("2\n8 4 4 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view3 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view3, rd3);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // off Board toCol number
    Readable rd4 = new StringReader("2\n4\n4 23 q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view4 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view4, rd4);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // invalid from position
    Readable rd5 = new StringReader("1\n1\n4 4\nQ");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view5 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view5, rd5);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // invalid to position
    Readable rd6 = new StringReader("\n2\n4 2 2\nQ");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view6 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view6, rd6);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // English model diagonally two places away
    Readable rd7 = new StringReader("\n2\n4 4 5\nQ");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view7 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view7, rd7);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // not two places away
    Readable rd8 = new StringReader("\n2\n4 5 4\nQ");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view8 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view8, rd8);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());

    // middle marble not empty
    Readable rd9 = new StringReader("\n3\n4 5 4\nQ");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view9 = new MarbleSolitaireTextView(one, gameLog);
    c = new MarbleSolitaireControllerImpl(one, view9, rd9);
    c.playGame();
    assertEquals(invalidMoveQuitStr, gameLog.toString());
  }

  @Test
  public void testQuitAfterTwoMoves() {
    Readable rd = new StringReader("2 4 4 4 5 4 3 4 q");
    MarbleSolitaireController c = new MarbleSolitaireControllerImpl(one, view1, rd);
    c.playGame();
    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O _ O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 30\n"
            + "Game quit!\n"
            + "State of game when quit:\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O _ O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 30", gameLog.toString());
  }

  @Test
  public void testDefaultGameOverPlay() {

    // game over board
    MarbleSolitaireModel over = new EnglishSolitaireModel(1);
    assertTrue(over.isGameOver());

    MarbleSolitaireTextView view = new MarbleSolitaireTextView(over, gameLog);
    String gameOverStr = "_\nScore: 0\n" +
            "Game over!\n_\nScore: 0";

    // on board move and quit when game is over
    Readable rd = new StringReader("1 1 1 1 q");
    MarbleSolitaireController controller =
            new MarbleSolitaireControllerImpl(over, view, rd);
    controller.playGame();
    assertEquals(gameOverStr, gameLog.toString());

    // on board move and no quit when game is over
    Readable rd2 = new StringReader("1 1 1 1");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view2 =
            new MarbleSolitaireTextView(over, gameLog);
    controller = new MarbleSolitaireControllerImpl(over, view2, rd2);
    controller.playGame();
    assertEquals(gameOverStr, gameLog.toString());

    // invalid non-integer garbage inputs and Q when game is over
    Readable rd3 = new StringReader("&Tw\n3yi\na\n4 Q");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view3 =
            new MarbleSolitaireTextView(over, gameLog);
    controller = new MarbleSolitaireControllerImpl(over, view3, rd3);
    controller.playGame();
    assertEquals(gameOverStr, gameLog.toString());

    // invalid non-integer garbage inputs and no Q when game is over
    Readable rd4 = new StringReader("&Tw\n3yi\na\n4");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view4 =
            new MarbleSolitaireTextView(over, gameLog);
    controller = new MarbleSolitaireControllerImpl(over, view4, rd4);
    controller.playGame();
    assertEquals(gameOverStr, gameLog.toString());

    // off board move and no quit when game is over
    Readable rd5 = new StringReader("1 -2 3 -5");
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view5 =
            new MarbleSolitaireTextView(over, gameLog);
    controller = new MarbleSolitaireControllerImpl(over, view5, rd5);
    controller.playGame();
    assertEquals(gameOverStr, gameLog.toString());
  }

  @Test
  public void testPlayToGameOver() {

    // play to game over with no q at the end
    Readable rd =
            new StringReader("2 4 4 4 5 4 3 4 7 4 5 4 4 6 4 4 4 3 4 5 4 1 4 3");
    MarbleSolitaireController controller =
            new MarbleSolitaireControllerImpl(one, view1, rd);
    controller.playGame();
    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 32\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 31\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O _ O O O\n"
            + "    O O O\n"
            + "    O O O\n"
            + "Score: 30\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O _ O\n"
            + "    O _ O\n"
            + "Score: 29\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O O O O O O O\n"
            + "    O _ O\n"
            + "    O _ O\n"
            + "Score: 28\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O _ _ O _ O\n"
            + "O O O O O O O\n"
            + "    O _ O\n"
            + "    O _ O\n"
            + "Score: 27\n"
            + "Game over!\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "_ _ O _ O _ O\n"
            + "O O O O O O O\n"
            + "    O _ O\n"
            + "    O _ O\n"
            + "Score: 26", gameLog.toString());

    // play to game over with q at the end
    Readable rdQ =
            new StringReader("2 4 4 4 5 4 3 4 7 4 5 4 " +
                    "4 6 4 4 4 3 4 5 4 1 4 3 Q");
    MarbleSolitaireModel m = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view2 =
            new MarbleSolitaireTextView(m, gameLog);
    controller = new MarbleSolitaireControllerImpl(m, view2, rdQ);
    controller.playGame();
    String[] lines = gameLog.toString().split("\n");
    assertEquals(57, lines.length);

    // check that the last 9 lines are correct
    String lastMsg = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 9, lines.length));
    assertEquals("Game over!\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "_ _ O _ O _ O\n"
            + "O O O O O O O\n"
            + "    O _ O\n"
            + "    O _ O\n"
            + "Score: 26", lastMsg);

    // play to game over with some non-integer garbage in
    Readable rdG =
            new StringReader("45% 2 4 4 4 5 %6\n4 3 4 7 4 &*(\n5 4 " +
                    "4 6 4 4 $3#) 4 3 4\n5 4 oi 1 4 - 3");
    MarbleSolitaireModel m2 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view3 =
            new MarbleSolitaireTextView(m2, gameLog);
    controller = new MarbleSolitaireControllerImpl(m2, view3, rdG);
    controller.playGame();
    lines = gameLog.toString().split("\n");
    assertEquals(57, lines.length);
    // check that the last 9 lines are correct
    lastMsg = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 9, lines.length));
    assertEquals("Game over!\n"
            + "    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "_ _ O _ O _ O\n"
            + "O O O O O O O\n"
            + "    O _ O\n"
            + "    O _ O\n"
            + "Score: 26", lastMsg);

    // play to game over with two invalid moves
    Readable rdInvalid  =
            new StringReader("2 4 x 4 4 " +
                    "6 1 a 0 2 3 " +
                    "5 4 3 4 " +
                    "45 2 3 3 " +
                    "7 4 5 4 4 6 4 4 4 3 4 5 4 1 4 3");
    MarbleSolitaireModel m3 = new EnglishSolitaireModel();
    gameLog = new StringBuilder();
    MarbleSolitaireTextView view4 =
            new MarbleSolitaireTextView(m3, gameLog);
    controller = new MarbleSolitaireControllerImpl(m3, view4, rdInvalid);
    controller.playGame();
    assertEquals("    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 31\n" +
            "Invalid move. Play again.\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 31\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O _ O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 30\n" +
            "Invalid move. Play again.\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O _ O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 30\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O _ O\n" +
            "    O _ O\n" +
            "Score: 29\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O O O _ _ O\n" +
            "O O O O O O O\n" +
            "    O _ O\n" +
            "    O _ O\n" +
            "Score: 28\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "O O _ _ O _ O\n" +
            "O O O O O O O\n" +
            "    O _ O\n" +
            "    O _ O\n" +
            "Score: 27\n" +
            "Game over!\n" +
            "    O O O\n" +
            "    O _ O\n" +
            "O O O O O O O\n" +
            "_ _ O _ O _ O\n" +
            "O O O O O O O\n" +
            "    O _ O\n" +
            "    O _ O\n" +
            "Score: 26", gameLog.toString());
  }
}
