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
import java.io.IOException;
import java.net.URL;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RSSAdapter extends ArrayAdapter<RSSItem> {
  private final ArrayList<RSSItem> rssList;

  public RSSAdapter(Context context, int resource, ArrayList<RSSItem> objects) {
    super(context, resource, objects);
    rssList = objects;
  }

  private class ViewHolder {
    ImageView imageView;
    TextView urlView;
    TextView titleView;
    TextView dateView;
    Bitmap bitmap;
    RSSItem item;
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
      viewHolder.urlView = (TextView) convertView.findViewById(R.id.newslink);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //viewHolder.imageUrl = rssList.get(position).getImage();
    //viewHolder.imageUrl = "http://farm8.staticflickr.com/7315/9046944633_881f24c4fa_s.jpg";
//    if(viewHolder.imageUrl.length() > 0) {
//      Bitmap imageBitmap = null;
//      try {
//        URL imageURL = new URL(rssList.get(position).getImage());
//        imageBitmap = BitmapFactory.decodeStream(imageURL.openStream());
//        viewHolder.imageView.setImageBitmap(imageBitmap);
//        Log.v("image", "downloading successful");
//      } catch (Exception e) {
//        Log.e("error", "Downloading Image Failed");
//        viewHolder.imageView.setImageResource(R.drawable.noimages);
//      }
//    }
//    else {
//      viewHolder.imageView.setImageResource(R.drawable.noimages);
//    }
    viewHolder.titleView.setText(rssList.get(position).getTitle());
    viewHolder.dateView.setText(rssList.get(position).getPubDate());
    viewHolder.urlView.setText(rssList.get(position).getLink());
    //String link = rssList.get(position).getLink();
    //viewHolder.link = link;
    viewHolder.item = rssList.get(position);

    //new DownloadAsyncTask().execute(viewHolder);
    return convertView;
  }

  private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

    @Override
    protected ViewHolder doInBackground(ViewHolder... params) {
      ViewHolder viewHolder = params[0];
      String imageUrl = viewHolder.item.getImage();

      //load image directly
      try {
        URL imageURL = new URL(imageUrl);
        viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
      } catch (IOException e) {
        Log.e("error", "Downloading Image Failed");
        viewHolder.bitmap = null;
      }

      return viewHolder;
    }

    @Override
    protected void onPostExecute(ViewHolder result) {
      // TODO Auto-generated method stub
      if (result.bitmap == null) {
        result.imageView.setImageResource(R.drawable.noimages);
      } else {
        result.imageView.setImageBitmap(result.bitmap);
      }
    }
  }
}
//package wangchen.java.com.cnews;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.LayoutInflater;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class RSSAdapter extends ArrayAdapter<RSSItem> {
//  private final ArrayList<RSSItem> rssList;
//
//  public RSSAdapter(Context context, int resource, ArrayList<RSSItem> objects) {
//    super(context, resource, objects);
//    rssList = objects;
//  }
//
//  private class ViewHolder {
//    ImageView imageView;
//    TextView urlView;
//    TextView titleView;
//    TextView dateView;
//  }
//
//  @Override
//  public View getView(int position, View convertView, ViewGroup parent) {
//    ViewHolder viewHolder;
//
//    if (convertView == null) {
//      LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
//      convertView = inflater.inflate(R.layout.newsitem, null);
//
//      viewHolder = new ViewHolder();
//      viewHolder.imageView = (ImageView) convertView
//              .findViewById(R.id.newspic);
//      viewHolder.titleView = (TextView) convertView
//              .findViewById(R.id.newstitle);
//      viewHolder.dateView = (TextView) convertView
//              .findViewById(R.id.newsdate);
//      viewHolder.urlView = (TextView) convertView.findViewById(R.id.newslink);
//      convertView.setTag(viewHolder);
//    } else {
//      viewHolder = (ViewHolder) convertView.getTag();
//    }
//
//    viewHolder.imageView.setImageResource(R.drawable.ic_launcher_background);
//    viewHolder.titleView.setText(rssList.get(position).getTitle());
//    viewHolder.dateView.setText(rssList.get(position).getPubDate());
//    viewHolder.urlView.setText(rssList.get(position).getLink());
//    return convertView;
//  }
//}
