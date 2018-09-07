package wangchen.java.com.cnews;

import java.util.HashMap;
public final class Constants {
  public static final HashMap<String, String> NEWSCATEGORY = new HashMap<String, String>() {{
      NEWSCATEGORY.put("国内新闻", "http://news.qq.com/newsgn/rss_newsgn.xml");
      NEWSCATEGORY.put("教育", "http://edu.qq.com/gaokao/rss_gaokao.xml");
      NEWSCATEGORY.put("体育", "http://sports.qq.com/rss_newssports.xml");
      NEWSCATEGORY.put("电影", "http://ent.qq.com/movie/rss_movie.xml");
      NEWSCATEGORY.put("互联网", "http://tech.qq.com/web/rss_web.xml");
      NEWSCATEGORY.put("财经", "http://finance.qq.com/scroll/rss_scroll.xml");
    }};
}
