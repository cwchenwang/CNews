package wangchen.java.com.cnews;

import android.database.sqlite.SQLiteDatabase;
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
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.widget.Toast;


public class RecommandFragment extends Fragment {
  private ListView listView;
  private SwipeRefreshLayout swipeView;

  private ArrayList<RSSItem> rssList = null;
  private RSSAdapter adapter;
  private RssDataController rssDataController;
  private RefreshController refreshController;

  private String tag = "tag";
  private String sourceUrl = "http://rss.sina.com.cn/news/china/focus15.xml"; //热点url
  private boolean isRefreshing = false;
  private long lastRecommandTime = 0;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.newslist, container, false);
    listView = view.findViewById(R.id.newslistView);
    swipeView = view.findViewById(R.id.swipe_refresh_layout);

    rssDataController = new RssDataController();
    rssDataController.execute(sourceUrl);

    refreshController = new RefreshController();
    swipeView.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                if(rssDataController.getStatus() == AsyncTask.Status.PENDING){
                  rssDataController.execute(sourceUrl);
                  Toast.makeText(getActivity().getApplicationContext(), "正在为您推荐，请稍候", Toast.LENGTH_SHORT).show();
                  swipeView.setRefreshing(false);
                  return;
                }
                if(isRefreshing) return;

                long curTime = Calendar.getInstance().getTimeInMillis() / 1000;
                long tep = (long)(Math.random() * 30 + 30);
                if(curTime - lastRecommandTime < tep) {
                  swipeView.setRefreshing(false);
                  Toast.makeText(getActivity().getApplicationContext(), "太频繁啦，没有那么可推荐哦", Toast.LENGTH_SHORT).show();
                  return;
                }
                refreshController = new RefreshController();
                refreshController.execute(sourceUrl);
                swipeView.setRefreshing(false);
//                else if(refreshController.getStatus() == AsyncTask.Status.PENDING) {
//                  Toast.makeText(getActivity().getApplicationContext(), "为您更新推荐，请稍候", Toast.LENGTH_SHORT).show();
//                  swipeView.setRefreshing(false);
//                  return;
//                }
//                else {
//                  Log.v("ha", "launching");
//                  refreshController.execute(sourceUrl);
//                  //Toast.makeText(getActivity().getApplicationContext(), "正在加载呢，请等待", Toast.LENGTH_SHORT).show();
//                  swipeView.setRefreshing(false);
//                }
              }
            }
    );

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), NewsDetailActivity.class);

        intent.putExtra("RSSITEM", rssList.get(position));
        intent.putExtra("POSITION", position);

        rssList.get(position).setRead();

        final int tep_p = position;
        adapter.notifyDataSetChanged();
        new Thread(new Runnable() {
          @Override
          public void run() {
            Log.v("db", "starting db");
            NewsDBHelper db = ((CNewsApp)getActivity().getApplicationContext()).getDB();
            db.insertRecommand(rssList.get(tep_p));
          }
        }) {
        }.start();
        startActivity(intent);

//        ((CNewsApp)getActivity().getApplicationContext()).updateReadTimes(TypeName.getType(tag));
//        HashMap<TypeName, Integer> map = ((CNewsApp)getActivity().getApplicationContext()).getReadTimes();
//        for(TypeName key: map.keySet()) {
//          Log.v(key.getName(), map.get(key).toString());
//        }
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

      rssList = new ArrayList<>();
      if(getActivity() != null) {
        adapter = new RSSAdapter(getActivity(), R.layout.newsitem, rssList);
        listView.setAdapter(adapter);
      } else return;

      NewsDBHelper db = ((CNewsApp)getActivity().getApplicationContext()).getDB();
      ArrayList<RSSItem> tep = db.retriveRecommand();
//      Log.v("tepsize", tep.size()+"");
//      Log.v("type", tag);
      for(int i = 0; i < tep.size(); i++) {
      //  Log.v("type", tep.get(i).toString());
        if(tep.get(i).haveRead())
          rssList.add(0, tep.get(i));
        else rssList.add(tep.get(i));
      } //加载数据库中保存的列表

      adapter.notifyDataSetChanged();
      if(tep.size() > 0) return;

      int cnt = 0;
      int random = (int)Math.random() * result.size();
      if(result.size() > 0 && !rssList.contains(result.get(random))) {
        rssList.add(0, result.get(random));
        db.insertRecommand(result.get(random));
        cnt++;
      }

      if(rssList.size() > 30) {
        for(int i = 0; i < 3; i++) {
          rssList.remove(Math.random() * rssList.size());
        }
      }

      HashMap<TypeName, Integer> map = ((CNewsApp)getActivity().getApplicationContext()).getReadTimes();
      for(int i = 0; i < 3; i++) {
        int t = Roulette.nextType(map);
        RSSItem rssItem = db.retrieveOneRandomly(t);
        if(rssItem != null && !rssList.contains(rssItem)) {
          cnt++;
          rssList.add(0, rssItem);
          db.insertRecommand(rssItem);
        }
      }
      adapter.notifyDataSetChanged();
      if(cnt > 0) Toast.makeText(getActivity(), "更新了"+cnt+"条推荐", Toast.LENGTH_SHORT).show();
      else Toast.makeText(getActivity(), "暂无新的推荐", Toast.LENGTH_SHORT).show();
    }
  }

  private class RefreshController extends AsyncTask<String, Integer, ArrayList<RSSItem>>{
    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
      Log.v("ha", "do in backgroud");
      return fetchData(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {

      Log.v("ha", "onPostExecute");
      rssList = new ArrayList<>();
      if(getActivity() != null) {
        adapter = new RSSAdapter(getActivity(), R.layout.newsitem, rssList);
        listView.setAdapter(adapter);
      } else return;

      Log.v("db", "reading database");
      NewsDBHelper db = ((CNewsApp)getActivity().getApplicationContext()).getDB();
      ArrayList<RSSItem> tep = db.retriveRecommand();
//      Log.v("tepsize", tep.size()+"");
//      Log.v("type", tag);
      for(int i = 0; i < tep.size(); i++) {
        //  Log.v("type", tep.get(i).toString());
        if(tep.get(i).haveRead())
          rssList.add(0, tep.get(i));
        else rssList.add(tep.get(i));
      } //加载数据库中保存的列表

      int cnt = 0;
      int random = (int)Math.random() * result.size();
      if(result.size() > 0 && !rssList.contains(result.get(random))) {
        rssList.add(0, result.get(random));
        db.insertRecommand(result.get(random));
        cnt++;
      }

      if(rssList.size() > 30) {
        for(int i = 0; i < 3; i++) {
          rssList.remove(Math.random() * rssList.size());
        }
      }

      HashMap<TypeName, Integer> map = ((CNewsApp)getActivity().getApplicationContext()).getReadTimes();
      for(int i = 0; i < 3; i++) {
        int t = Roulette.nextType(map);
        RSSItem rssItem = db.retrieveOneRandomly(t);
        if(rssItem != null && !rssList.contains(rssItem)) {
          cnt++;
          rssList.add(0, rssItem);
          db.insertRecommand(rssItem);
        }
      }
      adapter.notifyDataSetChanged();
      Log.v("recommand", "finished");
      if(cnt > 0) Toast.makeText(getActivity(), "更新了"+cnt+"条推荐", Toast.LENGTH_SHORT).show();
      else Toast.makeText(getActivity(), "暂无新的推荐", Toast.LENGTH_SHORT).show();
      isRefreshing = false;
      lastRecommandTime = (long)(Calendar.getInstance().getTimeInMillis() / 1000);
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
}