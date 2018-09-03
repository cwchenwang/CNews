package wangchen.java.com.cnews;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.util.Log;
import android.os.AsyncTask;

import org.xmlpull.v1.*;

import java.net.*;
import java.util.Date;
import java.util.ArrayList;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

  ArrayList<RSSItem> rssList;
  RSSAdapter adapter;
  private final String url = "http://news.qq.com/newsgn/rss_newsgn.xml";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.newslist);

    generateData(10);
    ListView listView = findViewById(R.id.newslistView);
    adapter = new RSSAdapter(this, R.layout.newsitem, rssList);
    listView.setAdapter(adapter);
    //new RssDataController().execute(url);
  }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.main, menu);
//    return true;
//  }

  private void generateData(int n) {
    rssList = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      RSSItem tep = new RSSItem("This is the " + i + " post", "", new SimpleDateFormat("yyyy-mm-dd").format(new Date()));
      rssList.add(tep);
    }
  }
  private enum RSSXMLTag {
    TITLE, DATE, LINK, CONTENT, GUID, IGNORETAG;
  }

  private class RssDataController extends AsyncTask<String, Integer, ArrayList<RSSItem>>{
    private RSSXMLTag currentTag;

    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
      // TODO Auto-generated method stub
      String urlStr = params[0];
      InputStream is = null;
      ArrayList<RSSItem> postDataList = new ArrayList<RSSItem>();
      try {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setReadTimeout(10 * 1000);
        connection.setConnectTimeout(10 * 1000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        int response = connection.getResponseCode();
        Log.d("debug", "The response is: " + response);
        is = connection.getInputStream();

        // parse xml after getting the data
        XmlPullParserFactory factory = XmlPullParserFactory
                .newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(is, null);

        int eventType = xpp.getEventType();
        RSSItem pdData = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, DD MMM yyyy HH:mm:ss");
        while (eventType != XmlPullParser.END_DOCUMENT) {
          if (eventType == XmlPullParser.START_DOCUMENT) {

          } else if (eventType == XmlPullParser.START_TAG) {
            if (xpp.getName().equals("item")) {
              pdData = new RSSItem();
              currentTag = RSSXMLTag.IGNORETAG;
            } else if (xpp.getName().equals("title")) {
              currentTag = RSSXMLTag.TITLE;
            } else if (xpp.getName().equals("link")) {
              currentTag = RSSXMLTag.LINK;
            } else if (xpp.getName().equals("pubDate")) {
              currentTag = RSSXMLTag.DATE;
            }
          } else if (eventType == XmlPullParser.END_TAG) {
            if (xpp.getName().equals("item")) {
              // format the data here, otherwise format data in
              // Adapter
              Date postDate = dateFormat.parse(pdData.getPubDate());
              pdData.setDate(dateFormat.format(postDate));
              postDataList.add(pdData);
            } else {
              currentTag = RSSXMLTag.IGNORETAG;
            }
          } else if (eventType == XmlPullParser.TEXT) {
            String content = xpp.getText();
            content = content.trim();
            Log.d("debug", content);
            if (pdData != null) {
              switch (currentTag) {
                case TITLE:
                  if (content.length() != 0) {
                    if (pdData.getTitle() != null) {
                      pdData.setTitle(content);
                    } else {
                      pdData.setTitle(content);
                    }
                  }
                  break;
                case LINK:
                  if (content.length() != 0) {
                    pdData.setLink(content);
                  }
                  break;
                case DATE:
                  if (content.length() != 0) {
                    pdData.setDate(content);
                  }
                  break;
                default:
                  break;
              }
            }
          }

          eventType = xpp.next();
        }
        Log.v("tst", String.valueOf((postDataList.size())));
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (XmlPullParserException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return postDataList;
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
      // TODO Auto-generated method stub
      for (int i = 0; i < result.size(); i++) {//ignore this comment >
        rssList.add(result.get(i));
      }
      adapter.notifyDataSetChanged();
    }
  }
}
