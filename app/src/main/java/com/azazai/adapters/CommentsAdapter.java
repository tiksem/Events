package com.azazai.adapters;

import android.support.v4.app.Fragment;
import android.view.View;

import com.azazai.R;
import com.azazai.data.Comment;
import com.squareup.picasso.Picasso;
import com.utilsframework.android.adapters.RecyclerViewListAdapterWithNullItemsSupport;
import com.utilsframework.android.time.TimeUtils;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkUser;

import java.util.Set;

/**
 * Created by CM on 7/7/2015.
 */
public class CommentsAdapter extends RecyclerViewListAdapterWithNullItemsSupport<Comment, CommentHolder> {
    private final Picasso picasso;
    private Fragment fragment;
    private Set<Comment> commentsRequestedForDeleting;
    private Comment editingComment;

    public CommentsAdapter(Fragment fragment) {
        this.fragment = fragment;
        picasso = Picasso.with(fragment.getContext());
    }

    @Override
    public int getNormalItemLayoutId() {
        return R.layout.comment;
    }

    @Override
    protected void onBindItemViewHolder(final CommentHolder holder,
                                        final int position,
                                        final Comment comment) {
        holder.name.setText(comment.userName);
        holder.message.setText(comment.text);
        String date = TimeUtils.getAlternativeDisplayDateTime(fragment.getContext(),
                comment.date * 1000l);
        holder.date.setText(date);
        if (comment.userId == VkUser.getCurrentUserId()) {
            final boolean commentRequestedForDeleting = commentsRequestedForDeleting.contains(comment);
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!commentRequestedForDeleting && comment != editingComment) {
                        v.setTag(comment);
                        v.showContextMenu();
                    }
                }
            });
            fragment.registerForContextMenu(holder.options);
            holder.deleteLoadingView.setVisibility(
                    commentRequestedForDeleting ? View.VISIBLE : View.GONE);
            holder.editingLoadingView.setVisibility(
                    editingComment == comment ? View.VISIBLE : View.GONE);
            holder.options.setVisibility(View.VISIBLE);
        } else {
            holder.options.setVisibility(View.GONE);
            holder.deleteLoadingView.setVisibility(View.GONE);
            holder.editingLoadingView.setVisibility(View.GONE);
        }

        picasso.load(comment.avatar).into(holder.avatar);
    }

    public void setCommentsRequestedForDeleting(Set<Comment> commentsRequestedForDeleting) {
        this.commentsRequestedForDeleting = commentsRequestedForDeleting;
    }

    @Override
    protected CommentHolder onCreateItemViewHolder(View view, int viewType) {
        return new CommentHolder(view);
    }

    public void setEditingComment(Comment editingComment) {
        this.editingComment = editingComment;
    }
}
