package com.example.chj.design.base;

/**
 * Created by ff on 2018/5/25.
 */

public interface BaseView<T extends BasePresenter> {

    /**
     * 设置presenter
     *
     * @param presenter
     */
    void setPresenter(T presenter);

}
