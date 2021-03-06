package com.trak.sam.collegelog.ViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.BaseItemClick;
import com.trak.sam.collegelog.helper.BaseViewAdapter;
import com.trak.sam.collegelog.model.School;

import java.util.ArrayList;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class SchoolRecyclerViewAdapter extends BaseViewAdapter<SchoolRecyclerViewAdapter.ViewHolder, School> {

    private final BaseItemClick<School> mListener;

    public SchoolRecyclerViewAdapter(ArrayList<School> items, BaseItemClick<School> listener) {
        super(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_school, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListItemClick(holder.mItem);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        //public final TextView mContentView;
        public School mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
