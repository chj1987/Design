package com.example.chj.design.http;

import com.example.chj.design.model.entity.Book;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ff on 2018/5/30.
 */

public interface IRetrofitService {
    //https://api.douban.com/v2/movie/top250?start=0&count=10
    @GET("top250")
    Flowable<Book> getTopMovie(@Query("start") int start, @Query("count") int count);
}
