package wangchen.java.com.cnews;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class ViewHolder {
  ImageView imageView;
  TextView titleView;
  TextView dateView;
}

public class RSSAdapter extends ArrayAdapter<RSSItem> {
  private final ArrayList<RSSItem> rssList;

  public RSSAdapter(Context context, int resource, ArrayList<RSSItem> objects) {
    super(context, resource, objects);
    rssList = objects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;

    if (convertView == null) {
      LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
      convertView = inflater.inflate(R.layout.newsitem, null);

      viewHolder = new ViewHolder();
      viewHolder.imageView = (ImageView) convertView
              .findViewById(R.id.newspic);
      viewHolder.titleView = (TextView) convertView
              .findViewById(R.id.newstitle);
      viewHolder.dateView = (TextView) convertView
              .findViewById(R.id.newsdate);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.imageView.setImageResource(R.drawable.apple);
    viewHolder.titleView.setText(rssList.get(position).getTitle());
    viewHolder.dateView.setText(rssList.get(position).getPubDate());

    return convertView;
  }
//  public View getView(int position, View convertView, ViewGroup parent) {
//    View rowView;
//
//    if(convertView == null) {
//      LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
//      convertView = inflater.inflate(R.layout.newsitem, null);
//    }
//
//    rowView = convertView;
//    ImageView thumbImageView = (ImageView) rowView
//            .findViewById(R.id.newspic);
//
//      thumbImageView.setImageResource(R.drawable.apple);
//
//    TextView postTitleView = (TextView) rowView
//            .findViewById(R.id.newstitle);
//    postTitleView.setText(rssList.get(position).getTitle());
//
//    TextView postDateView = (TextView) rowView
//            .findViewById(R.id.newsdate);
//    postDateView.setText(rssList.get(position).getPubDate());
//
//    return rowView;
//  }
}
