package com.example.chj.design.model;

import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.entity.Video;
import com.example.chj.design.model.source.IDataSource;
import com.example.chj.design.utils.Preconditions;

import io.reactivex.Observable;

/**
 * Created by ff on 2018/5/30.
 */

public class DataRespositoryImpl implements IDataSource {

    private static DataRespositoryImpl mInstance;
    private IDataSource mLocalDataSource;
    private IDataSource mRemoteDataSource;

    private DataRespositoryImpl(IDataSource localDataSource, IDataSource remoteDataSource) {
        this.mLocalDataSource = Preconditions.checkNotNull(localDataSource);
        this.mRemoteDataSource = Preconditions.checkNotNull(remoteDataSource);
    }

    public static DataRespositoryImpl getInstance(IDataSource localDataSource,
                                                  IDataSource remoteDataSource) {
        if (mInstance == null) {
            mInstance = new DataRespositoryImpl(localDataSource, remoteDataSource);
        }
        return mInstance;
    }

    @Override
    public Observable<Book> getBook() {

        return getDataFromRemote();
    }

    @Override
    public Observable<Video> getVideoLists() {
        return mRemoteDataSource.getVideoLists();
    }

    private Observable<Book> getDataFromRemote() {
        return mRemoteDataSource.getBook();
    }
}
