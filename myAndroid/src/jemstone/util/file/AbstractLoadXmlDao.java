package jemstone.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jemstone.util.MyRuntimeException;
import jemstone.util.log.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;

public abstract class AbstractLoadXmlDao implements LoadFileDao {
  protected final Logger log = Logger.getLogger(this);

  @SuppressLint("SimpleDateFormat")
  protected final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
  protected final XmlPullParser xpp;

  protected final String NAMESPACE;
  protected final String ROOT_NODE;

  /** Map tag names to entity parser */
  protected final Map<String,EntityParser> parsers = new HashMap<String,EntityParser>();

  public AbstractLoadXmlDao(String nameSpace, String rootNode) throws DaoException {
    this.NAMESPACE = nameSpace;
    this.ROOT_NODE = rootNode;

    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      xpp = factory.newPullParser();
    } catch (XmlPullParserException e) {
      throw new DaoException(e);
    }
  }

  public void load(InputStream input) throws DaoException {
    try {
      xpp.setInput(input, ENCODING);
      parse();
    } catch (DaoException e) {
      throw e;
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  @Override
  public void load(Reader input) throws DaoException {
    try {
      xpp.setInput(input);
      parse();
    } catch (DaoException e) {
      throw e;
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  protected abstract void parse() throws XmlPullParserException, IOException, DaoException;

  protected void parse(final String parentTag, EntityParser parser) throws XmlPullParserException, IOException, DaoException {
    String tag = null;

    for (int eventType = xpp.getEventType();  ; eventType = xpp.next()) {
      switch (eventType) {
        case XmlPullParser.START_TAG:
          tag = xpp.getName();
          EntityParser p = parsers.get(tag);
          if (p != null) {
            int id = parseId();
            p.create(id);

            xpp.next();
            parse(tag, p);
          }
          break;

        case XmlPullParser.END_TAG:
          tag = xpp.getName();
          if (parentTag != null && parentTag.equals(tag)) {
            return;
          }
          tag = null;
          break;

        case XmlPullParser.TEXT:
          String value = xpp.getText();
          if (parser != null && tag != null) {
            try {
              parser.parse(tag, value);
            } catch (IllegalArgumentException e) {
              // thrown if tag is not recognised, ignore
            } catch (Exception e) {
              final String className = parser.getClass().getSimpleName();
              throw new DaoException(e, "Error in %s.parse: [tag=%s, value=%s]", className, tag, value);
            }
          }
          break;

        case XmlPullParser.END_DOCUMENT:
          return;
      }
    }
  }

  protected int parseId() {
    String attrib = xpp.getAttributeValue(null, ID);
    return (attrib != null) ? Integer.parseInt(attrib) : 0;
  }

  protected Date parseDate(String value) {
    try {
      return dateFormat.parse(value);
    } catch (ParseException e) {
      throw new MyRuntimeException(e, "Error parsing date: %s", value);
    }
  }

  public interface EntityParser {
    void create(int id);
    void parse(String tag, String value);
  }
}
