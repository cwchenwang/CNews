package wangchen.java.com.cnews;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import wangchen.java.com.cnews.db.NewsDBHelper;

public class NewsCollectActivity extends AppCompatActivity {

  private ListView listView;
  private ArrayList<RSSItem> collectionList = null;
  private RSSAdapter adapter;
  private CollectDataController collectDataController;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_collect);
    listView = (ListView)this.findViewById(R.id.collectionlist);
    collectDataController = new CollectDataController();
    collectDataController.execute();

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);

        intent.putExtra("RSSITEM", collectionList.get(position));
        intent.putExtra("POSITION", position);
        TextView titleView = view.findViewById(R.id.newstitle);
        TextView dateView = view.findViewById(R.id.newsdate);

        collectionList.get(position).setRead();
        titleView.setTextColor(getResources().getColor(R.color.grey));
        dateView.setTextColor(getResources().getColor(R.color.grey));
        adapter.notifyDataSetChanged();
        startActivity(intent);
      }
    });
  }

  private class CollectDataController extends AsyncTask<String, Integer, ArrayList<RSSItem>> {
    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
      final NewsDBHelper db = ((CNewsApp)getApplicationContext()).getDB();
      return db.retriveCollections();
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
      if(result.size() == 0) {
        RSSItem rssItem = new RSSItem("你没有收藏，快去添加吧", "", "1970-01-01 00:00:00");
        collectionList = new ArrayList<>();
        collectionList.add(rssItem);
        adapter = new RSSAdapter(NewsCollectActivity.this, R.layout.newsitem, collectionList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
      }
      else {
        collectionList = result;
        for(int i = 0; i < result.size(); i++) {
          collectionList.get(i).setUnread();
        }
        adapter = new RSSAdapter(NewsCollectActivity.this, R.layout.newsitem, collectionList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
      }
//      if (rssList == null) {
//        rssList = new ArrayList<>();
//        if(getActivity() != null) {
//          adapter = new RSSAdapter(getActivity(), R.layout.newsitem, rssList);
//          listView.setAdapter(adapter);
//        }
//        else return;
//      }
//      int t = rssList.size();
//      for (int i = 0; i < result.size(); i++) {
//        if(i >= 10) break;
//        if (i < t) rssList.set(i, result.get(i));
//        else rssList.add(result.get(i));
//        //Log.v("Data loaded", result.get(i).toString());
//      }
//      adapter.notifyDataSetChanged();
//      loadingData = false;
    }
  }
}
