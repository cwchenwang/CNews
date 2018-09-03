package wangchen.java.com.cnews;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

  ArrayList<RSSItem> rssList;
  RSSAdapter adapter;
  RssDataController dataController;
  private final String url = "http://news.qq.com/newsgn/rss_newsgn.xml";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.newslist);

    generateData(30);
    ListView listView = findViewById(R.id.newslistView);
    adapter = new RSSAdapter(this, R.layout.newsitem, rssList);
    listView.setAdapter(adapter);

    dataController = new RssDataController();
    dataController.execute(url);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    menu.add(0, 1,1, "About");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if(id == 1) {
      Toast.makeText(getApplicationContext(), "CNews is a newsapp developed by Clarence Wang",Toast.LENGTH_SHORT).show();
    }
    return super.onContextItemSelected(item);
  }

  private void generateData(int n) {
    rssList = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      RSSItem tep = new RSSItem("This is the " + i + " post", "", new SimpleDateFormat("yyyy-mm-dd").format(new Date()));
      rssList.add(tep);
    }
  }
  private class RssDataController extends AsyncTask<String, Integer, ArrayList<RSSItem>>{
    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
      String urlStr = params[0];
      ArrayList<RSSItem> postDataList = new ArrayList<>();
      try {
        URL url = new URL(urlStr);
        InputStream inputStream = url.openStream();

        RSSParser rssParser = new RSSParser(inputStream);
        postDataList = rssParser.parseRSS();
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return postDataList;
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
      for (int i = 0; i < result.size(); i++) { //update data
        rssList.set(i, result.get(i));
        Log.v("ha", result.get(i).toString());
      }
      adapter.notifyDataSetChanged();
    }
    
  }
}
