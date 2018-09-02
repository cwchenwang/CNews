package wangchen.java.com.cnews;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;
import java.text.SimpleDateFormat;
import android.widget.ListView;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

  private RSSItem[] rssList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.newslist);

    generateData(10);
    ListView listView = findViewById(R.id.newslistView);
    RSSAdapter adapter = new RSSAdapter(this, R.layout.newsitem, rssList);
    listView.setAdapter(adapter);
  }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.main, menu);
//    return true;
//  }

  private void generateData(int n) {
    rssList = new RSSItem[n];
    for(int i = 0; i < n; i++) {
      rssList[i] = new RSSItem();
      rssList[i].setTitle("This is the " + i + " post");
      rssList[i].setDate(new SimpleDateFormat("yyyy-mm-dd").format(new Date()));
    }
  }
}
