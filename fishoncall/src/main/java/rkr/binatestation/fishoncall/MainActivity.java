package rkr.binatestation.fishoncall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nguyenhoanglam.progresslayout.ProgressLayout;

public class MainActivity extends AppCompatActivity {
    public static final String URL = "http://fishoncall.in/";
    private WebView mWebView;
    private ProgressLayout progressLayout;
    private boolean isError = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressLayout = (ProgressLayout) findViewById(R.id.activity_main);
        mWebView = (WebView) findViewById(R.id.AM_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);        // enable javascript just test

        mWebView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showNetworkError();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressLayout != null && !isError) {
                    progressLayout.showContent();
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (url.equalsIgnoreCase(URL) && progressLayout != null) {
                    progressLayout.showLoading();
                }
            }
        });

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.loadUrl(URL);

    }


    public void showNetworkError() {
        isError = true;
        progressLayout.showError(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_signal_wifi_off_black_24dp),
                getString(R.string.no_connection),
                getString(R.string.retry),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mWebView != null) {
                            isError = false;
                            mWebView.reload();
                        }
                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public Context getContext() {
        return MainActivity.this;
    }
}
