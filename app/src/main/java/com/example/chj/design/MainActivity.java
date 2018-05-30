package com.example.chj.design;

import android.os.Bundle;

import com.example.chj.design.base.BaseActivity;
import com.example.chj.design.model.source.Remote.RemoteResponsitoryImpl;

public class MainActivity extends BaseActivity {

    private RemoteResponsitoryImpl instance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isNeedBottomBar() {
        return false;
    }

    @Override
    protected void onChildCreate(Bundle savedInstanceState) {
        initToolBar("首页");
        initFloatingActionButton();
        initDrawer();

    }
}
