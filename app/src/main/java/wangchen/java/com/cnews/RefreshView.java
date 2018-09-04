package wangchen.java.com.cnews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RefreshView extends ListView implements OnScrollListener {

  private LayoutInflater layoutInflater;
  private RelativeLayout headerView;

  public RefreshView(Context context, AttributeSet attr) {
    super(context, attr);
    initView(context);
  }
  private void initView(Context context) {
    layoutInflater = LayoutInflater.from(context);
    headerView = (RelativeLayout)layoutInflater.inflate(R.layout.refresh_header, null);
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

  }
}
