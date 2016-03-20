package com.mathiasluo.designer.view.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.app.OAuthUrl;
import com.mathiasluo.designer.presenter.LoginPresenter;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.utils.ToastUtil;
import com.mathiasluo.designer.view.IView.ILoginActivity;
import com.mathiasluo.designer.view.widget.ProgressWeb;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MathiasLuo on 2016/3/4.
 */
public class LoginActivity extends SwipBaseActivity<LoginActivity, LoginPresenter> implements ILoginActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.webView)
    ProgressWeb webView;


    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //   requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter = (LoginPresenter) getPresenter();
        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(new IconicsDrawable(this).icon("gmi_arrow_back").sizeDp(16).color(Color.WHITE));
        mToolbar.setNavigationOnClickListener(v -> {
            ToastUtil.Toast(getString(R.string.login_failed));
            setResult(RESULT_CANCELED);
            LoginActivity.this.finish();
        });

        webView.getWebView().setWebViewClient(new WebViewClient() {
            String authCode;
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                ToastUtil.Toast("登录失败请稍后重试\n" + error.toString());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains("?code") && url.contains(OAuthUrl.OAUTH_STATE)) {
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    LogUtils.e("onPageStarted================>>>>>>>>>获取到了code\n" + authCode);
                    //选择动画处理还是直接结束
                    ToastUtil.Toast("正在登陆,请稍后...");
                    webView.getWebView().setVisibility(View.GONE);
                    webView.setRefreshing(true);
                    mLoginPresenter.getAccesToken(authCode);
                }
            }
        });
        webView.loadUrl(OAuthUrl.getOAuthLoginUrl(getString(R.string.Client_ID)));

      /*  mWebView.setWebViewClient(new WebViewClient() {
            String authCode;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                ToastUtil.Toast("登录失败请稍后重试\n" + error.toString());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.e("现在在============>>> onPageStarted");
                if (url.contains("?code") && url.contains(OAuthUrl.OAUTH_STATE)) {
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    LogUtils.e("onPageStarted================>>>>>>>>>获取到了code\n" + authCode);
                    //选择动画处理还是直接结束
                    mLoginPresenter.getAccesToken(authCode);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                LogUtils.e("现在在============>>> onLoadResource");
            }
        });
        mWebView.loadUrl(OAuthUrl.getOAuthLoginUrl(getString(R.string.Client_ID)));*/
    }

    @Override
    protected LoginPresenter creatPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }
}

