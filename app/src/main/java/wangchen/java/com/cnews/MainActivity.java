package wangchen.java.com.cnews;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.widget.ImageButton;
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
  private ImageButton imageButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewPager = (ViewPager)findViewById(R.id.viewPager);
    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(viewPagerAdapter);
    tabLayout = findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

//    imageButton = findViewById(R.id.typeManager);
//    imageButton.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Intent intent = new Intent(MainActivity.this, TypeActivity.class);
//
//      }
//    });
  }
}
