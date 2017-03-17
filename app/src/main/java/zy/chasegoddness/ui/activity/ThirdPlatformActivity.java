package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import zy.chasegoddness.R;
import zy.chasegoddness.presenter.ThirdPartyPresenter;
import zy.chasegoddness.ui.activity.iactivity.IThirdPlatformView;
import zy.chasegoddness.ui.view.IconButton;
import zy.chasegoddness.ui.view.SideMenu;

public class ThirdPlatformActivity extends BaseActivity implements IThirdPlatformView {

    private WebView webView;
    private IconButton btn_back, btn_forward, btn_refresh, btn_menu;
    private ImageView iv_bg;
    private SideMenu menu;
    private ThirdPartyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_platform);

        initPresenter();
        initView();
        initWeb();
        presenter.init();
    }

    private void initPresenter() {
        presenter = new ThirdPartyPresenter(this);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview_third_party);
        btn_back = (IconButton) findViewById(R.id.btn_third_party_back);
        btn_forward = (IconButton) findViewById(R.id.btn_third_party_forward);
        btn_refresh = (IconButton) findViewById(R.id.btn_third_party_refresh);
        btn_menu = (IconButton) findViewById(R.id.btn_third_party_menu);
        iv_bg = (ImageView) findViewById(R.id.iv_third_party_bg);
        menu = (SideMenu) findViewById(R.id.sm_third_party);

        btn_forward.setOnClickListener(v -> {
            if (isWebViewEnable)
                webView.goForward();
        });
        btn_back.setOnClickListener(v -> {
            if (isWebViewEnable)
                webView.goBack();
        });
        btn_refresh.setOnClickListener(v -> {
            if (isWebViewEnable)
                webView.reload();
        });
        btn_menu.setOnClickListener(v -> {
            if (menu.isOpen()) {
                menu.closeMenu();
            } else {
                menu.openMenu();
            }
        });
        menu.setSideMenuResourse(R.layout.item_third_party);
        menu.setOnClickMenuItemListener((v, postion) -> {
            setWebViewEnable(true);
            switch (postion) {
                case 0:
                    webView.loadUrl("http://m.weibo.cn");
                    break;
                case 1:
                    webView.loadUrl("http://matter.renren.com");
                    break;
                case 2:
                    webView.loadUrl("http://qzone.qq.com");
                    break;
                default:
                    break;
            }
            setMenuOpen(false);
        });
    }

    private void initWeb() {
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isWebViewEnable) {
                setWebViewEnable(false);
                menu.openMenu();
                return true;
            } else if (menu.isOpen()) {
                menu.closeMenu();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ThirdPlatformActivity.class));
    }

    private boolean isWebViewEnable;

    @Override
    public void setWebViewEnable(boolean enable) {
        isWebViewEnable = enable;
        if (enable) {
            webView.setVisibility(View.VISIBLE);
            iv_bg.setVisibility(View.INVISIBLE);
        } else {
            webView.setVisibility(View.INVISIBLE);
            iv_bg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setMenuOpen(boolean isOpen) {
        if (isOpen && !menu.isOpen()) {
            menu.openMenu();
        }
        if (!isOpen && menu.isOpen()) {
            menu.closeMenu();
        }
    }
}
