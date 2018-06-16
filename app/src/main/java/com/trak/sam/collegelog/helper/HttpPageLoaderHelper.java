package com.trak.sam.collegelog.helper;

import com.trak.sam.collegelog.callback.BaseHttpCallback;

public class HttpPageLoaderHelper<T> implements BaseHttpCallback<T> {

    private boolean mIsBellow;
    private BaseOnScrollListener<T> mBaseOnScrollListener;

    public HttpPageLoaderHelper(boolean isBellow, BaseOnScrollListener<T> baseOnScrollListener) {
        mIsBellow = isBellow;
        mBaseOnScrollListener = baseOnScrollListener;
    }

    @Override
    public void onItemReceived(T item) {

    }

    @Override
    public void onItemsReceived(T[] items) {
        if (mIsBellow) {
            mBaseOnScrollListener.addPageBellow(items);
        } else {
            mBaseOnScrollListener.addPageAbove(items);
        }
    }

    @Override
    public void onFailed(Exception e) {

    }
}

