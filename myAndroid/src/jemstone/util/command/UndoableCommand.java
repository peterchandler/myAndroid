package jemstone.util.command;

import java.io.Serializable;

import jemstone.util.Printable;


/**
 * An undoable command intended to be executed by the {@link CommandManager}.
 */
public interface UndoableCommand extends Serializable, Printable {
  /**
   * Called by the {@link CommandManager} to execute the command
   */
  public void execute();

  /**
   * Called by the {@link CommandManager} to undo the command
   */
  public void undo();

  /**
   * Called by the {@link CommandManager} to redo the command
   */
  public void redo();
  
  /**
   * Called by the {@link CommandManager} to determine whether the command can be executed,
   * or whether it is a null command.
   */
  public boolean canExecute();

  /**
   * Called by the {@link CommandManager} to determine whether the command can be undone,
   * and hence whether it should be added to the undo stack.
   * <p>
   * Implementations can use this to indicate whether specific a instance of this command
   * are undoable (i.e. it is context sensitive), and not just applicable to all instances
   * of this class are.
   *
   * @return <code>true</code> if the command can be undone
   */
  public boolean canUndo();
}
