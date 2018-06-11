package com.example.chj.design.home;

import android.os.Bundle;

import com.example.chj.design.base.BaseFragment;

/**
 * Created by ff on 2018/6/11.
 */

public class HomeFragment extends BaseFragment {
    @Override
    protected int getLayoutID() {
        return 0;
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
