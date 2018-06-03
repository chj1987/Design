package com.example.chj.design.video;

import android.support.v4.widget.NestedScrollView;
import android.view.ViewTreeObserver;

import com.example.chj.design.MainActivity;
import com.example.chj.design.model.entity.Video;
import com.example.chj.design.model.entity.VideoItem;
import com.example.chj.design.model.source.IDataSource;
import com.example.chj.design.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ff on 2018/6/1.
 */

public class VideoPresenter implements VideoFragmentContract.Presenter {
    private IDataSource mDataRespository;
    private CompositeDisposable mCompositeDisposable;
    private VideoFragment mVideoFragmentView;
    private ArrayList<VideoItem> itemArrayList;

    public VideoPresenter(VideoFragmentContract.View videoFragmentView,
                          IDataSource dataRespository) {
        mCompositeDisposable = new CompositeDisposable();
        mVideoFragmentView = (VideoFragment) Preconditions.checkNotNull(videoFragmentView);
        mDataRespository = Preconditions.checkNotNull(dataRespository);
        mVideoFragmentView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        // Toast.makeText(mVideoFragmentView.getContext(), "subscribe", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getVideoList() {
        mVideoFragmentView.showLoading();
        mDataRespository
                .getVideoLists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Video>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Video video) {
                        itemArrayList = new ArrayList<>();
                        List<Video.ItemsBean> itemsBeanList = video.getItems();
                        for (int i = 0; i < itemsBeanList.size(); i++) {
                            VideoItem videoItem = new VideoItem();
                            videoItem.setName(itemsBeanList.get(i).getKey());
                            videoItem.setTime(String.valueOf(itemsBeanList.get(i).getPutTime()));
                            videoItem.setSize(String.valueOf(itemsBeanList.get(i).getFsize()));
                            itemArrayList.add(videoItem);
                        }

                        mVideoFragmentView.showVideoLists(itemArrayList);
                        final NestedScrollView scrollview = ((MainActivity) mVideoFragmentView.getActivity()).nestedScrollViewiew;
                        scrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                scrollview.post(new Runnable() {
                                    public void run() {
                                        scrollview.scrollTo(0, 0);
                                    }
                                });
                            }
                        });
                        mVideoFragmentView.hideLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
