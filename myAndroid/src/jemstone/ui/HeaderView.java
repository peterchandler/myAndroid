package jemstone.ui;

import jemstone.model.HasName;
import jemstone.myandroid.R;
import jemstone.util.log.Logger;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeaderView extends RelativeLayout implements OnClickListener {
  private static final Logger log = Logger.getLogger(HeaderView.class);

  private final Context context;

  /** The layout view */
  private View layout;

  /** The title text */
  private TextView title;
  private TextView subtitle;

  /** Optional buttons */
  private View back;
  @SuppressWarnings("unused")
  private View home;
  private View undo;
  private View redo;
  private View add;
  private View delete;
  private View accept;
  private View cancel;
  private View config;

  private CommandButtonHandler handler;

  public HeaderView(Context context) {
    super(context);
    this.context = context;
    initView();
  }

  public HeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    initView();
  }

  public HeaderView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;
    initView();
  }

  protected void initView() {
    log.debug("Inflating header_layout for %s", context.getClass().getSimpleName());

    this.layout = LayoutInflater.from(context).inflate(R.layout.header_layout, this);

    this.title = (TextView) layout.findViewById(R.id.title);
    this.subtitle = (TextView) layout.findViewById(R.id.subtitle);

    this.back = layout.findViewById(R.id.back);
    this.home = initButton(layout, R.id.home);
    this.undo = initButton(layout, R.id.undo);
    this.redo = initButton(layout, R.id.redo);
    this.add = initButton(layout, R.id.add);
    this.delete = initButton(layout, R.id.delete);
    this.accept = initButton(layout, R.id.accept);
    this.cancel = initButton(layout, R.id.cancel);
    this.config = initButton(layout, R.id.config);

    setButtonState();
  }

  private View initButton(View view, int id) {
    View button = view.findViewById(id);
    if (button != null) {
      button.setOnClickListener(this);
    }
    return button;
  }

  @Override
  public void onClick(View view) {
    // Propagate the click event
    if (handler != null) {
      handler.onClick(view);
    }

    // Refresh the button state
    setButtonState();
  }

  public void setButtonState() {
    back.setVisibility(handler != null && handler.canHome() ? View.VISIBLE : View.GONE);
    undo.setVisibility(handler != null && handler.canUndo() ? View.VISIBLE : View.GONE);
    redo.setVisibility(handler != null && handler.canRedo() ? View.VISIBLE : View.GONE);
    add.setVisibility (handler != null && handler.canAdd()  ? View.VISIBLE : View.GONE);
    delete.setVisibility(handler != null && handler.canDelete() ? View.VISIBLE : View.GONE);
    accept.setVisibility(handler != null && handler.canAccept() ? View.VISIBLE : View.GONE);
    cancel.setVisibility(handler != null && handler.canCancel() ? View.VISIBLE : View.GONE);
    config.setVisibility(handler != null && handler.canConfig() ? View.VISIBLE : View.GONE);
  }

  public CommandButtonHandler getCommandButtonHandler() {
    return handler;
  }

  public void setCommandButtonHandler(CommandButtonHandler handler) {
    this.handler = handler;
  }
  
  public String getTitle() {
    return title.getText().toString();
  }

  public void setTitle(CharSequence text) {
    title.setText(text);
  }

  public void setTitle(int stringId) {
    String text = context.getString(stringId);
    setTitle(text);
  }

  public void setTitle(int stringId, HasName entity) {
    String text = context.getString(stringId, entity.getName());
    setTitle(text);
  }

  public void setTitle(int stringId, Object ... args) {
    String text = context.getString(stringId, args);
    setTitle(text);
  }

  public void setSubTitle(String text) {
    subtitle.setText(text);
    subtitle.setVisibility(VISIBLE);
  }

  public void setSubTitle(int stringId) {
    setSubTitle(context.getString(stringId));
  }
  
  public CommandButtonHandler getCommandHandler() {
    return handler;
  }

  public interface CommandButtonHandler extends OnClickListener {
    public boolean canHome();
    public boolean canUndo();
    public boolean canRedo();
    public boolean canAdd();
    public boolean canDelete();
    public boolean canAccept();
    public boolean canCancel();
    public boolean canConfig();
    
    public void onHome();
    public void onUndo();
    public void onRedo();
    public void onAdd();
    public void onDelete();
    public void onAccept();
    public void onCancel();
    public void onConfig();
  }
}
