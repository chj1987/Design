package com.example.chj.design.model;

import com.example.chj.design.model.entity.Book;
import com.example.chj.design.model.source.IDataSource;
import com.example.chj.design.utils.Preconditions;

import io.reactivex.Flowable;

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
    public Flowable<Book> getBook() {

        return getDataFromRemote();
    }

    private Flowable<Book> getDataFromRemote() {
        return mRemoteDataSource.getBook();
    }
}
