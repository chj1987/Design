package com.example.chj.design;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.chj.design.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.btn_success)
    Button btnSuccess;
    @BindView(R.id.btn_failure)
    Button btnFailure;
    @BindView(R.id.btn_error)
    Button btnError;

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
