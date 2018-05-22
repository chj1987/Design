package com.example.chj.design.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.example.chj.design.App;
import com.example.chj.design.R;
import com.example.chj.design.utils.ActivityUtils;
import com.example.chj.design.widget.CustomProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ff on 2018/5/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
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
        //setImmerseLayout();
        setTransparentStatusBar(this);
        onChildCreate(savedInstanceState);
    }

    public static void setTransparentStatusBar(Activity activity) {
        //5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            //4.4到5.0
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
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
     * 设置沉浸状态栏
     */
    private void setImmerseLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = app.getStatusBarHeight();
            ViewGroup contentLayout = (ViewGroup) drawerlayout.getChildAt(0);
            contentLayout.getChildAt(1).setPadding(contentLayout.getPaddingLeft(),
                    contentLayout.getPaddingTop() + statusBarHeight,
                    contentLayout.getPaddingRight(),
                    contentLayout.getPaddingBottom());
        }
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

        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        View childView = LayoutInflater.from(this).inflate(layoutResID, null);
        if (container != null) {
            container.removeAllViews();
            container.addView(childView);
        }
    }


    @Override
    protected void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }

    /**
     * 初始化title中间的view
     *
     * @param title
     */
    protected void initBaseTitle(String title) {
        setTitleLayout(R.layout.layout_title);
        titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(title);
        app.setMLayoutParam(titleText, app.TITLE_TEXT_WIDTH, 1f);
        app.setMTextSize(titleText, app.TITLE_STRING_SIZE);
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
}
