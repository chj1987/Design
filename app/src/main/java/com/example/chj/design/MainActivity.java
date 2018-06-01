package com.example.chj.design;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.chj.design.base.BaseActivity;
import com.example.chj.design.model.DataRespositoryImpl;
import com.example.chj.design.model.source.IDataSource;
import com.example.chj.design.model.source.Remote.RemoteResponsitoryImpl;
import com.example.chj.design.video.VideoFragment;
import com.example.chj.design.video.VideoPresenter;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.container)
    FrameLayout container;
    private VideoFragment videoFragment;
    private VideoPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        initToolBar("首页");
        initFloatingActionButton();
        initDrawer();
    }

    @Override
    protected boolean isNeedBottomBar() {
        return false;
    }

    @Override
    protected void onChildCreate(Bundle savedInstanceState) {
        if (videoFragment == null) {
            videoFragment = VideoFragment.newInstance();
        }
        IDataSource dataSource = RemoteResponsitoryImpl.getInstance();
        presenter = new VideoPresenter(videoFragment, DataRespositoryImpl.getInstance(dataSource, dataSource));

        addFragment(R.id.container, videoFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }
}
