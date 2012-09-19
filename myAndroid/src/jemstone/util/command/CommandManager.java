package jemstone.util.command;

import java.io.Serializable;

import jemstone.util.Stack;
import jemstone.util.log.Logger;

/**
 * Command manager supports undo/redo stack for commands.
 * <p>
 * Note that we use a {@link Stack} for GWT client compatibility.
 */
public class CommandManager implements Serializable {
  private static final long serialVersionUID = 1L;
  private static Logger log = Logger.getLogger(CommandManager.class);

  private final String name;
  private final Stack<UndoableCommand> undoStack;
  private final Stack<UndoableCommand> redoStack;

  /**
   * Create a new command manager with a maximum capacity of 128 commands
   */
  public CommandManager(String name) {
    this(name, 128);
  }

  /**
   * Create a new command manager with the specified maximum capacity
   * @param maxSize
   */
  public CommandManager(String name, int maxSize) {
    this.name = name;
    this.undoStack = new Stack<UndoableCommand>(maxSize);
    this.redoStack = new Stack<UndoableCommand>(maxSize);
  }

  /**
   * Execute the specified command, push it onto the undo stack, and clear the redo stack
   * @param command
   */
  public void execute(UndoableCommand command) {
    if (!command.canExecute()) {
      log.debug("%s.execute(): Cannot execute %s", name, command);
      return;
    }
    
    // Execute the command
    command.execute();

    // Push executed command onto undo stack
    if (command.canUndo()) {
      undoStack.push(command);
    }
    redoStack.clear();

    // And log it
    log.debug("%s.execute(): %s", name, command);
  }

  /**
   * Pop first item off undo stack and call {@link UndoableCommand#undo()}. The command
   * is then pushed onto the redo stack.
   * @return The command that was undone, or <code>null</code> if undo stack was empty
   */
  public UndoableCommand undo() {
    if (hasUndo()) {
      // Undo the command
      UndoableCommand command = undoStack.pop();
      command.undo();

      // Push undone command onto redo stack
      redoStack.push(command);

      // And log it
      log.debug("%s.undo(): %s", name, command);

      // Return the command that was undone
      return command;
    }

    return null;
  }

  /**
   * Pop first item off redo stack and call {@link #execute(UndoableCommand)}
   * @return The command that was redone, or <code>null</code> if redo stack was empty
   */
  public UndoableCommand redo() {
    if (hasRedo()) {
      // Redo the command
      UndoableCommand command = redoStack.pop();
      command.redo();

      // Push redone command onto undo stack
      if (command.canUndo()) {
        undoStack.push(command);
      }

      // And log it
      log.debug("%s.redo(): %s", name, command);

      return command;
    }
    return null;
  }

  /**
   * Clear the undo and redo stacks
   */
  public void clear() {
    undoStack.clear();
    redoStack.clear();
  }

  /**
   * @return <code>true</code> if the undo stack is not empty
   */
  public boolean hasUndo() {
    return !undoStack.isEmpty();
  }

  /**
   * @return <code>true</code> if the redo stack is not empty
   */
  public boolean hasRedo() {
    return !redoStack.isEmpty();
  }

  /**
   * @return The number of commands in the undo stack (used only for testing)
   */
  public int undoSize() {
    return undoStack.size();
  }

  /**
   * @return The number of commands in the redo stack (used only for testing)
   */
  public int redoSize() {
    return redoStack.size();
  }
}
