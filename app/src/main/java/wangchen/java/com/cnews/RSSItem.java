package wangchen.java.com.cnews;

import java.util.Comparator;

public class RSSItem {
  private String title;
  private String author;
  private String link;
  private String description;
  private String pubDate;
  private String guid;

  public RSSItem() {

  }
  public RSSItem(String title, String link, String pubDate) {
    this.title = title;
    this.link = link;
    this.pubDate = pubDate;
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
  @Override
  public String toString() { // return json format of the rss item
    return "{title:" + title + ", description:" + description + ", link:" + link + ", author:" + author + ", date:" + pubDate + "}";
  }
}