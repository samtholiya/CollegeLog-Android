package com.trak.sam.collegelog.helper;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BaseViewAdapter<T extends RecyclerView.ViewHolder, U> extends RecyclerView.Adapter<T> {

    protected ArrayList<U> mValues;

    public BaseViewAdapter(ArrayList<U> arrayList) {
        mValues = arrayList;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addItem(U item) {
        mValues.add(item);
        notifyItemInserted(mValues.size() - 1);
    }

    public void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }


}
