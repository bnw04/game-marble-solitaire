import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import game.marblesolitaire.model.single.MarbleSolitaireModel;
import game.marblesolitaire.model.single.MarbleSolitaireModelState;
import game.marblesolitaire.model.withMultiple.TriangleSolitaireModel;
import game.marblesolitaire.view.TriangleSolitaireTextView;
import game.marblesolitaire.view.MarbleSolitaireView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the TriangleSolitaireModel class.
 */
public class TriangleSolitaireTest {
  private MarbleSolitaireModel one;
  private MarbleSolitaireView view1;
  private MarbleSolitaireModel two;
  private MarbleSolitaireView view2;
  private MarbleSolitaireModel three;
  private MarbleSolitaireView view3;
  private MarbleSolitaireModel armSix;
  private MarbleSolitaireView view4;

  @Before
  public void setUp() {
    one = new TriangleSolitaireModel();
    view1 = new TriangleSolitaireTextView(one);

    two = new TriangleSolitaireModel(2, 2);
    view2 = new TriangleSolitaireTextView(two, System.out);

    three = new TriangleSolitaireModel(1);
    view3 = new TriangleSolitaireTextView(three);

    armSix = new TriangleSolitaireModel(6, 5, 0);
    view4 = new TriangleSolitaireTextView(armSix);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testViewExceptionNull() {
    new TriangleSolitaireTextView(null, System.out);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testViewOutNull() {
    new TriangleSolitaireTextView(one, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidStartEmpty() {
    new TriangleSolitaireModel(1,2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testOutStartEmpty() {
    new TriangleSolitaireModel(0,5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testOutStartEmpty2() {
    new TriangleSolitaireModel(5,0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidArm() {
    new TriangleSolitaireModel(0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidArm2() {
    new TriangleSolitaireModel(-1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidArm3() {
    new TriangleSolitaireModel(0, 0, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testOutStartRow() {
    new TriangleSolitaireModel(3, 3, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testOutStartCol() {
    new TriangleSolitaireModel(3, 0, 3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCell() {
    new TriangleSolitaireModel(2, 0,1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidCell2() {
    new TriangleSolitaireModel(6, 4,5);
  }

  @Test
  public void testExceptionMsg() {
    String expectedMsg = "Invalid empty cell position (1, 2)";
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () ->
                    new TriangleSolitaireModel(1,2));
    assertEquals(expectedMsg, exception.getMessage());
  }

  @Test
  public void testView() {
    MarbleSolitaireModelState armOne = new TriangleSolitaireModel(1, 0,0);
    MarbleSolitaireView view = new TriangleSolitaireTextView(armOne);
    assertEquals("_", view.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            armOne.getSlotAt(0,0));

    assertEquals("    _\n" +
            "   O O\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O", view1.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(0,0));

    assertEquals("    O\n" +
            "   O O\n" +
            "  O O _\n" +
            " O O O O\n" +
            "O O O O O", view2.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            two.getSlotAt(2,2));

    assertEquals("_", view3.toString());

    assertEquals("     O\n" +
            "    O O\n" +
            "   O O O\n" +
            "  O O O O\n" +
            " O O O O O\n" +
            "_ O O O O O", view4.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            armSix.getSlotAt(5,0));
  }

  @Test
  public void testSize() {
    assertEquals(5, one.getBoardSize());
    assertEquals(5, two.getBoardSize());
    assertEquals(1, three.getBoardSize());
    assertEquals(6, armSix.getBoardSize());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSlotOutOfBound() {
    one.getSlotAt(-1, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSlotOutOfBound2() {
    one.getSlotAt(0, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSlotOutOfBound3() {
    one.getSlotAt(5, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSlotOutOfBound4() {
    three.getSlotAt(0, 1);
  }

  @Test
  public void testSlot() {
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(0,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(0,4));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(1,2));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(2,3));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid,
            one.getSlotAt(3,4));

    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(0,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            two.getSlotAt(2,2));

    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(1,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(1,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(4,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(4,4));
  }

  @Test
  public void testScore() {
    assertEquals(14, one.getScore());
    assertEquals(14, two.getScore());
    assertEquals(0, three.getScore());
    assertEquals(20, armSix.getScore());
  }

  // move from empty
  @Test (expected = IllegalArgumentException.class)
  public void testMoveFromEmpty() {
    one.move(0,0,2,0);
  }

  // move to not two places away, diagonally/vertically/horizontally
  @Test (expected = IllegalArgumentException.class)
  public void testMoveNotTwoPlace() {
    one.move(3,0,0,0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveNotTwoPlace2() {
    one.move(0,3,0,0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveNotTwoPlace3() {
    one.move(3,3,0,0);
  }

  // move to down-left or up-right diagonally two places away
  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidDiagonal() {
    one.move(4,0,2,2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidDiagonal2() {
    armSix.move(3,2,5,0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveToSelf() {
    one.move(1,1,1,1);
  }

  // move middle marble is not empty
  @Test (expected = IllegalArgumentException.class)
  public void testMoveMidNotEmpty() {
    one.move(2,3,4,3);
  }

  // move to marble is not empty
  @Test (expected = IllegalArgumentException.class)
  public void testMoveToNotEmpty() {
    one.move(2,2,2,4);
  }

  // move to out of range
  @Test (expected = IllegalArgumentException.class)
  public void testMoveToOutOfBound() {
    two.move(2,5,2,7);
  }

  // move from out of range
  @Test (expected = IllegalArgumentException.class)
  public void testMoveFromOutOfBound() {
    two.move(2,7,2,5);
  }

  // move from invalid
  @Test (expected = IllegalArgumentException.class)
  public void testMoveFromInvalid() {
    armSix.move(0,6,0,0);
  }

  // move to invalid
  @Test (expected = IllegalArgumentException.class)
  public void testMoveToInvalid() {
    armSix.move(1,1,6,0);
  }

  @Test
  public void testMoveOver() {

    // valid diagonal up-right move
    one.move(2, 0, 0, 0);
    assertEquals("    O\n" +
            "   _ O\n" +
            "  _ O O\n" +
            " O O O O\n" +
            "O O O O O", view1.toString());
    assertEquals(13, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(2,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(1,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(0,0));

    // valid diagonal up-left move
    one.move(3,2,1,0);
    assertEquals("    O\n" +
            "   O O\n" +
            "  _ _ O\n" +
            " O O _ O\n" +
            "O O O O O", view1.toString());
    assertEquals(12, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(2,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(1,0));

    // horizontal right move
    one.move(3,0,3,2);
    assertEquals("    O\n" +
            "   O O\n" +
            "  _ _ O\n" +
            " _ _ O O\n" +
            "O O O O O", view1.toString());
    assertEquals(11, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(3,2));

    // horizontal left move
    one.move(3,3,3,1);
    assertEquals("    O\n" +
            "   O O\n" +
            "  _ _ O\n" +
            " _ O _ _\n" +
            "O O O O O", view1.toString());
    assertEquals(10, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(3,2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(3,1));

    // valid diagonal down left move
    one.move(0,0,2,0);
    assertEquals("    _\n" +
            "   _ O\n" +
            "  O _ O\n" +
            " _ O _ _\n" +
            "O O O O O", view1.toString());
    assertEquals(9, one.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(0,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            one.getSlotAt(1,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            one.getSlotAt(2,0));
    assertFalse(one.isGameOver());

    // valid diagonal down right move
    two.move(0,0,2,2);
    assertEquals("    _\n" +
            "   O _\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O", view2.toString());
    assertEquals(13, two.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            two.getSlotAt(0,0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            two.getSlotAt(1,1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble,
            two.getSlotAt(2,2));
    assertFalse(two.isGameOver());
  }

  @Test
  public void testIsGameOver() {
    MarbleSolitaireModel game = new TriangleSolitaireModel(3);
    MarbleSolitaireView view = new TriangleSolitaireTextView(game);
    assertEquals("  _\n" +
            " O O\n" +
            "O O O", view.toString());
    game.move(2, 0, 0, 0);
    game.move(2, 2, 2, 0);

    assertFalse(game.isGameOver());
    game.move(0, 0, 2, 2);
    assertEquals("  _\n" +
            " _ _\n" +
            "O _ O", view.toString());
    assertEquals(2, game.getScore());
    assertTrue(game.isGameOver());

    // after game is over, all moves will be invalid
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      int fromRow = random.nextInt(6);
      int fromCol = random.nextInt(6);
      int toRow = random.nextInt(6);
      int toCol = random.nextInt(6);
      assertThrows(IllegalArgumentException.class, () ->
              game.move(fromRow, fromCol, toRow, toCol)
      );
    }
  }

  @Test
  public void testDefaultOverGame() {
    // all empty game over
    assertEquals(MarbleSolitaireModelState.SlotState.Empty,
            three.getSlotAt(0, 0));
    assertEquals(0, three.getScore());
    assertTrue(three.isGameOver());

    // size 2 game is default over game
    MarbleSolitaireModel game = new TriangleSolitaireModel(2, 1, 1);
    MarbleSolitaireView view = new TriangleSolitaireTextView(game);
    assertEquals(" O\n" +
            "O _", view.toString());
    assertEquals(2, game.getScore());
    assertTrue(game.isGameOver());
  }
}
