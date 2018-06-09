package com.trak.sam.collegelog.callback;

public interface BaseHttpCallback<T> {
    public void onItemReceived(T item);
    public void onItemsReceived(T[] items);
    public void onFailed(Exception e);
}
