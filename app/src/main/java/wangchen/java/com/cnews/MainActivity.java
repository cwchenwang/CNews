package wangchen.java.com.cnews;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private ArrayList<String> listData = new ArrayList<>();

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private ViewPagerAdapter viewPagerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewPager = (ViewPager)findViewById(R.id.viewPager);
    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(viewPagerAdapter);
    tabLayout = findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);
  }
}
//package wangchen.java.com.cnews;
//
//import android.widget.Toast;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.AsyncTask;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.content.Intent;
//import android.widget.TextView;
//
//import java.text.ParseException;
//import java.util.Collections;
//import java.util.ArrayList;
//import java.io.InputStream;
//import java.io.IOException;
//import java.net.URL;
//import java.net.MalformedURLException;
//
//public class MainActivity extends AppCompatActivity {
//
//  private final String url = "http://news.qq.com/newsgn/rss_newsgn.xml";
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    setContentView(R.layout.newslist);
//  }
//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    //menu.add(0, 1,1, "About");
//    getMenuInflater().inflate(R.menu.menu_home, menu);
//    //Toolbar的搜索框
//    MenuItem searchItem = menu.findItem(R.id.toolbar_search);
//    return true;
//  }
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    switch(item.getItemId()) {
//      case R.id.about:
//        Intent intent = new Intent(getApplicationContext(), OneTypeActivity.class);
//        intent.putExtra("URL", url);
//        startActivity(intent);
//        return true;
//      default:
//        break;
//      }
//      return super.onContextItemSelected(item);
//  }
//}
