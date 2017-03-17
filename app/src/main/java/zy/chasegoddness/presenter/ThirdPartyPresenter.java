package zy.chasegoddness.presenter;

import zy.chasegoddness.ui.activity.iactivity.IThirdPlatformView;

/**
 * Created by Administrator on 2016/11/7.
 */

public class ThirdPartyPresenter {
    private IThirdPlatformView view;

    public ThirdPartyPresenter(IThirdPlatformView view) {
        this.view = view;
    }

    public void init(){
        view.setWebViewEnable(false);
        view.setMenuOpen(true);
    }
}
