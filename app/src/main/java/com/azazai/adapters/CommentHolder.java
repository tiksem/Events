package com.azazai.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azazai.R;

/**
 * Created by CM on 7/7/2015.
 */
public class CommentHolder extends RecyclerView.ViewHolder {
    public TextView message;
    public TextView name;
    public ImageView avatar;
    public TextView date;
    public View options;
    public View deleteLoadingView;

    public CommentHolder(View view) {
        super(view);
        message = (TextView) view.findViewById(R.id.message);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        date = (TextView) view.findViewById(R.id.date);
        name = (TextView) view.findViewById(R.id.name);
        options = view.findViewById(R.id.options);
        deleteLoadingView = view.findViewById(R.id.comment_removing_loading);
    }
}
