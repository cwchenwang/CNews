package wangchen.java.com.cnews;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import wangchen.java.com.cnews.db.NewsDBHelper;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class MainActivity extends AppCompatActivity {
  private ArrayList<TypeName> typeList = new ArrayList<TypeName>() {{
    for(TypeName typeName : TypeName.values()) {
      add(typeName);
    }
  }};

  private final int REQUEST_LOGIN = 2;
  final String dialogItemList[] = new String[TypeName.values().length];

  private boolean dialogRes[] = new boolean[TypeName.values().length];

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private ViewPagerAdapter viewPagerAdapter;
  private ImageButton imageButton;
  private ImageButton loginButton;
  private Toolbar toolbar;
  private DrawerLayout mDrawerLayout;
  private AlertDialog.Builder builder;
  private NavigationView navigationView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    viewPager = (ViewPager)findViewById(R.id.viewPager);
    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), typeList);
    viewPager.setAdapter(viewPagerAdapter);
    tabLayout = findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);


    toolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    mDrawerLayout = findViewById(R.id.drawer_layout);

    navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.nav_collections) {
                  Intent intent = new Intent(getApplicationContext(), NewsCollectActivity.class);
                  startActivity(intent);
                }

                // set item as selected to persist highlight
                //menuItem.setChecked(true);
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


    builder = new AlertDialog.Builder(this);
    int i = 0;
    for(TypeName typeName : TypeName.values()) {
      dialogItemList[i] = typeName.getName();
      dialogRes[i] = false;
      i++;
    }

    imageButton = findViewById(R.id.typeManager);
    imageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int i = 0;
        for(TypeName typeName : TypeName.values()) {
          if(typeList.contains(typeName)) {
            dialogRes[i] = true;
          }
          i++;
        }
        builder.setMultiChoiceItems(dialogItemList, dialogRes, new DialogInterface.OnMultiChoiceClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            dialogRes[which] = isChecked;
          }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            for(int i = 0; i < dialogRes.length; i++) {
              TypeName t = TypeName.getType(dialogItemList[i]);
              if(t.getName().equals("推荐")) continue;
              if(dialogRes[i] == false) {
                if(typeList.contains(t)) {
                  typeList.remove(t);
                  //viewPagerAdapter.typeList.remove(t);
                }
              }
              else {
                if(typeList.contains(t) == false)
                {
                  typeList.add(t);
                  for(int j = 0; j < typeList.size(); j++) {
                    //Log.v("ha", typeList.get(j).toString());
                  }
                }
              }
            }
            viewPagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(0);
            dialog.dismiss();
          }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
      }
    });

    loginButton = navigationView.getHeaderView(0).findViewById(R.id.login_button);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "登陆", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_home, menu);

    //SearchView searchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
    SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
    searchView.setSearchableInfo(info);

    searchView.setQueryHint("查找内容...");
    searchView.setSubmitButtonEnabled(true);
    //设置监听
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getApplicationContext(), "搜索成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), NewsSearchActivity.class);
        intent.putExtra("SEARCH", query);
        startActivity(intent);
        return true;
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
      case R.id.about:
        AlertDialog myDialog;
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("About");
        b.setMessage("CNews is a news app developed by Clarence Wang.");
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
        myDialog = b.create();
        myDialog.show();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == REQUEST_LOGIN) {
      if(resultCode == LoginActivity.LOGIN_SUCCESS) {
        String username = data.getStringExtra("USERNAME");
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.username_display);
        textView.setText(username);
        Log.v("username", username);
      }
    }
  }
}