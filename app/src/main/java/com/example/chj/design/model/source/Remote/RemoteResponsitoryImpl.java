package com.example.chj.design.model.source.Remote;

import com.example.chj.design.http.IRetrofitService;
import com.example.chj.design.http.RetrofitHelp;
import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.entity.Video;
import com.example.chj.design.model.source.IDataSource;

import io.reactivex.Observable;

/**
 * Created by ff on 2018/5/30.
 */

public class RemoteResponsitoryImpl implements IDataSource {

    private IRetrofitService mRetrofitService;

    public RemoteResponsitoryImpl(String BASE_URL) {
        RetrofitHelp retrofitHelp = new RetrofitHelp.Builder(BASE_URL).build();
        mRetrofitService = retrofitHelp.getService();

    }

    @Override
    public Observable<Book> getBook() {
        return mRetrofitService.getTopMovie(0, 10);
    }

    @Override
    public Observable<Video> getVideoLists() {
        return mRetrofitService.getVideoList("movies");
    }
}
