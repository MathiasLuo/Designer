package com.mathiasluo.designer.view.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * Created by MathiasLuo on 2016/3/9.
 */
public class ProgressWeb extends SwipeRefreshLayout {
    WebView mWebView;

    public ProgressWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mWebView = new WebView(context);
        addView(mWebView);
        initWebView();
    }

    public void initWebView() {
        setWebChromeClient(new ProgressWebChomeClient(this));
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            mWebView.loadUrl(mWebView.getUrl());
            }
        });

    }

    public WebView getWebView() {
        return mWebView;
    }

    public void setWebChromeClient(WebChromeClient wenChomeClient) {
        mWebView.setWebChromeClient(wenChomeClient);
    }

    public void setWebClient(WebViewClient webClient) {
        mWebView.setWebViewClient(webClient);
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }
    public class ProgressWebChomeClient extends WebChromeClient {
        ProgressWeb mProgressWeb;

        public ProgressWebChomeClient(ProgressWeb mProgressWeb) {
            this.mProgressWeb = mProgressWeb;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressWeb.setRefreshing(false);
            } else if (!mProgressWeb.isRefreshing()) mProgressWeb.setRefreshing(true);

            super.onProgressChanged(view, newProgress);
        }
    }
}
