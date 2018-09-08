package wangchen.java.com.cnews;

import android.app.Application;

import wangchen.java.com.cnews.db.NewsDBHelper;

//To save global variables

public class CNewsApp extends Application {

  private NewsDBHelper newsDBHelper;

  @Override
  public void onCreate() {
    super.onCreate();
    newsDBHelper = new NewsDBHelper(this);
  }

  public NewsDBHelper getDB() {
     return newsDBHelper;
  }
}
