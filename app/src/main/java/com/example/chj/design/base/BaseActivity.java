package com.example.chj.design.base;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.example.chj.design.widget.CustomProgressDialog;

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

    //    private CustomProgressDialog dialogWait = null;
    //图片加载工具
    //protected ImageLoaderUtil mImageLoaderUtil;
    protected Unbinder bind;
    protected CustomProgressDialog progressDialog;
    protected TextView titleText;
    // protected ProgressActivity viewStstus;
    protected FrameLayout container;
    protected LinearLayout baseTitle;
    protected Toolbar toolbar;
    protected BottomNavigationBar bottomBar;
    private DrawerLayout drawerlayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        app = (App) getApplication();
        //mImageLoaderUtil = new ImageLoaderUtil();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        ActivityUtils.addActivity(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initParms(bundle);
        }
        toolbar.setTitle("");
        if (isNeedBottomBar()) {
            bottomBar.setVisibility(View.VISIBLE);
        } else {
            bottomBar.setVisibility(View.GONE);
        }
        onChildCreate(savedInstanceState);
    }

    protected abstract boolean isNeedBottomBar();

    protected abstract void onChildCreate(Bundle savedInstanceState);

    protected void initParms(Bundle bundle) {

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_base, null);
        super.setContentView(view);
        setChildContentView(layoutResID);
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

        }
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        View childView = LayoutInflater.from(this).inflate(layoutResID, null);
        if (container != null) {
            container.removeAllViews();
            container.addView(childView);
        }
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
}
