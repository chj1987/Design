package com.example.chj.design;

import android.os.Bundle;

import com.example.chj.design.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected boolean isNeedBottomBar() {
        return true;
    }

    @Override
    protected void onChildCreate(Bundle savedInstanceState) {
        initToolBar("首页");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
