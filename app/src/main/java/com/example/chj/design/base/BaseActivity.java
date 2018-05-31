package com.example.chj.design.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.example.chj.design.App;
import com.example.chj.design.R;
import com.example.chj.design.utils.ActivityUtils;
import com.example.chj.design.utils.PermissionListener;
import com.example.chj.design.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by ff on 2018/5/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int CODE_REQUEST_PERMISSION = 1;
    private static PermissionListener mPermissionListener;
    protected App app;

    //图片加载工具
    //protected ImageLoaderUtil mImageLoaderUtil;
    protected Unbinder bind;
    protected TextView titleText;
    // protected ProgressActivity viewStstus;
    protected FrameLayout container;
    protected LinearLayout baseTitle;
    protected Toolbar toolbar;
    protected BottomNavigationBar bottomBar;
    protected FloatingActionButton mFloatingActionButton;
    protected LinearLayout mMenuLl;
    private DrawerLayout mRootDl;
    private ActionBarDrawerToggle toggle;
    protected ProgressDialog dialogWait;
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        app = (App) getApplication();
        //mImageLoaderUtil = new ImageLoaderUtil();
        super.onCreate(savedInstanceState);

        if (initBundle(getIntent().getExtras())) {
            setContentView(getLayoutId());
            bind = ButterKnife.bind(this);
            ActivityUtils.addActivity(this);

            initWindow();

            initWidget();
            toolbar.setTitle("");
            if (isNeedBottomBar()) {
                bottomBar.setVisibility(View.VISIBLE);
            } else {
                bottomBar.setVisibility(View.GONE);
            }
            onChildCreate(savedInstanceState);
        } else {
            finish();
        }
    }

    private void initWidget() {

    }

    private void initWindow() {

    }

    private boolean initBundle(Bundle extras) {
        return true;
    }

    protected abstract boolean isNeedBottomBar();

    protected abstract void onChildCreate(Bundle savedInstanceState);


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_base, null);
        super.setContentView(view);
        setChildContentView(layoutResID);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    /**
     * 加载child布局
     *
     * @param layoutResID
     */
    private void setChildContentView(int layoutResID) {
        // viewStstus = (ProgressActivity) findViewById(R.id.view_status);
        bottomBar = (BottomNavigationBar) findViewById(R.id.bottombar);
        container = (FrameLayout) findViewById(R.id.fl_activity_child_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);

        }
        View childView = LayoutInflater.from(this).inflate(layoutResID, null);
        if (container != null) {
            container.removeAllViews();
            container.addView(childView);
        }

        dialogWait = new ProgressDialog(this);
        dialogWait.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up actionshould open or close the drawer.
        // ActionBarDrawerToggle will takecare of this.
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initDrawer() {
        mRootDl = (DrawerLayout) findViewById(R.id.dl_root);
        mMenuLl = (LinearLayout) findViewById(R.id.ll_menu);
        toggle = new ActionBarDrawerToggle(this, mRootDl, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mRootDl.addDrawerListener(toggle);

        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) mMenuLl.getLayoutParams();
        layoutParams.width = getScreenSize()[0] / 4 * 3;
    }

    public int[] getScreenSize() {
        int screenSize[] = new int[2];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenSize[0] = displayMetrics.widthPixels;
        screenSize[1] = displayMetrics.heightPixels;
        return screenSize;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        ActivityUtils.removeActivity(this);
    }

    /**
     * 设置标题bar
     *
     * @param resId title ID
     */
    private void setTitleLayout(int resId) {
        if (resId == -1) {
            baseTitle.removeAllViews();
        }
        app.setMLayoutParam(baseTitle, 1f, app.TITLE_HEIGHT);
        View view = LayoutInflater.from(this).inflate(resId, null);
        if (baseTitle != null) {
            baseTitle.removeAllViews();
            baseTitle.addView(view);
        }
    }

    protected abstract int getLayoutId();

    /**
     * 初始化title中间的view
     *
     * @param title
     */
    protected void initToolBar(String title) {
        titleText = (TextView) findViewById(R.id.tv_title);
        titleText.setText(title);
        app.setMTextSize(titleText, app.TITLE_STRING_SIZE);
    }

    /**
     * 初始化title中间的view
     *
     * @param
     */
    protected void initFloatingActionButton() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);
        mFloatingActionButton.setVisibility(View.VISIBLE);
        app.setMViewMargin(mFloatingActionButton, 0, 0, 0.02f, 0.15f);
    }

    public static void requestPermissions(String[] permissions, PermissionListener listener) {
        Activity activity = ActivityUtils.getTopActivity();
        Preconditions.checkNotNull(activity);
        if (activity == null) {
            return;
        }
        mPermissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            //权限没有授权
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionList.toArray(new String[permissionList.size()]),
                    CODE_REQUEST_PERMISSION);
        } else {
            mPermissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int result = grantResults[i];
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onGranted(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * @param frameLayoutID
     * @param fragment
     */
    protected void addFragment(int frameLayoutID, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.add(frameLayoutID, fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.add(frameLayoutID, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        } else {
            new NullPointerException("fragment is not null");
        }
    }

    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayoutId, fragment);
        transaction.commit();
    }
}
