package com.example.chj.design.my;

import android.os.Bundle;

import com.example.chj.design.base.BaseFragment;

/**
 * Created by ff on 2018/6/11.
 */

public class MyFragment extends BaseFragment {

    @Override
    protected int getLayoutID() {
        return 0;
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return null;
    }
}
