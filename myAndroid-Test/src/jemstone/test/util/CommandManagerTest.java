package jemstone.test.util;

import java.io.PrintWriter;

import jemstone.util.command.CommandManager;
import jemstone.util.command.UndoableCommand;
import junit.framework.Assert;
import android.test.AndroidTestCase;

public class CommandManagerTest extends AndroidTestCase {
  private CommandManager manager;
  private final int maxSize = 16;

  @Override
  public void setUp() throws Exception {
    manager = new CommandManager("CommandManagerTest", maxSize);
  }

  public void testPush() {
    Assert.assertFalse("hasUndo", manager.hasUndo());
    Assert.assertFalse("hasRedo", manager.hasRedo());

    TestCommand command = new TestCommand();
    for (int i=0;  i < maxSize;  i++) {
      manager.execute(command);
    }
    Assert.assertEquals("Depth", maxSize, command.depth);
    Assert.assertTrue("hasUndo",  manager.hasUndo());
    Assert.assertFalse("hasRedo", manager.hasRedo());

    for (int i=0;  i < maxSize;  i++) {
      manager.undo();
    }
    Assert.assertEquals("Depth", 0, command.depth);

    Assert.assertFalse("hasUndo", manager.hasUndo());
    Assert.assertTrue("hasRedo", manager.hasRedo());
  }

  public void testUndo() {
    TestCommand command = new TestCommand();
    for (int i=0;  i < 10;  i++) {
      manager.execute(command);
    }

    for (int i=0;  i < 5;  i++) {
      manager.undo();
    }

    Assert.assertEquals("Depth", 5, command.depth);
  }

  public void testOverflow() {
    TestCommand command = new TestCommand();
    for (int i=0;  i < maxSize+2;  i++) {
      manager.execute(command);
    }
    Assert.assertEquals("Depth", maxSize+2, command.depth);
    Assert.assertEquals("Undo Buffer Overflow", maxSize, manager.undoSize());

    for (int i=0;  i < maxSize+2;  i++) {
      manager.undo();
    }
    Assert.assertEquals("Depth", 2, command.depth);
    Assert.assertEquals("Redo Buffer Overflow", maxSize, manager.redoSize());
  }

  public void testRedo() {
    TestCommand command = new TestCommand();
    for (int i=0;  i < 10;  i++) {
      manager.execute(command);
    }

    for (int i=0;  i < 5;  i++) {
      manager.undo();
    }
    manager.redo();

    Assert.assertEquals("Depth", 6, command.depth);
    Assert.assertTrue("hasUndo", manager.hasUndo());
    Assert.assertTrue("hasRedo", manager.hasRedo());
    Assert.assertEquals("hasRedo", 4, manager.redoSize());

    // Executing a command causes redo stack to empty
    manager.execute(command);
    Assert.assertFalse("hasRedo", manager.hasRedo());
  }

  public void testUndoRedo() {
    for (int i=0;  i < 5;  i++) {
      manager.execute(new TestCommand(i+1));
    }

    TestCommand undo1 = (TestCommand)manager.undo();
    Assert.assertEquals("Undo Command #1", 5, undo1.id);

    TestCommand undo2 = (TestCommand)manager.undo();
    Assert.assertEquals("Undo Command #2", 4, undo2.id);

    TestCommand redo1 = (TestCommand)manager.redo();
    Assert.assertEquals("Redo Command #1", 4, redo1.id);

    TestCommand redo2 = (TestCommand)manager.redo();
    Assert.assertEquals("Redo Command #2", 5, redo2.id);
    Assert.assertFalse("hasRedo", manager.hasRedo());

    TestCommand redo3 = (TestCommand)manager.redo();
    Assert.assertNull("Depth", redo3);
  }

  public void testCantUndo() {
    for (int i=0;  i < 5;  i++) {
      manager.execute(new TestCommand2(i+1));
    }

    TestCommand undo1 = (TestCommand)manager.undo();
    Assert.assertNull("Expecting Null", undo1);
  }

  private static class TestCommand implements UndoableCommand {
    private static final long serialVersionUID = 1L;

    private int id = -1;
    private int depth = 0;

    public TestCommand() {
    }

    public TestCommand(int id) {
      this.id = id;
    }

    @Override
    public void execute() {
      depth++;
    }

    @Override
    public void undo() {
      depth--;
    }

    @Override
    public void redo() {
      execute();
    }

    @Override
    public boolean canExecute() {
      return true;
    }

    @Override
    public boolean canUndo() {
      return true;
    }

    @Override
    public void print(PrintWriter out, int depth) {
    }
  }

  private static class TestCommand2 extends TestCommand {
    private static final long serialVersionUID = 1L;

    public TestCommand2(int id) {
      super(id);
    }

    @Override
    public boolean canUndo() {
      return false;
    }
  }
}
