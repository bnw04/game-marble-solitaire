import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import game.marblesolitaire.model.single.EnglishSolitaireModel;
import game.marblesolitaire.model.single.MarbleSolitaireModel;
import game.marblesolitaire.model.single.MarbleSolitaireModelState;
import game.marblesolitaire.view.MarbleSolitaireTextView;
import game.marblesolitaire.view.MarbleSolitaireView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the EnglishSolitaireModel class.
 */
public class EnglishSolitaireTest {
  private MarbleSolitaireModel one;
  private MarbleSolitaireView view1;
  private MarbleSolitaireModel two;
  private MarbleSolitaireView view2;
  private MarbleSolitaireModel three;
  private MarbleSolitaireView view3;
  private MarbleSolitaireModel four;
  private MarbleSolitaireView view4;

  @Before
  public void setUp() {
    one = new EnglishSolitaireModel();
    view1 = new MarbleSolitaireTextView(one);

    two = new EnglishSolitaireModel(0, 2);
    view2 = new MarbleSolitaireTextView(two);

    three = new EnglishSolitaireModel(1);
    view3 = new MarbleSolitaireTextView(three);

    four = new EnglishSolitaireModel(5, 12, 8);
    view4 = new MarbleSolitaireTextView(four);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testExceptionNull() {
    new MarbleSolitaireTextView(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRow() {
    new EnglishSolitaireModel(6,1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCol() {
    new EnglishSolitaireModel(0,7);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRow2() {
    new EnglishSolitaireModel(7,3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidArm() {
    new EnglishSolitaireModel(0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidArm2() {
    new EnglishSolitaireModel(2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidArm3() {
    new EnglishSolitaireModel(-5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidArm4() {
    new EnglishSolitaireModel(2, 0, 2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCol2() {
    new EnglishSolitaireModel(3, 3, 7);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCell() {
    new EnglishSolitaireModel(3, 0,1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCell2() {
    new EnglishSolitaireModel(7, 17,5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCell3() {
    new EnglishSolitaireModel(5, 0,13);
  }

  @Test
  public void testExceptionMsg() {
    String expectedMsg = "Invalid empty cell position (7, 3)";
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () ->
                    new EnglishSolitaireModel(7,3));
    assertEquals(expectedMsg, exception.getMessage());
  }

  @Test
  public void testView() {
    MarbleSolitaireModelState armOne = new EnglishSolitaireModel(1, 0,0);
    MarbleSolitaireView view = new MarbleSolitaireTextView(armOne);
    assertEquals("_", view.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            armOne.getSlotAt(0,0));

    MarbleSolitaireModelState armFive = new EnglishSolitaireModel(5);
    MarbleSolitaireView view5 = new MarbleSolitaireTextView(armFive);
    assertEquals("        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O _ O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O", view5.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            armFive.getSlotAt(6,6));

    assertEquals("    O O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O", view1.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,3));

    assertEquals(""
            + "    _ O O\n"
            + "    O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O", view2.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            two.getSlotAt(0,2));

    assertEquals("_", view3.toString());

    assertEquals("        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "O O O O O O O O O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O O\n"
            + "        O O O O _", view4.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            four.getSlotAt(12,8));
  }

  @Test
  public void testSize() {
    assertEquals(7, one.getBoardSize());
    assertEquals(7, two.getBoardSize());
    assertEquals(1, three.getBoardSize());
    assertEquals(13, four.getBoardSize());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSlotException1() {
    one.getSlotAt(-1, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSlotException2() {
    three.getSlotAt(0, 1);
  }

  @Test
  public void testSlot() {
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(0,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(0,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(6,6));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(6,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(3,2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(2,0));
  }

  @Test
  public void testScore() {
    assertEquals(32, one.getScore());
    assertEquals(32, two.getScore());
    assertEquals(0, three.getScore());
    assertEquals(104, four.getScore());
  }

  // move from empty
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException1() {
    one.move(3,3,3,5);
  }

  // move to not two places away
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException21() {
    one.move(1,3,4,3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveException22() {
    one.move(1,3,1,3);
  }

  // move to diagonally two places away
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException23() {
    one.move(1,3,3,5);
  }

  // move middle marble is not empty
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException3() {
    one.move(2,3,4,3);
  }

  // move to marble is not empty
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException4() {
    one.move(2,2,2,4);
  }

  // move to out of range
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException5() {
    two.move(2,5,2,7);
  }

  // move from out of range
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException6() {
    two.move(2,7,2,5);
  }

  // move from invalid
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException7() {
    four.move(0,3,0,5);
  }

  // move to invalid
  @Test (expected = IllegalArgumentException.class)
  public void testMoveException8() {
    four.move(0,5,0,3);
  }

  @Test
  public void testMoveOver() {

    // move down
    one.move(1, 3, 3,3);
    assertEquals("    O O O\n"
            + "    O _ O\n"
            + "O O O _ O O O\n"
            + "O O O O O O O\n"
            + "O O O O O O O\n"
            + "    O O O\n"
            + "    O O O", view1.toString());
    assertFalse(one.isGameOver());

    // move up
    one.move(4,3,2,3);
    assertEquals("    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O O O _ O O O\n"
            + "    O O O\n"
            + "    O O O", view1.toString());

    // move right
    assertEquals(30, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(4,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(4,2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(4,3));
    one.move(4,1,4,3);
    assertEquals("    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O _ O O O\n"
            + "O _ _ O O O O\n"
            + "    O O O\n"
            + "    O O O", view1.toString());
    assertEquals(29, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(4,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(4,2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(4,3));

    // move left
    one.move(3,5,3,3);
    assertEquals("    O O O\n"
            + "    O _ O\n"
            + "O O O O O O O\n"
            + "O O O O _ _ O\n"
            + "O _ _ O O O O\n"
            + "    O O O\n"
            + "    O O O", view1.toString());
    assertFalse(one.isGameOver());

    one.move(3,2,3,4);
    assertEquals(27, one.getScore());
    one.move(3,0,3,2);
    assertEquals(26, one.getScore());
    one.move(2,2,4,2);
    assertEquals(25, one.getScore());
    one.move(0,2,2,2);
    assertEquals(24, one.getScore());
    one.move(4,3,4,1);
    assertEquals(23, one.getScore());
    one.move(6,2,4,2);
    assertEquals(22, one.getScore());
    one.move(6,3,4,3);
    assertEquals(21, one.getScore());
    one.move(0,4,0,2);
    assertEquals(20, one.getScore());
    one.move(2,4,0,4);
    assertEquals(19, one.getScore());

    // game not over yet
    one.move(4,4,2,4);
    assertEquals(18, one.getScore());
    assertFalse(one.isGameOver());

    // game over with marbles
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(6,4));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(5,4));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(4,4));
    one.move(6,4,4,4);
    assertEquals(17, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(6,4));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(5,4));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(4,4));
    assertTrue(one.isGameOver());

    // after game is over, all moves will be invalid
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      int fromRow = random.nextInt(6);
      int fromCol = random.nextInt(6);
      int toRow = random.nextInt(6);
      int toCol = random.nextInt(6);
      assertThrows(IllegalArgumentException.class, () ->
              one.move(fromRow, fromCol, toRow, toCol)
      );
    }

    // all empty game over
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            three.getSlotAt(0, 0));
    assertEquals(0, three.getScore());
    assertTrue(three.isGameOver());
  }
}
