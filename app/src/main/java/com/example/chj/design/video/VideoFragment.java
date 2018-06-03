package com.example.chj.design.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.chj.design.MainActivity;
import com.example.chj.design.R;
import com.example.chj.design.base.BaseFragment;
import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.entity.VideoItem;
import com.example.chj.design.video.adapter.VideoAdapter;
import com.example.chj.design.widget.itemdecoration.DividerItemDecoration;
import com.example.chj.design.widget.statusview.LoadingAndRetryManager;
import com.example.chj.design.widget.statusview.OnLoadingAndRetryListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ff on 2018/6/1.
 */

public class VideoFragment extends BaseFragment implements VideoFragmentContract.View {

    @BindView(R.id.recycler_video)
    RecyclerView recyclerVideo;
    private VideoPresenter mPresenter;
    private VideoAdapter adapter;
    private LoadingAndRetryManager manager;

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initWidget(View root) {
        FrameLayout rootView = ((MainActivity) mContext).mRootContainer;
        manager = LoadingAndRetryManager.generate(rootView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {

            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getVideoList();
    }

    @Override
    public void setPresenter(VideoFragmentContract.Presenter presenter) {
        mPresenter = (VideoPresenter) presenter;
    }

    @Override
    public void showLoading() {
        manager.showLoading();
    }

    @Override
    public void hideLoading() {
        manager.showContent();
    }

    @Override
    public void showVideoList(Book book) {

    }

    @Override
    public void showVideoLists(List<VideoItem> list) {
        initAdapter(list);
    }

    private void initAdapter(List<VideoItem> list) {
        adapter = new VideoAdapter(getActivity(), list);
        recyclerVideo.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerVideo.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        recyclerVideo.setAdapter(adapter);
    }
}
