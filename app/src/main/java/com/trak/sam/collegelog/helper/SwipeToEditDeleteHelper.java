package com.trak.sam.collegelog.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.OnItemSwiped;

public class SwipeToEditDeleteHelper extends ItemTouchHelper.SimpleCallback {

    private final OnItemSwiped mOnItemSwiped;
    private Drawable mDeleteBackground;
    private Drawable mDeleteIcon;
    private Drawable mEditBackground;
    private Drawable mEditIcon;
    private int mIconMargin;

    public SwipeToEditDeleteHelper(Context context, OnItemSwiped onItemSwiped) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mOnItemSwiped = onItemSwiped;
        mDeleteBackground = new ColorDrawable(ContextCompat.getColor(context, R.color.swipeDeleteBackgroundColor));
        mDeleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_sweep_24dp);
        mEditBackground = new ColorDrawable(ContextCompat.getColor(context, R.color.swipeEditBackgroundColor));
        mEditIcon = ContextCompat.getDrawable(context, R.drawable.ic_edit_24dp);

        mDeleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mIconMargin = (int) context.getResources().getDimension(R.dimen.swipe_to_delete_margin);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // Not important, we don't want drag & drop
        return false;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        /*_profileAdapter = (ProfileAdapter)recyclerView.getAdapter();
        if (_profileAdapter.isUndoEnabled() && _profileAdapter.isPendingRemoval(position)) {
            return 0;
        }*/
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        int swipedPosition = viewHolder.getAdapterPosition();

        switch(swipeDir) {
            case ItemTouchHelper.LEFT:
                mOnItemSwiped.swipedLeft(swipedPosition);
                break;
            case ItemTouchHelper.RIGHT:
                mOnItemSwiped.swipedRight(swipedPosition);
                break;
        }

    }


    @Override

    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        // Not sure why, but this method gets called for view holders that are already swiped away
        if (viewHolder.getAdapterPosition() == -1) {
            // Not interested in those
            return;
        }
        // Draw _background
        int itemHeight = itemView.getBottom() - itemView.getTop();

        if (dX <= 0) {
            mDeleteBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            int intrinsicWidth = mDeleteIcon.getIntrinsicWidth();
            int intrinsicHeight = mDeleteIcon.getIntrinsicWidth();
            int xMarkLeft = itemView.getRight() - mIconMargin - intrinsicWidth;
            int xMarkRight = itemView.getRight() - mIconMargin;
            int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int xMarkBottom = xMarkTop + intrinsicHeight;
            mDeleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

            mDeleteBackground.draw(c);
            mDeleteIcon.draw(c);
        } else {
            mEditBackground.setBounds(0, itemView.getTop(), (int) dX, itemView.getBottom());
            int intrinsicHeight = mEditIcon.getIntrinsicWidth();
            int xMarkLeft = mIconMargin;
            int xMarkRight = intrinsicHeight + mIconMargin;
            int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int xMarkBottom = xMarkTop + intrinsicHeight;
            mEditIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

            mEditBackground.draw(c);
            mEditIcon.draw(c);
        }
        // Draw X mark
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
