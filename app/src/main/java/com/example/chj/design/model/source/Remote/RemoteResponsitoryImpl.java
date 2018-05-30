package com.example.chj.design.model.source.Remote;

import com.example.chj.design.http.IRetrofitService;
import com.example.chj.design.http.RetrofitHelp;
import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.source.IDataSource;

import io.reactivex.Flowable;

/**
 * Created by ff on 2018/5/30.
 */

public class RemoteResponsitoryImpl implements IDataSource {

    private IRetrofitService mRetrofitService;

    private static RemoteResponsitoryImpl mInstance = new RemoteResponsitoryImpl();

    private RemoteResponsitoryImpl() {
        mRetrofitService = RetrofitHelp.getInstance().getService();
    }

    public static RemoteResponsitoryImpl getInstance() {
        return mInstance;
    }

    @Override
    public Flowable<Book> getBook() {
        return mRetrofitService.getTopMovie(0, 10);
    }
}
