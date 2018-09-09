package wangchen.java.com.cnews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

  ArrayList<TypeName> typeList;

  public ViewPagerAdapter(FragmentManager fm, ArrayList<TypeName> typeList) {
    super(fm);
    this.typeList = typeList;
  }

  @Override
  public Fragment getItem(int position) {
    Fragment fragment = null;
    if (position == 0) {
      fragment = new RecommandFragment();
    }
    else {
      TypeName typeName = typeList.get(position);
      fragment = NewsFragment.newInstance(typeName.getURL(), typeName.getName());
    }
//    else if (position == 2)
//    {
//      fragment = NewsFragment.newInstance("http://ent.qq.com/movie/rss_movie.xml", "互联网");
//    }
//    else if (position == 1)
//    {
//      fragment = NewsFragment.newInstance("http://tech.qq.com/web/rss_web.xml", "科技");
//    }
//    else if(position == 3) {
//      fragment = NewsFragment.newInstance("http://rss.sina.com.cn/news/china/focus15.xml", "新浪");
//    }
    return fragment;
  }

  @Override
  public int getCount() {
    return typeList.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    //String title = null;
    return typeList.get(position).getName();
//    if (position == 0)
//    {
//      title = "推荐";
//    }
//    else if (position == 1)
//    {
//      title = "互联网";
//    }
//    else if (position == 2)
//    {
//      title = "电影";
//    }
//    else if(position == 3) {
//      title = "新浪";
//    }
//    return title;
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

//  @Override
//  public void destroyItem(ViewGroup container, int position, Object object) {}
}