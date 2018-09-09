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
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

//  @Override
//  public void destroyItem(ViewGroup container, int position, Object object) {}
}