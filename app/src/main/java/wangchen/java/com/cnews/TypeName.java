package wangchen.java.com.cnews;

public enum TypeName {

  RECOMMAND("推荐", "http://news.qq.com"),
  China("国内新闻", "http://news.qq.com/newsgn/rss_newsgn.xml"),
  EDU("教育", "http://edu.qq.com/gaokao/rss_gaokao.xml"),
  SPORTS("体育", "http://sports.qq.com/rss_newssports.xml"),
  MOVIE("电影", "http://ent.qq.com/movie/rss_movie.xml"),
  WEB("互联网", "http://tech.qq.com/web/rss_web.xml"),
  FINANCE("财经", "http://finance.qq.com/scroll/rss_scroll.xml");

  private String name;
  private String url;

  TypeName(String name, String url) {
    this.name = name;
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public String getURL() {
    return url;
  }

  public static TypeName getType(String name) {
    if(name.equals("推荐")) return RECOMMAND;
    else if(name.equals("国内新闻")) return China;
    else if(name.equals("教育")) return EDU;
    else if(name.equals("体育")) return SPORTS;
    else if(name.equals("电影")) return MOVIE;
    else if(name.equals("互联网")) return WEB;
    else if(name.equals("财经")) return FINANCE;
    return RECOMMAND;
  }

  public static int getTypeInt(String name) {
    if(name.equals("推荐")) return 0;
    else if(name.equals("国内新闻")) return 1;
    else if(name.equals("教育")) return 2;
    else if(name.equals("体育")) return 3;
    else if(name.equals("电影")) return 4;
    else if(name.equals("互联网")) return 5;
    else if(name.equals("财经")) return 6;
    return 0;
  }

  @Override
  public String toString() {
    return name + " " + url;
  }
}