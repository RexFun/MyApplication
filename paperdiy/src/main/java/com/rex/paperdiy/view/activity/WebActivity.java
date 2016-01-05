package com.rex.paperdiy.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rex.paperdiy.R;
import com.rexfun.androidlibrarytool.InjectUtil;

public class WebActivity extends AppCompatActivity {
    @InjectUtil.InjectView(id = R.id.web_view) WebView mWebView;
    @InjectUtil.InjectView(id = R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        InjectUtil.injectView(this);
        getSupportActionBar().setHomeButtonEnabled(true);//actionbar主按键可以被点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键

        setTitle("RexFun's GitHub");

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == mProgressBar.getVisibility()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.loadUrl("https://github.com/RexFun");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home://返回
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
