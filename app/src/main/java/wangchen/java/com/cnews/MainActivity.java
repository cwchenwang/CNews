package wangchen.java.com.cnews;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v4.view.GravityCompat;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.widget.ImageButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.SpinnerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private ArrayList<String> listData = new ArrayList<>();

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private ViewPagerAdapter viewPagerAdapter;
  private ImageButton imageButton;
  private Toolbar toolbar;
  private DrawerLayout mDrawerLayout;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewPager = (ViewPager)findViewById(R.id.viewPager);
    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(viewPagerAdapter);
    tabLayout = findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);


    toolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    mDrawerLayout = findViewById(R.id.drawer_layout);

    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
              }
            });

    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
//    imageButton = findViewById(R.id.typeManager);
//    imageButton.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Intent intent = new Intent(MainActivity.this, TypeActivity.class);
//
//      }
//    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_home, menu);

    SearchView sv = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();

    //设置监听
    sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
