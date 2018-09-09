package wangchen.java.com.cnews;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class RSSItem implements Serializable {
  private String title;
  private String author;
  private String link;
  private String description;
  private String pubDate;
  private String guid;
  private String image = "";
  private boolean read = false;
  private boolean isCollected = false;

  public RSSItem() {

  }
  public RSSItem(String title, String link, String pubDate) {
    this.title = title;
    this.link = link;
    this.pubDate = pubDate;
  }

  public String getImage() {
    image = ImageFetcher.FetchImage(link);
    return image;
  }
  public String getTitle() {
    return title;
  }
  public String getAuthor() {
    return author;
  }
  public String getLink() {
    return link;
  }
  public String getDescription() {
    return description;
  }
  public String getPubDate() {
    return pubDate;
  }
  public String getGuid() {
    return guid;
  }
  public boolean haveRead() {
    return read;
  }

  public boolean judgeCollect() {
    return isCollected;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  public void setAuthor(String author) {
    this.author = author;
  }
  public void setLink(String link) {
    this.link = link;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setDate(String date) {
    this.pubDate = date;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public void setRead() {
    read = true;
  }
  public void setUnread() {
    read = false;
  }

  public void setCollected() {
    isCollected = true;
  }
  public void unCollect() {
    isCollected = false;
  }
  public void changeCollect() {
    isCollected = !isCollected;
  }

  public void unRead() {
    read = false;
  }
  public boolean hasImage() {
    if(image.length() > 0) return true;
    return false;
  }

  public String getHTML() {
    String res = "";
    Log.v("url", this.link);
    if(link.substring(0, 4).equals("http")) {
      link = "https" + link.substring(4);
    }
    try {
      URL url = new URL(link);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      String line;
      while((line = reader.readLine()) != null) {
        res += line;
      }
    } catch(MalformedURLException e) {
      Log.e("Geting html", "url malformed");
    } catch(IOException e) {
      Log.e("io", "error");
    }
    return res;
  }

  @Override
  public String toString() { // return json format of the rss item
    int t = 0;
    if(read == true) t = 1;
//    return "{title:" + title + ", description:" + description + ", link:" + link +
//            ", author:" + author + ", read:" + t + ", image:" + image + ", date:" + pubDate + "}";
    return "{title:" + title + ", link:" + link +
            ", author:" + author + ", read:" + t + ", image:" + image + ", date:" + pubDate + "}";
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public boolean equals(Object v) {
    if(v instanceof RSSItem) {
      if(((RSSItem) v).getTitle().equals(this.getTitle())) {
        return true;
      }
      else {
        return false;
      }
    }
    return false;
  }
}