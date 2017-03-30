package com.azazai.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azazai.R;
import com.azazai.data.Comment;
import com.squareup.picasso.Picasso;
import com.utilsframework.android.adapters.RecyclerViewListAdapterWithNullItemsSupport;
import com.utilsframework.android.adapters.navigation.LazyLoadingListAdapter;
import com.utilsframework.android.time.TimeUtils;

/**
 * Created by CM on 7/7/2015.
 */
public class CommentsAdapter extends RecyclerViewListAdapterWithNullItemsSupport<Comment, CommentHolder> {
    private final Picasso picasso;
    private Context context;

    public CommentsAdapter(Context context) {
        this.context = context;
        picasso = Picasso.with(context);
    }

    @Override
    public int getNormalItemLayoutId() {
        return R.layout.comment;
    }

    @Override
    protected void onBindItemViewHolder(final CommentHolder holder,
                                        final int position,
                                        Comment comment) {
        holder.name.setText(comment.userName);
        holder.message.setText(comment.text);
        String date = TimeUtils.getAlternativeDisplayDateTime(context, comment.date * 1000l);
        holder.date.setText(date);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                v.showContextMenu();
            }
        });

        picasso.load(comment.avatar).into(holder.avatar);
    }

    @Override
    protected CommentHolder onCreateItemViewHolder(View view, int viewType) {
        return new CommentHolder(view);
    }
}
