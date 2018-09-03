package wangchen.java.com.cnews;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class RSSParser {
  private final String TAG = "RSSParser";
  InputStream in;

  public RSSParser(InputStream inputStream) {
    this.in = inputStream;
  }

  public final List<RSSItem> parseRSS() throws IOException {
    List<RSSItem> rssList = new ArrayList<>();

    String title = null;
    String link = null;
    String pubDate = null;
    boolean isItem = false;

    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      XmlPullParser parser = factory.newPullParser();
      parser.setInput(in, null);

      while(parser.next() != XmlPullParser.END_DOCUMENT) {
        int eventType = parser.getEventType();
        String name = parser.getName();
        if(name == null) continue;
        if(eventType == XmlPullParser.END_TAG) {
          if(name.equalsIgnoreCase("item")) {
            isItem = false;
          }
          continue;
        }
        if(eventType == XmlPullParser.START_TAG) {
          if(name.equalsIgnoreCase("item")) {
            isItem = true;
            continue;
          }
        }

        String str = "";
        if(parser.next() == XmlPullParser.TEXT) {
          str = parser.getText();
          parser.nextTag();
        }

        if(name.equalsIgnoreCase("title")) {
          title = str;
        } else if(name.equalsIgnoreCase("pubDate")) {
          pubDate = str;
        } else if(name.equalsIgnoreCase("link")) {
          link = str;
        }

        if(title != null && link != null && pubDate != null) {
          if (isItem) {
            RSSItem item = new RSSItem(title, link, pubDate);
            rssList.add(item);
          }
          title = null;
          link = null;
          pubDate = null;
          isItem = false;
        }
      }
    } catch(XmlPullParserException e) {
      Log.e(TAG, "XMlPullParserError", e);
    }
    finally {
      in.close();
    }
    return rssList;
  }
}