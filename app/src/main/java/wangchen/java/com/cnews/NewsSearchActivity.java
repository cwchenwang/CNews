package wangchen.java.com.cnews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class NewsSearchActivity extends AppCompatActivity {

  private ListView listView;
  private ArrayList<RSSItem> searchList = null;
  private RSSAdapter adapter;
  private SearchDataController searchDataController;
  private String toSearch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_collect);
    listView = (ListView)this.findViewById(R.id.collectionlist);

    toSearch = getIntent().getStringExtra("SEARCH");

    TextView textView = this.findViewById(R.id.collect_text);
    textView.setText("\"" + toSearch + "\"" + "的搜索结果");

    searchDataController = new SearchDataController();
    searchDataController.execute(toSearch);



    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);

        intent.putExtra("RSSITEM", searchList.get(position));
        intent.putExtra("POSITION", position);
        TextView titleView = view.findViewById(R.id.newstitle);
        TextView dateView = view.findViewById(R.id.newsdate);

        searchList.get(position).setRead();
        titleView.setTextColor(getResources().getColor(R.color.grey));
        dateView.setTextColor(getResources().getColor(R.color.grey));
        adapter.notifyDataSetChanged();

        startActivity(intent);

//        final int tep_p = position;
//        new Thread(new Runnable() {
//          @Override
//          public void run() {
//            NewsDBHelper db = ((CNewsApp)getApplicationContext()).getDB();
//            db.updateItem(searchList.get(tep_p));
//          }
//        });

      }
    });

  }

  private class SearchDataController extends AsyncTask<String, Integer, ArrayList<RSSItem>> {
    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
      final NewsDBHelper db = ((CNewsApp)getApplicationContext()).getDB();
      return db.searchStr(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
      if(result.size() == 0) {
        Toast.makeText(getApplicationContext(), "没有搜到结果哦", Toast.LENGTH_SHORT).show();
      }
      else {
        searchList = result;
        adapter = new RSSAdapter(NewsSearchActivity.this, R.layout.newsitem, searchList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
      }
    }
  }
}
