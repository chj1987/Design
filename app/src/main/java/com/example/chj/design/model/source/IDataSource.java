package com.example.chj.design.model.source;

import com.example.chj.design.model.entity.Book;

import io.reactivex.Flowable;

/**
 * Created by ff on 2018/5/30.
 */

public interface IDataSource {
    Flowable<Book> getBook();
}
