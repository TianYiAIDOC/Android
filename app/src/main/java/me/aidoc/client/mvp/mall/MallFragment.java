package me.aidoc.client.mvp.mall;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.aidoc.client.R;
import me.aidoc.client.base.BaseFragment;
import me.aidoc.client.base.frame.BaseView;
import me.aidoc.client.util.L;
import ren.yale.android.cachewebviewlib.CacheWebView;


public class MallFragment extends BaseFragment<MallPresenter> implements MallContract.View {
    View view;
    ImmersionBar mImmersionBar;
    Unbinder unbinder;
    @BindView(R.id.webView)
    CacheWebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_store, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initWebView();
        mPresenter.getMallUrl();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initImmersionBar();
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.colorPrimary).statusBarDarkFont(true);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getMallUrlSuccess(String url) {
        if (!TextUtils.isEmpty(url))
            mWebView.loadUrl("" + url);
    }

    @Override
    public void getMallUrlErro(String msg) {
        L.i(msg);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        //方法体略去，要继承实现必须实现的方法。
    }

    private void initWebView() {
        // 设置WebView WebSetting 参数
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebView.setCacheStrategy(WebViewCache.CacheStrategy.NORMAL);//强制缓存
        mWebView.setEnableCache(false);
//		webView.addJavascriptInterface(new JSHook(), "android");

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

        });

        // 设置WebView js事件
        mWebView.setWebChromeClient(new WebChromeClient());
    }

}
