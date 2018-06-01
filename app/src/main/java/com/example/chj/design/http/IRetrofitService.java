package com.example.chj.design.http;

import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.entity.Video;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ff on 2018/5/30.
 */

public interface IRetrofitService {
    //https://api.douban.com/v2/movie/top250?start=0&count=10
    @GET("top250")
    Observable<Book> getTopMovie(@Query("start") int start, @Query("count") int count);

    //https://api-demo.qnsdk.com/v1/kodo/bucket/demo-videos?prefix=movies
    @GET("demo-videos")
    Observable<Video> getVideoList(@Query("prefix") String prefix);
}
