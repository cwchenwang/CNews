package wangchen.java.com.cnews;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

enum RSSTAG {
  TITLE, DATE, LINK, DESCRIPTION, GUID, AUTHOR, IGNORETAG
}

public class RSSParser {
  private RSSTAG currentTag;
  InputStream in;

  public RSSParser(InputStream inputStream) {
    this.in = inputStream;
  }

  public final ArrayList<RSSItem> parseRSS() throws IOException {
    ArrayList<RSSItem> postDataList = new ArrayList<>();
    try {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(in, null);

        int eventType = xpp.getEventType();
        RSSItem pdData = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
          if (eventType == XmlPullParser.START_DOCUMENT) {

          } else if (eventType == XmlPullParser.START_TAG) {
            if (xpp.getName().equals("item")) {
              pdData = new RSSItem();
              currentTag = RSSTAG.IGNORETAG;
            } else if (xpp.getName().equalsIgnoreCase("title")) {
              currentTag = RSSTAG.TITLE;
            } else if (xpp.getName().equalsIgnoreCase("link")) {
              currentTag = RSSTAG.LINK;
            } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
              currentTag = RSSTAG.DATE;
            } else if(xpp.getName().equalsIgnoreCase("author")) {
              currentTag = RSSTAG.AUTHOR;
            } else if(xpp.getName().equalsIgnoreCase("description")) {
              currentTag = RSSTAG.DESCRIPTION;
            }
          } else if (eventType == XmlPullParser.END_TAG) {
            if (xpp.getName().equals("item")) {
              // format the data here, otherwise format data in
              // Adapter
              postDataList.add(pdData);
            } else {
              currentTag = RSSTAG.IGNORETAG;
            }
          } else if (eventType == XmlPullParser.TEXT) {
            String content = xpp.getText();
            content = content.trim();
            Log.d("debug", content);
            if (pdData != null) {
              if(content.length() != 0) {
                if(currentTag == RSSTAG.DATE) pdData.setDate(content);
                else if(currentTag == RSSTAG.DESCRIPTION) pdData.setDescription(content);
                else if(currentTag == RSSTAG.LINK) pdData.setLink(content);
                else if(currentTag == RSSTAG.AUTHOR) pdData.setAuthor(content);
                else if(currentTag == RSSTAG.TITLE) pdData.setTitle(content);
              }
            }
          }
          eventType = xpp.next();
        }
    } catch(XmlPullParserException e) {
      e.printStackTrace();
    }
    return postDataList;
  }
}