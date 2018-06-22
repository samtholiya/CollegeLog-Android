package com.trak.sam.collegelog.ViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.OnDeleteItemClicked;
import com.trak.sam.collegelog.model.Department;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class CreateDepartmentRecyclerViewAdapter extends RecyclerView.Adapter<CreateDepartmentRecyclerViewAdapter.ViewHolder>  {

    private final List<Department> mValues;
    private final OnDeleteItemClicked mListener;

    public CreateDepartmentRecyclerViewAdapter(List<Department> items, OnDeleteItemClicked listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_department_create, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.addTextChangedListener(new TextWatcherHandler(position));
        holder.mView.setOnClickListener(new OnClickHandler(position));
    }

    private class OnClickHandler implements View.OnClickListener {

        private int mPosition;

        private OnClickHandler(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View view) {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onDeleteItemClick(view, mPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private class TextWatcherHandler implements TextWatcher {

        private int mPosition;

        private TextWatcherHandler(int position) {
            mPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mValues.get(mPosition).name = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final EditText mIdView;
        public final Button mContentView;
        public Department mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView =  view.findViewById(R.id.editText);
            mContentView =  view.findViewById(R.id.button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
