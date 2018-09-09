package wangchen.java.com.cnews;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import java.net.URLConnection;
import java.awt.image.BufferedImage;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageFetcher {
  public static void main(String[] args) {
    System.out.println(FetchImage("http://ent.qq.com/a/20180906/002144.htm"));
  }
  public static String FetchImage(String url) {
    String html = getHTML(url);
    if(html.length() <= 0) return "";
    Matcher matcher = Pattern.compile("(?<=<img src=\")[0-9a-zA-Z\\./_]*?(jpg|png)(?=\")").matcher(html);
    int maxsize = 0;
    String res = "";
    while(matcher.find()) {
      String tep = "http:" + matcher.group();
      //System.out.println(tep);
      try{
//        URL imageUrl = new URL(tep);
//        URLConnection connection = imageUrl.openConnection();
//        connection.setDoOutput(true);
//        BufferedImage image = ImageIO.read(connection.getInputStream());
//        int srcWidth = image.getWidth();      // 源图宽度
//        int srcHeight = image.getHeight();    // 源图高度
//        int size = srcWidth * srcHeight;
        URL imageURL = new URL(tep);
        Bitmap imageBitmap = BitmapFactory.decodeStream(imageURL.openStream());
        int size = imageBitmap.getWidth() * imageBitmap.getHeight();
        if(size > maxsize) {
          res = tep;
          //System.out.println(tep);
          maxsize = size;
        }
      }
      catch(Exception e) {
        Log.v("image", "open failed");}
    }
    Log.v("image", res);
    return res;
  }
  private static String getHTML(String url) {
    String res = "";
    try {
      URL u = new URL(url);
      InputStream in = u.openStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
      String line;
      while((line = bufferedReader.readLine()) != null) {
        res += line;
      }
    } catch(Exception e) { e.printStackTrace(); }
    return res;
  }
}