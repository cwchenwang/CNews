package wangchen.java.com.cnews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Ref;
import java.util.ArrayList;
import android.content.Context;
import android.widget.Toast;

public class NewsFragment extends Fragment {
  private ListView listView;
  private SwipeRefreshLayout swipeView;

  private ArrayList<RSSItem> rssList = null;
  private RSSAdapter adapter;
  private RssDataController rssDataController;
  private RefreshController refreshController;

  private String tag = "tag";
  private String sourceUrl;
  private boolean loadingData = true;

  private int REQUESTCODE = 1;

  public static NewsFragment newInstance(String sourceStr, String tag) {
    NewsFragment newsFragment = new NewsFragment();
    Bundle bundle = new Bundle();
    bundle.putString("SOURCEURL", sourceStr);
    bundle.putString("TAG", tag); //the type of this fragment
    newsFragment.setArguments(bundle);
    return newsFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.newslist, container, false);
    listView = view.findViewById(R.id.newslistView);
    swipeView = view.findViewById(R.id.swipe_refresh_layout);

    sourceUrl = getArguments().getString("SOURCEURL");
    tag = getArguments().getString("TAG");

    rssDataController = new RssDataController();
    rssDataController.execute(sourceUrl);

    refreshController = new RefreshController();
    swipeView.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                if(loadingData == true) {
                  Toast.makeText(getActivity().getApplicationContext(), "正在努力获取新闻，请等待", Toast.LENGTH_SHORT).show();
                  swipeView.setRefreshing(false);
                  return;
                }
                //Toast.makeText(getActivity().getApplicationContext(), "您的新闻已最新", Toast.LENGTH_SHORT).show();
                refreshController = new RefreshController();
                refreshController.execute(sourceUrl);
              }
            }
    );

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), NewsDetailActivity.class);
        String pageLink = ((TextView)view.findViewById(R.id.newslink)).getText().toString().trim();
        String title = ((TextView)view.findViewById(R.id.newstitle)).getText().toString().trim();
        String pubDate = ((TextView)view.findViewById(R.id.newsdate)).getText().toString().trim();

        intent.putExtra("RSSITEM", rssList.get(position));
        intent.putExtra("POSITION", position);
        TextView titleView = view.findViewById(R.id.newstitle);
        TextView dateView = view.findViewById(R.id.newsdate);

        rssList.get(position).setRead();
//        titleView.setTextColor(getResources().getColor(R.color.grey));
//        dateView.setTextColor(getResources().getColor(R.color.grey));
        adapter.notifyDataSetChanged();
        startActivityForResult(intent, REQUESTCODE);
      }
    });

    return view;
  }

  private class RssDataController extends AsyncTask<String, Integer, ArrayList<RSSItem>>{
    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
      return fetchData(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
      if (rssList == null) {
        rssList = new ArrayList<>();
        if(getActivity() != null) {
          adapter = new RSSAdapter(getActivity(), R.layout.newsitem, rssList);
          listView.setAdapter(adapter);
        }
        else return;
      }
      int t = rssList.size();
      for (int i = 0; i < result.size(); i++) {
        if(i >= 10) break;
        if (i < t) rssList.set(i, result.get(i));
        else rssList.add(result.get(i));
        Log.v("Data loaded", result.get(i).toString());
      }
      adapter.notifyDataSetChanged();
      loadingData = false;
    }
  }

  private class RefreshController extends AsyncTask<String, Integer, ArrayList<RSSItem>> {
    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
      return fetchData(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
      int cnt = 0;
      for(RSSItem item : result) {
        Log.v("result", item.toString());
        if(rssList.contains(item)) {
          continue;
        }
        else {
          rssList.add(0, item);
          cnt++;
        }
      }

      adapter.rssList = rssList; //重新指向，避免出错
      adapter.notifyDataSetChanged();


//      for(int i = 0; i < adapter.rssList.size(); i++) {
//        Log.v(i+"", adapter.rssList.get(i).toString());
//        if(adapter.rssList.get(i).haveRead()) {
//          Log.v("read", i+"");
//        }
//        else Log.v("not read", i+"");
//      }

      if(cnt == 0)
        Toast.makeText(getActivity().getApplicationContext(), "您的新闻已最新", Toast.LENGTH_SHORT).show();
      else {
        //set adapter here
        //adapter.notifyDataSetChanged();
        Toast.makeText(getActivity().getApplicationContext(), "更新了" + cnt + "条新闻", Toast.LENGTH_SHORT).show();
      }
      swipeView.setRefreshing(false);
    }
  }

  private ArrayList<RSSItem> fetchData(String sourceUrl) {
    String urlStr = sourceUrl;
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
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
    // operation succeeded. 默认值是-1
    if (resultCode == 1) {
      if (requestCode == REQUESTCODE) {
        int position = data.getIntExtra("POSITION", 0);
        //设置结果显示框的显示数值
        RSSItem rssItem = (RSSItem)data.getSerializableExtra("RSSITEM");
        Log.v("ha", rssItem.toString());
        if(rssList.get(position).judgeCollect() != rssItem.judgeCollect()) {
          rssList.get(position).changeCollect();
        }
      }
    }
  }
}