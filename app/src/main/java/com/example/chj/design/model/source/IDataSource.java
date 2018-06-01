package com.example.chj.design.model.source;

import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.entity.Video;

import io.reactivex.Observable;

/**
 * Created by ff on 2018/5/30.
 */

public interface IDataSource {
    Observable<Book> getBook();

    Observable<Video> getVideoLists();
}
