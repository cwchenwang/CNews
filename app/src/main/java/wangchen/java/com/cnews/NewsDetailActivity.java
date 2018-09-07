package wangchen.java.com.cnews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.WebSettings;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.view.View;
import android.view.MenuItem;
import android.view.Menu;

public class NewsDetailActivity extends AppCompatActivity {

  private WebView webView;
  private String url;
  private RSSItem rssItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_detail);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news_detail);
    setSupportActionBar(toolbar);
//    ImageButton imageButton = findViewById(R.id.finishButton);
//
//    imageButton.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        finish();
//      }
//    });

//    Toolbar mToolbarTb = findViewById(R.id.toolbar);
//    mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v){
//      finish();
//    }
//    });

    Intent in = getIntent();
    rssItem = (RSSItem)in.getSerializableExtra("RSSITEM");
    url = rssItem.getLink();

    if (TextUtils.isEmpty(url)) {
      Toast.makeText(getApplicationContext(), "URL ERROR", Toast.LENGTH_SHORT).show();
      finish();
    }
    else {
      webView = findViewById(R.id.webView);
      initWebView();
      webView.loadUrl(url);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_newsdetail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case R.id.backup:
        finish();
        break;
      case R.id.share:
        share();
        break;
//
//      case R.id.keep:
//
//        mUserKeep();
//
//        break;
//
//      case R.id.photo:
//
//        takeScreenShot(this);
//
//        Intent intent=new Intent(this,ScreenCutActivity.class);
//        startActivity(intent);
//
//        break;
//
//      case R.id.text_size:
//
//        textSizeDialog();
//
//        break;

      default:
        break;
    }
    return true;
  }

  private void share() {
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, rssItem.getTitle() + "\n" + rssItem.getLink());
    startActivity(Intent.createChooser(shareIntent, "选择分享的应用"));
  }

  private void initWebView() {
    WebSettings settings = webView.getSettings();
    settings.setDomStorageEnabled(true);
    settings.setJavaScriptEnabled(true);
    webView.clearCache(true);
    webView.setHorizontalScrollBarEnabled(false);

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
      }

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        webView.loadUrl(url);
        return true;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
      }

      @Override
      public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        invalidateOptionsMenu();
      }
    });
  }
}

