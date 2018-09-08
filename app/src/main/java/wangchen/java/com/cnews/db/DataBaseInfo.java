package wangchen.java.com.cnews.db;

import android.provider.BaseColumns;

public class DataBaseInfo {

  public static final class CollectionsColumns implements BaseColumns {
    public static final String TABLE_NAME = "COLLECTIONS";
    public static final String ID = "NEWS";
    public static final String DATA = "HTML";
  }

  public static final class RecommandColumns implements BaseColumns {  //list that has been read
    public static final String TABLE_NAME = "RECOMMAND";
    public static final String ID = "NEWS";
    public static final String DATA = "CONTENT";
  }

  public static final class ChinaColumns implements BaseColumns {  //list that has been read
    public static final String TABLE_NAME = "CHINA";
    public static final String ID = "NEWS";
    public static final String DATA = "CONTENT";
  }

  public static final class EduColumns implements BaseColumns {  //list that has been read
    public static final String TABLE_NAME = "EDU";
    public static final String ID = "NEWS";
    public static final String DATA = "CONTENT";
  }

  public static final class SportsColumns implements BaseColumns {  //list that has been read
    public static final String TABLE_NAME = "SPORTS";
    public static final String ID = "NEWS";
    public static final String DATA = "CONTENT";
  }

  public static final class MovieColumns implements BaseColumns {  //list that has been read
    public static final String TABLE_NAME = "MOVIE";
    public static final String ID = "NEWS";
    public static final String DATA = "CONTENT";
  }

  public static final class WebColumns implements BaseColumns {  //list that has been read
    public static final String TABLE_NAME = "WEB";
    public static final String ID = "NEWS";
    public static final String DATA = "CONTENT";
  }

  public static final class FinanceColumns implements BaseColumns {  //list that has been read
    public static final String TABLE_NAME = "FINANCE";
    public static final String ID = "NEWS";
    public static final String DATA = "CONTENT";
  }
}