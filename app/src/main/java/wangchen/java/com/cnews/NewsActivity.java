package wangchen.java.com.cnews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.WebSettings;

public class NewsActivity extends AppCompatActivity {

  private WebView webView;
  private String url;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_newsitem);
    Intent in = getIntent();
    url = in.getStringExtra("LINK");
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

