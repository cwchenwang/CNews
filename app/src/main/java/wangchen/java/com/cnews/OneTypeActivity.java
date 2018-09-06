package wangchen.java.com.cnews;

import android.widget.Toast;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Collections;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import android.util.Log;
import android.widget.ArrayAdapter;

public class OneTypeActivity extends AppCompatActivity {
  //Code for view
  private AlertDialog aboutDialog;
  private ListView listView;
  private SwipeRefreshLayout swipeView;

  //Code for data
  private ArrayList<RSSItem> rssList = null;
  private RSSAdapter adapter = null;
  private RssDataController dataController;
  private RefreshController refreshController;
  private String url;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Intent in = getIntent();
    url = in.getStringExtra("URL");
    Log.v("ha", url);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.newslist);

    swipeView = findViewById(R.id.swipe_refresh_layout);

    listView = findViewById(R.id.newslistView);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), NewsContentActivity.class);
        String pageLink = ((TextView)view.findViewById(R.id.newslink)).getText().toString().trim();
        intent.putExtra("LINK", pageLink);

        TextView titleView = view.findViewById(R.id.newstitle);
        TextView dateView = view.findViewById(R.id.newsdate);
        titleView.setTextColor(getResources().getColor(R.color.grey));
        dateView.setTextColor(getResources().getColor(R.color.grey));
        adapter.notifyDataSetChanged();
        startActivity(intent);
      }
    });
    dataController = new RssDataController();
    dataController.execute(url);
    swipeView.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                refreshController = new RefreshController();
                refreshController.execute(url);
                swipeView.setRefreshing(false);
              }
            }
    );
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
      for(RSSItem s : postDataList) {
        Log.v("ha", s.toString());
      }
      return postDataList;
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
      if(rssList == null) {
        rssList = new ArrayList<>();
        adapter = new RSSAdapter(OneTypeActivity.this, R.layout.newsitem, rssList);
        listView.setAdapter(adapter);
      }
      int t = rssList.size();
      for(int i = 0; i < result.size(); i++) {
        if(i < t) rssList.set(i, result.get(i));
        else rssList.add(result.get(i));
      }
      adapter.notifyDataSetChanged();
    }
  }

    private class RefreshController extends AsyncTask<String, Integer, ArrayList<RSSItem>>{
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
        if(rssList == null) {
          rssList = new ArrayList<>();
          adapter = new RSSAdapter(OneTypeActivity.this, R.layout.newsitem, rssList);
          listView.setAdapter(adapter);
        }
        int t = rssList.size();
        if(t == result.size()) Toast.makeText(getApplicationContext(), "您的新闻已最新",Toast.LENGTH_SHORT).show();
        else {
          for (int i = 0; i < result.size(); i++) {
            if (i < t) rssList.set(i, result.get(i));
            else rssList.add(result.get(i));
          }
          //set adapter here
          adapter.notifyDataSetChanged();
          int i = result.size() - t;
          Toast.makeText(getApplicationContext(), "更新了" + i + "条新闻",Toast.LENGTH_SHORT).show();
        }
      }
  }
}
