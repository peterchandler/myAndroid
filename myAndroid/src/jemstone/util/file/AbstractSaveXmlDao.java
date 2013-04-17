package jemstone.util.file;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import jemstone.model.EntityManager;
import jemstone.model.HasId;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.annotation.SuppressLint;

public abstract class AbstractSaveXmlDao<T extends EntityManager> implements SaveFileDao<T> {
  @SuppressLint("SimpleDateFormat")
  protected final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
  protected final XmlSerializer serializer;

  protected final String NAMESPACE;
  protected final String ROOT_NODE;

  /**
   * TESTING ONLY: If set, and exception will be thrown from {@link #save(EntityManager)}
   * before the complete document is written. This is to facilitate testing of how the
   * application handles file corruptions.
   */
  private final boolean throwTestException;

  public AbstractSaveXmlDao(String nameSpace, String rootNode) throws DaoException {
    this(nameSpace, rootNode, false);
  }

  /**
   * USE FOR TESTING ONLY: If set, an exception will be thrown from {@link #save(EntityManager)}
   * before the document is completely written. This is to facilitate testing of how the
   * application handles file corruptions.
   */
  public AbstractSaveXmlDao(String nameSpace, String rootNode, boolean throwTestException) throws DaoException {
    this.NAMESPACE = nameSpace;
    this.ROOT_NODE = rootNode;
    this.throwTestException = throwTestException;
    
    try {
      serializer = XmlPullParserFactory.newInstance().newSerializer();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  public void save(T manager, OutputStream out) throws DaoException {
    try {
      serializer.setOutput(out, ENCODING);
      save(manager);
    } catch (UnsupportedOperationException e) {
      throw e;
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  @Override
  public void save(T manager, Writer out) throws DaoException {
    try {
      serializer.setOutput(out);
      save(manager);
    } catch (UnsupportedOperationException e) {
      throw e;
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  private void save(T manager) throws IllegalArgumentException, IllegalStateException, IOException {
    // Start the document
    serializer.startDocument(ENCODING, true);
    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
    serializer.setPrefix(PREFIX, NAMESPACE);
    serializer.startTag(NAMESPACE, ROOT_NODE);
    
    // Save all content
    saveContent(manager);

    // Throw an exception if in test mode
    if (throwTestException) {
      throw new UnsupportedOperationException("TEST EXCEPTION: A test case has asked for the save operation to fail part-way through");
    }

    // End the document
    serializer.endTag(NAMESPACE, ROOT_NODE);
    serializer.endDocument();
  }

  protected abstract void saveContent(T manager) throws IllegalArgumentException, IllegalStateException, IOException;

  protected void write(String tag, String value) throws IllegalArgumentException, IllegalStateException, IOException {
    if (value != null) {
      serializer.startTag(NAMESPACE, tag);
      serializer.text(value);
      serializer.endTag(NAMESPACE, tag);
    }
  }

  protected void write(String tag, Date date) throws IllegalArgumentException, IllegalStateException, IOException {
    if (date != null) {
      String text = dateFormat.format(date);
      serializer.startTag(NAMESPACE, tag);
      serializer.text(text);
      serializer.endTag(NAMESPACE, tag);
    }
  }

  protected void write(String tag, boolean value) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag(NAMESPACE, tag);
    serializer.text(value ? "true" : "false");
    serializer.endTag(NAMESPACE, tag);
  }

  protected void write(String tag, long value) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag(NAMESPACE, tag);
    serializer.text(Long.toString(value));
    serializer.endTag(NAMESPACE, tag);
  }

  protected void write(String tag, double value) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag(NAMESPACE, tag);
    serializer.text(Double.toString(value));
    serializer.endTag(NAMESPACE, tag);
  }

  protected void write(HasId item) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.attribute(null, ID, Integer.toString(item.getId()));
  }
}
