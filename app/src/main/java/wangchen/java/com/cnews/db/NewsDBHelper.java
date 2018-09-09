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
          "(type integer, title text UNIQUE, link text, date text, read integer, collect integer)";
  private static final String CREATE_HISTORY = "create table if not exists history " +
          "(type integer, title text UNIQUE, link text, date text, read integer, collect integer)";

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
    ContentValues values = new ContentValues();
    values.put("type", t);
    values.put("title", rssItem.getTitle());
    values.put("link", rssItem.getLink());
    values.put("date", rssItem.getPubDate());
    if(rssItem.haveRead()) {
      values.put("read", 1);
    }
    else values.put("read", 0);
    if(rssItem.judgeCollect()) {
      values.put("collect", 1);
    }
    else values.put("collect", 0);
    db.insertWithOnConflict("history", null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }

  public void updateItem(RSSItem rssItem, int t) {
    ContentValues values = getValues(rssItem, t);
    SQLiteDatabase db = getWritableDatabase();
    db.insertWithOnConflict("history", null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }

  public void collectItem(RSSItem rssItem) {
    SQLiteDatabase db = getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put("type", 10); //10表示收藏
    values.put("title", rssItem.getTitle());
    values.put("link", rssItem.getLink());
    values.put("date", rssItem.getPubDate());
    if(rssItem.haveRead()) {
      values.put("read", 1);
    }
    else values.put("read", 0);
    if(rssItem.judgeCollect()) {
      values.put("collect", 1);
    }
    else values.put("collect", 0);
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
      String date = cursor.getString(3);
      int read = cursor.getInt(4);
      int collect = cursor.getInt(5);
      RSSItem rssItem = new RSSItem(title, link, date);
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
      String date = cursor.getString(3);
      int read = cursor.getInt(4);
      int collect = cursor.getInt(5);
      RSSItem rssItem = new RSSItem(title, link, date);
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

//package wangchen.java.com.cnews.db;
//
//import wangchen.java.com.cnews.RSSItem;
//import java.util.ArrayList;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//import android.widget.Toast;
//
//public class NewsDBHelper extends SQLiteOpenHelper {
//
//  public static final int DB_VERSION = 1;
//  public static final String DB_NAME = "NewsDB.db";
//
//  private static final String CREATECOLLECTIONSTABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.CollectionsColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.CollectionsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.CollectionsColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.CollectionsColumns.DATA + " TEXT)";
//  private static final String DELETECOLLECTIONSTABLE = "drop table if exists " +
//          DataBaseInfo.CollectionsColumns.TABLE_NAME;
//
//
//  private static final String CREATERECOMMANDTABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.RecommandColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.RecommandColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.RecommandColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.RecommandColumns.DATA + " TEXT)";
//  private static final String CREATECHINATABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.ChinaColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.ChinaColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.ChinaColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.ChinaColumns.DATA + " TEXT)";
//  private static final String CREATEEDUTABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.EduColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.EduColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.EduColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.EduColumns.DATA + " TEXT)";
//  private static final String CREATESPORTSTABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.SportsColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.SportsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.SportsColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.SportsColumns.DATA + " TEXT)";
//  private static final String CREATEMOVIETABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.MovieColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.MovieColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.MovieColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.MovieColumns.DATA + " TEXT)";
//  private static final String CREATEFINANCETABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.FinanceColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.FinanceColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.FinanceColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.FinanceColumns.DATA + " TEXT)";
//  private static final String CREATEWEBTABLE =
//          "CREATE TABLE if not exists " + DataBaseInfo.WebColumns.TABLE_NAME + " (" +
//                  DataBaseInfo.WebColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                  DataBaseInfo.WebColumns.ID + " TEXT UNIQUE," +
//                  DataBaseInfo.WebColumns.DATA + " TEXT)";
//
//  public NewsDBHelper(Context context) {
//    super(context, DB_NAME, null, DB_VERSION);
//  }
//
//  @Override
//  public void onCreate(SQLiteDatabase db) {
//    db.execSQL(CREATECOLLECTIONSTABLE);
//    db.execSQL(CREATERECOMMANDTABLE);
//    db.execSQL(CREATEMOVIETABLE);
//    db.execSQL(CREATECHINATABLE);
//    db.execSQL(CREATEFINANCETABLE);
//    db.execSQL(CREATEWEBTABLE);
//    db.execSQL(CREATEEDUTABLE);
//    db.execSQL(CREATESPORTSTABLE);
//  }
//
//  public ArrayList<RSSItem> retrieveCollect() {
//    SQLiteDatabase db = getReadableDatabase();
//    Cursor c = db.rawQuery("SELECT *  FROM " + DataBaseInfo.CollectionsColumns.TABLE_NAME, null);
//
//    ArrayList<RSSItem> rssList = new ArrayList<>();
//    if (c.moveToFirst()){
//      RSSItem s = new RSSItem();
//      c.getString(0);
//      c.getString(1);
//      Log.v("test", c.getString(0));
//      Log.v("test", c.getString(1));
//    }
////    studentDetailsList.add(s);
//    db.close();
//    return rssList;
//  }
//
//  public boolean handleCollect(String newsTitle, String news, boolean status) {
//    SQLiteDatabase db = getWritableDatabase();
//    newsTitle = newsTitle.replace("'", "''");
//    news = news.replace("'", "''"); //将单引号换成两个单引号
//
//    long i = 0;
//    if(status == true) { //收藏新闻
//      ContentValues contentValues = new ContentValues();
//      contentValues.put(DataBaseInfo.CollectionsColumns.ID, newsTitle);
//      contentValues.put(DataBaseInfo.CollectionsColumns.DATA, news);
//      //If conflict happens, just replace
//      i = db.insertWithOnConflict(DataBaseInfo.CollectionsColumns.TABLE_NAME,
//              null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
//    }
//    else {  //取消收藏
//      String[] args = { newsTitle };
//      i = db.delete(DataBaseInfo.CollectionsColumns.TABLE_NAME,
//              DataBaseInfo.CollectionsColumns.ID + " = ?", args);
//    }
//    db.close();
////    while(true) {
////      if(i == -100) break;
////    }
//    if(i == -1) {
//      Log.e("db", "数据库出了点问题");
//      return false;
//    }
//    return true;
//  }
//
//  @Override
//  public void onUpgrade(SQLiteDatabase db, int a, int b) { //Upgrade version
//
//  }
//}