package com.khevents.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import com.khevents.R;
import com.khevents.adapters.CommentsAdapter;
import com.khevents.data.Comment;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.social.SocialUtils;
import com.vkandroid.VkApiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 7/7/2015.
 */
public class CommentsFragment extends AbstractNavigationListFragment<Comment> {
    private static final String EVENT_ID = "eventId";
    private static final String TOP_COMMENTS = "topComments";

    public static CommentsFragment create(long eventId, List<Comment> topComments) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putLong(EVENT_ID, eventId);
        args.putParcelableArrayList(TOP_COMMENTS, new ArrayList<Comment>(topComments));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ViewArrayAdapter<Comment, ?> createAdapter(RequestManager requestManager) {
        return new CommentsAdapter(getActivity());
    }

    @Override
    protected NavigationList<Comment> getNavigationList(RequestManager requestManager, String filter) {
        Bundle arguments = getArguments();
        ArrayList<Comment> topComments = arguments.getParcelableArrayList(TOP_COMMENTS);
        return requestManager.getComments(topComments, arguments.getLong(EVENT_ID));
    }

    @Override
    protected void onListItemClicked(Comment item) {
        SocialUtils.openVkUserProfile(getActivity(), item.userId);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.comments;
    }
}
