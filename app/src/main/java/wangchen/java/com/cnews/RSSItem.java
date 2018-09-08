package wangchen.java.com.cnews;

import java.io.Serializable;

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
  public void setCollected() {
    isCollected = true;
  }
  public void unCollect() {
    isCollected = false;
  }
  public void unRead() {
    read = false;
  }
  public boolean hasImage() {
    if(image.length() > 0) return true;
    return false;
  }
  @Override
  public String toString() { // return json format of the rss item
    return "{title:" + title + ", description:" + description + ", link:" + link + ", author:" + author + ", image:" + image + ", date:" + pubDate + "}";
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public boolean equals(Object v) {
    boolean res = false;
    if(v instanceof RSSItem) {
      RSSItem ptr = (RSSItem)v;
      res = this.toString().equals(ptr.toString());
    }
    return res;
  }
}