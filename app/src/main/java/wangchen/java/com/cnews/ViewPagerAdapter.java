package wangchen.java.com.cnews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.HashMap;

public class ViewPagerAdapter extends FragmentPagerAdapter {

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    Fragment fragment = null;
    if (position == 0)
    {
      fragment = NewsFragment.newInstance("http://news.qq.com/newsgn/rss_newsgn.xml");
    }
    else if (position == 2)
    {
      fragment = NewsFragment.newInstance("http://ent.qq.com/movie/rss_movie.xml");
    }
    else if (position == 1)
    {
      fragment = NewsFragment.newInstance("http://tech.qq.com/web/rss_web.xml");
    }
    else if(position == 3) {
      fragment = NewsFragment.newInstance("http://rss.sina.com.cn/news/china/focus15.xml");
    }
    return fragment;
  }

  @Override
  public int getCount() {
    return 4;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    String title = null;
    if (position == 0)
    {
      title = "国内新闻";
    }
    else if (position == 1)
    {
      title = "互联网";
    }
    else if (position == 2)
    {
      title = "电影";
    }
    else if(position == 3) {
      title = "新浪";
    }
    return title;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {}
}