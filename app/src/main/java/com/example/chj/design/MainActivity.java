package com.example.chj.design;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.chj.design.base.BaseActivity;
import com.example.chj.design.home.HomeFragment;
import com.example.chj.design.http.API;
import com.example.chj.design.model.DataRespositoryImpl;
import com.example.chj.design.model.source.IDataSource;
import com.example.chj.design.model.source.Remote.RemoteResponsitoryImpl;
import com.example.chj.design.my.MyFragment;
import com.example.chj.design.shop.ShopingFragment;
import com.example.chj.design.video.VideoFragment;
import com.example.chj.design.video.VideoPresenter;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {


    @BindView(R.id.container)
    FrameLayout container;
    private VideoFragment videoFragment;
    private VideoPresenter presenter;
    private HomeFragment mHomeFragnebt;
    private ShopingFragment mShopingFragment;
    private MyFragment mMyFragment;

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
        return true;
    }

    @Override
    protected void onChildCreate(Bundle savedInstanceState) {

        initFragment();
    }

    private void initFragment() {
        bottomBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_white_24dp, "主页"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_message_white_24dp, "消息"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_discover_white_24dp, "其他"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_account_circle_white_24dp, "我的"))
                .setFirstSelectedPosition(1)
                .initialise();

        bottomBar.setTabSelectedListener(this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#2B2B2B")
                .setInActiveColor("#ADADAD")
                .setBarBackgroundColor("#FCFCFC");

        setdefaultFragment();
    }

    private void setdefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (videoFragment == null) {
            videoFragment = VideoFragment.newInstance();
        }

        IDataSource dataSource = new RemoteResponsitoryImpl(API.BASE_URL);
        presenter = new VideoPresenter(videoFragment, DataRespositoryImpl.getInstance(dataSource, dataSource));
        addFragment(R.id.container, videoFragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (mHomeFragnebt == null) {
                    mHomeFragnebt = HomeFragment.newInstance();
                }
                transaction.replace(R.id.container, mHomeFragnebt);
                break;
            case 1:
                if (videoFragment == null) {
                    videoFragment = videoFragment.newInstance();
                }
                transaction.replace(R.id.container, videoFragment);
                break;
            case 2:
                if (mShopingFragment == null) {
                    mShopingFragment = ShopingFragment.newInstance();
                }
                transaction.replace(R.id.container, mShopingFragment);
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance();
                }
                transaction.replace(R.id.container, mMyFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
