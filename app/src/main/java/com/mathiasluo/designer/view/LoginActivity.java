package com.mathiasluo.designer.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.app.OAuthUrl;
import com.mathiasluo.designer.presenter.LoginPresenter;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.view.IView.ILoginActivity;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MathiasLuo on 2016/3/4.
 */
public class LoginActivity extends SwipBaseActivity<LoginActivity, LoginPresenter> implements ILoginActivity {

    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter = (LoginPresenter) getPresenter();
        init();

    }

    private void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(new IconicsDrawable(this).icon("gmi_arrow_back").sizeDp(16).color(Color.WHITE));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            String authCode;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.e("现在在============>>> onPageStarted");
                if (url.contains("?code") && url.contains(OAuthUrl.OAUTH_STATE)) {
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    LogUtils.e("onPageStarted================>>>>>>>>>获取到了code\n" + authCode);
                    view.stopLoading();
                    view.setVisibility(View.GONE);
                    view.destroy();
                    //选择动画处理还是直接结束
                    //...............
                    mLoginPresenter.getAccesToken(authCode);

                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.e("现在在============>>> onPageFinished");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                mLoginPresenter.closeLoading();
            }
        });
        mWebView.loadUrl(OAuthUrl.getOAuthLoginUrl(getString(R.string.Client_ID)));
        mLoginPresenter.showLoading();
    }

    @Override
    protected LoginPresenter creatPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeLoading() {
        mProgressBar.setVisibility(View.GONE);
    }
}

