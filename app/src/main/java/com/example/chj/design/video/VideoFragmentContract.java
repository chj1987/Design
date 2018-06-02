package com.example.chj.design.video;

import com.example.chj.design.base.BasePresenter;
import com.example.chj.design.base.BaseView;
import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.entity.VideoItem;

import java.util.List;

/**
 * Created by ff on 2018/6/1.
 */

public interface VideoFragmentContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showVideoList(Book book);

        void showVideoLists(List<VideoItem> list);
    }

    interface Presenter extends BasePresenter {
        void getVideoList();
    }
}
