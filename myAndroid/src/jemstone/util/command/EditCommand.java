package jemstone.util.command;

import java.io.PrintWriter;
import java.util.Date;

import jemstone.model.HasId;
import jemstone.util.Printer;

public abstract class EditCommand<T> implements UndoableCommand {
  private static final long serialVersionUID = 1L;

  protected final T entity;
  protected final int field;
  protected final Object oldValue;
  protected final Object newValue;

  public abstract Object getValue();
  public abstract void setValue(Object value);

  public EditCommand(T entity, int field, Object newValue) {
    this.entity = entity;
    this.field = field;
    this.newValue = newValue;
    this.oldValue = getValue();
  }

  @Override
  public void execute() {
    setValue(newValue);
  }

  @Override
  public void undo() {
    setValue(oldValue);
  }

  @Override
  public void redo() {
    setValue(newValue);
  }

  @Override
  public boolean canExecute() {
    return true;
  }

  @Override
  public boolean canUndo() {
    return true;
  }

  public int getInt() {
    return (Integer) getValue();
  }

  public double getDouble() {
    return (Double) getValue();
  }

  public Date getDate() {
    return (Date) getValue();
  }

  public String getString() {
    return (String) getValue();
  }

  @Override
  public void print(PrintWriter out, int depth) {
    Printer.indent(out, depth);
    int id = (entity instanceof HasId) ? ((HasId)entity).getId() : -1;

    out.format("%s: [oldValue=%s, newValue=%s, entity=%s(id=%d)]",
               getClass().getSimpleName(), oldValue, newValue,
               entity.getClass().getSimpleName(), id);
  }

  @Override
  public String toString() {
    return Printer.toString(this);
  }
}
