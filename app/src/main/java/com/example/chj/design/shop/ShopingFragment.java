package com.example.chj.design.shop;

import android.os.Bundle;

import com.example.chj.design.R;
import com.example.chj.design.base.BaseFragment;


/**
 * Created by ff on 2018/6/11.
 */

public class ShopingFragment extends BaseFragment {
    @Override
    protected int getLayoutID() {
        return R.layout.shoping_fragment;
    }

    public static ShopingFragment newInstance() {
        ShopingFragment fragment = new ShopingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
