package com.trak.sam.collegelog.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseOnScrollListener<T> extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before mLoading more.
    private int mVisibleThreshold = 5;

    // The current offset index of data you have loaded
    private int mCurrentPage = 0;

    // The total number of items in the dataset after the last load
    private int mPreviousTotalItemCount = 0;

    private long mOffset = 0;

    private long mLimit = 0;
    // True if we are still waiting for the last set of data to load.
    // private boolean mLoading = true;

    // Sets the starting page index
    private int mStartingPageIndex = 0;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<T> mArrayList;
    private ArrayList<ArrayList<T>> mPageList;
    private PageOperator mPageOperator;
    private boolean mLoadingBellow;
    private boolean mLoadingAbove;

    public BaseOnScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;

    }

    public BaseOnScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        mVisibleThreshold = mVisibleThreshold * layoutManager.getSpanCount();
    }

    public BaseOnScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        mVisibleThreshold = mVisibleThreshold * layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        super.onScrolled(view, dx, dy);
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        Log.d("scroll item count", String.valueOf(totalItemCount));
        int firstVisibleItemPosition = 0;
        if (!isLoadingBellow() && totalItemCount == 0) {
            mPageOperator.loadDataBellow(0, 30, view);
            return;
        }
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        }

        mVisibleThreshold = (lastVisibleItemPosition - firstVisibleItemPosition);


        if (dy > 0 && ((totalItemCount - lastVisibleItemPosition) < mVisibleThreshold) && !mPageOperator.isLoadingBellow()) {
            Log.d("sam-scroll", "bellow");
        }

        if (dy < 0 && (firstVisibleItemPosition < mVisibleThreshold) && !mPageOperator.isLoadingAbove()) {
            Log.d("sam-scroll", "above");
        }


        // If it’s still mLoading, we check to see if the dataset count has
        // changed, if so we conclude it has finished mLoading and update the current page
        // number and total item count.
        /*
        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }
        */

        // If it isn’t currently mLoading, we check to see if we have breached
        // the mVisibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        /*if (!isLoadingBellow() && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount
                && view.getAdapter().getItemCount() > mVisibleThreshold) {
            // This condition will useful when recyclerview has less than mVisibleThreshold items
            mCurrentPage++;
            onLoadMoreBellow(mCurrentPage, totalItemCount, view);
            // mLoading = true;
        }*/
    }

    private void loadPageBellow(RecyclerView view) {
        mLoadingBellow = true;
        mPageOperator.loadDataBellow(mOffset, mLimit, view);
    }

    private void loadPageAbove(RecyclerView view) {
        if(mOffset == mArrayList.size()) {
            return;
        }
        mLoadingAbove = true;
        mPageOperator.loadDataAbove((mOffset - mArrayList.size()), mLimit, view);
    }

    // Call whenever performing new searches
    public void resetState() {
        this.mCurrentPage = this.mStartingPageIndex;
        this.mPreviousTotalItemCount = 0;
        // this.mLoading = true;
    }

    // Defines the process for actually mLoading more data based on page
//    public abstract void onLoadMoreBellow(int page, int totalItemsCount, RecyclerView view);

    public boolean isLoadingBellow() {
        return false;
    }

//    public abstract void onLoadMoreAbove(int page, int totalItemsCount, RecyclerView view);

    public boolean isLoadingAbove() {
        return false;
    }

    private void removeFirstPage() {
        if(mOffset == mArrayList.size()) {
            return;
        }
        mArrayList.removeAll(mPageList.get(0));
        mAdapter.notifyItemRangeRemoved(0, mPageList.get(0).size());
        mPageList.remove(0);
    }

    private void removeLastPage() {
        ArrayList<T> arrayList = mPageList.get(mPageList.size() - 1);
        int previousTotal = mArrayList.size();
        mArrayList.removeAll(arrayList);
        mAdapter.notifyItemRangeRemoved((previousTotal - arrayList.size()), arrayList.size());
        mOffset = mOffset - arrayList.size();
        mPageList.remove(mPageList.size() - 1);
    }

    public void addPageBellow(T[] items) {
        ArrayList<T> arrayList = new ArrayList<>(Arrays.asList(items));
        addPageBellow(arrayList);
    }

    public void addPageBellow(ArrayList<T> items) {
        mPageList.add(items);
        int lastPosition = mArrayList.size() - 1;
        mArrayList.addAll(items);
        mAdapter.notifyItemRangeInserted(lastPosition, items.size());
        mOffset = mOffset + items.size();
        mLoadingBellow = false;
    }


    public void addPageAbove(T[] items) {
        ArrayList<T> arrayList = new ArrayList<>(Arrays.asList(items));
        addPageAbove(arrayList);
    }


    public void addPageAbove(ArrayList<T> items) {
        mPageList.add(0, items);
        mArrayList.addAll(0, items);
        mAdapter.notifyItemRangeInserted(0, items.size());
        mLoadingAbove = false;
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    public void setArrayList(ArrayList<T> arrayList) {
        this.mArrayList = arrayList;
    }

    public void addPageOperator(PageOperator pageOperator) {
        mPageOperator = pageOperator;
    }

    public static interface PageOperator {
        public void loadDataBellow(long offset, long limit, RecyclerView view);

        public void loadDataAbove(long offset, long limit, RecyclerView view);

    }
}
