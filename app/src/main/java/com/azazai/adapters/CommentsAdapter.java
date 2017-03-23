package com.azazai.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azazai.R;
import com.azazai.data.Comment;
import com.squareup.picasso.Picasso;
import com.utilsframework.android.adapters.navigation.LazyLoadingListAdapter;
import com.utilsframework.android.time.TimeUtils;

/**
 * Created by CM on 7/7/2015.
 */
public class CommentsAdapter extends LazyLoadingListAdapter<Comment, CommentHolder> {
    private final Picasso picasso;

    public CommentsAdapter(Context context) {
        super(context);
        picasso = Picasso.with(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.comment;
    }

    @Override
    protected CommentHolder createViewHolder(View view, int position) {
        CommentHolder holder = new CommentHolder();
        holder.message = (TextView) view.findViewById(R.id.message);
        holder.avatar = (ImageView) view.findViewById(R.id.avatar);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.name = (TextView) view.findViewById(R.id.name);
        return holder;
    }

    @Override
    protected void reuseView(Comment comment, CommentHolder holder, int position, View view) {
        holder.name.setText(comment.userName);
        holder.message.setText(comment.text);
        String date = TimeUtils.getAlternativeDisplayDateTime(view.getContext(), comment.date * 1000l);
        holder.date.setText(date);
        picasso.load(comment.avatar).into(holder.avatar);
    }
}
