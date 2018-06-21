package com.trak.sam.collegelog.callback;

import java.util.ArrayList;

public interface CreateItemListFragment<T> {
    ArrayList<T> getItemList();
    void setItemList(ArrayList<T> arrayList);
}
