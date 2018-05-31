package com.example.chj.design.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chj on 2018/5/31.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected View mRoot;
    protected Bundle mBundle;
    protected LayoutInflater mInflater;
    private Unbinder mBind;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null) {
                parent.removeView(mRoot);
            }
        } else {
            mRoot = inflater.inflate(getLayoutID(), container, false);
            mInflater = inflater;

            onBandViewBefore(mRoot);

            mBind = ButterKnife.bind(this, mRoot);

            if (savedInstanceState != null) {
                onRestartaInstance(savedInstanceState);
            }
            initWidget(mRoot);

            initData();
        }
        return mRoot;
    }

    private void initData() {

    }


    private void initWidget(View root) {

    }

    private void onRestartaInstance(Bundle savedInstanceState) {

    }

    private void onBandViewBefore(View root) {

    }

    protected abstract int getLayoutID();

    private void initBundle(Bundle bundle) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        mBind = null;
    }
}
