package wangchen.java.com.cnews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import wangchen.java.com.cnews.RSSItem;
import wangchen.java.com.cnews.TypeName;

import java.util.ArrayList;

public class NewsDBHelper extends SQLiteOpenHelper {

  public static final int DB_VERSION = 1;
  public static final String DB_NAME = "NewsDB.db";
  public static final String TABLE_NAME = "news";
  private static final String CREATE_COLLECT = "create table if not exists news " +
          "(type integer, title text UNIQUE, link text, author text, date text, read integer, collect integer)";
  private static final String CREATE_HISTORY = "create table if not exists history " +
          "(type integer, title text UNIQUE, link text, author text, date text, read integer, collect integer)";

  private static final String DROP_COLLECT = "DROP TABLE IF EXISTS news";
  private static final String DROP_HISTORY = "DROP TABLE IF EXISTS history";

  public NewsDBHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_COLLECT);
    db.execSQL(CREATE_HISTORY);
  }


  @Override
  public void onUpgrade(SQLiteDatabase db, int a, int b) { //Upgrade version

  }

  private ContentValues getValues(RSSItem rssItem, int t) {
    ContentValues values = new ContentValues();
    values.put("type", t);
    values.put("title", rssItem.getTitle());
    values.put("link", rssItem.getLink());
    values.put("date", rssItem.getPubDate());
    values.put("author", rssItem.getAuthor());
    if(rssItem.haveRead()) {
      values.put("read", 1);
    }
    else values.put("read", 0);
    if(rssItem.judgeCollect()) {
      values.put("collect", 1);
    }
    else values.put("collect", 0);
    return values;
  }

  public void saveItem(RSSItem rssItem, int t) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = getValues(rssItem, t);
    db.insertWithOnConflict("history", null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }

  public void updateItem(RSSItem rssItem, int t) {
    ContentValues values = getValues(rssItem, t);
    SQLiteDatabase db = getWritableDatabase();
    db.insertWithOnConflict("history", null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }

  public void collectItem(RSSItem rssItem) {
    SQLiteDatabase db = getWritableDatabase();

    ContentValues values = getValues(rssItem, 10);
    db.insertWithOnConflict("news", null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }

  public ArrayList<RSSItem> retriveCollections() {
    SQLiteDatabase db = getWritableDatabase();
    ArrayList<RSSItem> rssList = new ArrayList<>();
    String query = "SELECT * FROM news WHERE type=10";

    Cursor cursor = db.rawQuery(query, null);
    while(cursor.moveToNext()) {
      String title = cursor.getString(1);
      String link = cursor.getString(2);
      String author = cursor.getString(3);
      String date = cursor.getString(4);
      int read = cursor.getInt(5);
      int collect = cursor.getInt(6);
      RSSItem rssItem = new RSSItem(title, link, author, date);
      if(read == 1) rssItem.setRead();
      rssItem.setCollected();
      rssList.add(rssItem);
    }
    return rssList;
  }

  public ArrayList<RSSItem> retriveList(int t) { //Retrieve the whole list of one type
    SQLiteDatabase db = getWritableDatabase();
    ArrayList<RSSItem> rssList = new ArrayList<>();
    String command = "SELECT * FROM history WHERE type=" + t;

    Cursor cursor = db.rawQuery(command, null);
    while(cursor.moveToNext()) {
      String title = cursor.getString(1);
      String link = cursor.getString(2);
      String author = cursor.getString(3);
      String date = cursor.getString(4);
      int read = cursor.getInt(5);
      int collect = cursor.getInt(6);
      RSSItem rssItem = new RSSItem(title, link, author, date);
      if(read == 1) rssItem.setRead();
      if(collect == 1) rssItem.setCollected();
      rssList.add(rssItem);
    }
    return rssList;
  }

  public void removeItem(RSSItem rssItem) {
    SQLiteDatabase db = getWritableDatabase();
    String[] args = { rssItem.getTitle() };
    db.delete("news", "title" + " = ?", args);
  }
}