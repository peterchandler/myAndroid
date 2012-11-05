package jemstone.ui;


public interface MenuItemHandler {
  public boolean canUndo();
  public boolean canRedo();
  public boolean canAdd();
  public boolean canDelete();
  public boolean canAccept();
  public boolean canCancel();
  public boolean canConfig();
  
  public boolean dispatch(int id);
  
  public void onUndo();
  public void onRedo();
  public void onAdd();
  public void onDelete();
  public void onAccept();
  public void onCancel();
}