package wangchen.java.com.cnews;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class ViewHolder {
  ImageView imageView;
  TextView titleView;
  TextView dateView;
}

public class RSSAdapter extends ArrayAdapter<RSSItem> {
  private final RSSItem[] rssList;
  public RSSAdapter(Context context, int resource, RSSItem[] objects) {
    super(context, resource, objects);
    rssList = objects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if(convertView == null) {
      LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
      View myView = inflater.inflate(R.layout.newsitem, null);
      viewHolder = new ViewHolder();
      viewHolder.imageView = convertView.findViewById(R.id.newspic);
      viewHolder.titleView = convertView.findViewById(R.id.newstitle);
      viewHolder.dateView = convertView.findViewById(R.id.newsdate);
    } else {
      viewHolder = (ViewHolder)convertView.getTag();
    }

    viewHolder.imageView.setImageResource(R.drawable.apple);

    viewHolder.titleView.setText(rssList[position].getTitle());

    viewHolder.dateView.setText(rssList[position].getPubDate());

    return convertView;
  }
}
