package com.mathiasluo.designer.view;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mathiasluo.designer.R;
import com.mathiasluo.designer.app.OAuthUrl;
import com.mathiasluo.designer.presenter.LoginPresenter;
import com.mathiasluo.designer.utils.LogUtils;
import com.mathiasluo.designer.utils.ToastUtil;
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


    private LoginPresenter mLoginPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter = (LoginPresenter) getPresenter();
        init();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                showLoading();
            }
        });

    }

    @Override
    public void setSupportProgress(int progress) {
        super.setSupportProgress(progress);
    }

    private void init() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(100);
        mProgressDialog.show();
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(new IconicsDrawable(this).icon("gmi_arrow_back").sizeDp(16).color(Color.WHITE));
        mToolbar.setNavigationOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            LoginActivity.this.finish();
        });

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(
                new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        // Activities and WebViews measure progress with different scales.
                        // The progress meter will automatically disappear when we reach 100%
                        LoginActivity.this.setProgress(progress * 1000);
                        mProgressDialog.setProgress(progress);

                        if (progress == 100) {
                            mToolbar.setTitle(R.string.title_activity_login);
                            mProgressDialog.dismiss();
                        }
                    }
                }

        );


        mWebView.setWebViewClient(new WebViewClient() {
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
                    view.stopLoading();
                    view.setVisibility(View.GONE);
                    view.destroy();
                    //选择动画处理还是直接结束
                    //...............
                    mLoginPresenter.getAccesToken(authCode);
                }
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                mLoginPresenter.closeLoading();
                LogUtils.e("现在在============>>> onLoadResource");
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

    }

    @Override
    public void closeLoading() {

    }
}

