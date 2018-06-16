package com.trak.sam.collegelog.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseOnScrollListener<T> extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before mLoading more.
    private int mVisibleThreshold = 5;

    private int mArraySize = 0;
    // The current offset index of data you have loaded
    private int mCurrentPage = 0;

    // The total number of items in the dataset after the last load
    private int mPreviousTotalItemCount = 0;

    private long mOffset = 0;
    private long mPreviousOffset = 0;

    private long mLimit = 0;
    // True if we are still waiting for the last set of data to load.
    // private boolean mLoading = true;

    // Sets the starting page index
    private int mStartingPageIndex = 0;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<T> mArrayList;
    private ArrayList<ArrayList<T>> mPageList = new ArrayList<>();
    private PageOperator mPageOperator;
    private boolean mLoadingBellow = false;
    private boolean mLoadingAbove = false;

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

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
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

    private int getFirstVisibleItem(int[] firstVisibleItemPositions) {
        int minSize = 0;
        for (int i = 0; i < firstVisibleItemPositions.length; i++) {
            if (i == 0) {
                minSize = firstVisibleItemPositions[i];
            } else if (firstVisibleItemPositions[i] < minSize) {
                minSize = firstVisibleItemPositions[i];
            }
        }
        return minSize;

    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        super.onScrolled(view, dx, dy);
        int lastVisibleItemPosition;
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition;

        if (!mLoadingBellow && totalItemCount == 0) {
            mPageOperator.loadDataBellow(0, 20, view);
            return;
        }

        firstVisibleItemPosition = getFirstItemPosition();
        lastVisibleItemPosition = getLastItemPosition();

        mVisibleThreshold = (lastVisibleItemPosition - firstVisibleItemPosition);
        mLimit = mVisibleThreshold;

        if (mVisibleThreshold == 0) {
            mLimit = 25;
        }

        if (dy > 0 && ((totalItemCount - lastVisibleItemPosition) < mVisibleThreshold) && !mLoadingBellow) {
            loadPageBellow(view);
        }

        if (dy < 0 && (firstVisibleItemPosition < mVisibleThreshold) && !mLoadingAbove) {
            loadPageAbove(view);
        }
    }

    private void loadPageBellow(RecyclerView view) {

        mLoadingBellow = true;
        mPageOperator.loadDataBellow(mOffset, mLimit, view);
    }

    private void loadPageAbove(RecyclerView view) {
        if (mPreviousOffset <= 0) {
            return;
        }
        mLoadingAbove = true;
        long temp = mPreviousOffset - mVisibleThreshold;
        long tempLimit = mLimit;
        if (temp < 0) {
            tempLimit = mPreviousOffset;
            temp = 0;
        }
        mPageOperator.loadDataAbove(temp, tempLimit, view);
    }

    // Call whenever performing new searches
    public void resetState() {
        this.mCurrentPage = this.mStartingPageIndex;
        this.mPreviousTotalItemCount = 0;
        // this.mLoading = true;
    }

    private void removeFirstPage() {
        int firstVisibleItemPosition = getFirstItemPosition();
        if (mArrayList.size() > (mVisibleThreshold * 3) && (mPageList.get(0).size() < firstVisibleItemPosition)) {
            mPreviousOffset += mPageList.get(0).size();
            mArrayList.removeAll(mPageList.get(0));
            mAdapter.notifyItemRangeRemoved(0, mPageList.get(0).size());
            mPageList.remove(0);
        }
    }

    private int getFirstItemPosition() {
        int firstVisibleItemPosition = 0;
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null);
            // get minimum element within the list
            firstVisibleItemPosition = getFirstVisibleItem(firstVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        }

        return firstVisibleItemPosition;
    }

    private int getLastItemPosition() {
        int lastVisibleItemPosition = 0;

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }

    private void removeLastPage() {

        int lastVisibleItemPosition = getLastItemPosition();

        if (mArrayList.size() > (mVisibleThreshold * 3) && ((mArrayList.size() - mPageList.get(mPageList.size() - 1).size() - 1) > lastVisibleItemPosition)) {
            ArrayList<T> arrayList = mPageList.get(mPageList.size() - 1);
            int previousTotal = mArrayList.size();
            mArrayList.removeAll(arrayList);
            mAdapter.notifyItemRangeRemoved((previousTotal - arrayList.size()), arrayList.size());
            mOffset = mOffset - arrayList.size();
            mPageList.remove(mPageList.size() - 1);
        }
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
        removeFirstPage();
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
        mPreviousOffset -= items.size();
        removeLastPage();
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
        mPageOperator.loadDataBellow(0, 20, null);
    }

    public interface PageOperator {

        void loadDataBellow(long offset, long limit, RecyclerView view);

        void loadDataAbove(long offset, long limit, RecyclerView view);
    }

}
