package com.khevents.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.adapters.CommentsAdapter;
import com.khevents.data.Comment;
import com.khevents.network.RequestManager;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.social.SocialUtils;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.view.EditTextUtils;
import com.utilsframework.android.view.Toasts;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 7/7/2015.
 */
public class CommentsFragment extends AbstractNavigationListFragment<Comment> implements ActionBarTitleProvider {
    private static final String EVENT_ID = "eventId";
    private static final String TOP_COMMENTS = "topComments";
    public static final String REQUEST_ADD_COMMENT_FOCUS = "requestAddCommentFocus";
    private long eventId;
    private EditText commentMessage;
    private View addCommentButton;

    public static CommentsFragment create(long eventId, List<Comment> topComments, boolean requestAddCommentFocus) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putLong(EVENT_ID, eventId);
        args.putBoolean(REQUEST_ADD_COMMENT_FOCUS, requestAddCommentFocus);
        args.putParcelableArrayList(TOP_COMMENTS, new ArrayList<Comment>(topComments));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventId = getArguments().getLong(EVENT_ID);
    }

    @Override
    protected ViewArrayAdapter<Comment, ?> createAdapter(RequestManager requestManager) {
        return new CommentsAdapter(getActivity());
    }

    @Override
    protected NavigationList<Comment> getNavigationList(RequestManager requestManager, String filter) {
        Bundle arguments = getArguments();
        ArrayList<Comment> topComments = arguments.getParcelableArrayList(TOP_COMMENTS);
        return requestManager.getComments(topComments, eventId);
    }

    @Override
    protected void onListItemClicked(Comment item) {
        SocialUtils.openVkUserProfile(getActivity(), item.userId);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.comments;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAddCommentControl(view);

        if (getArguments().getBoolean(REQUEST_ADD_COMMENT_FOCUS)) {
            getArguments().putBoolean(REQUEST_ADD_COMMENT_FOCUS, false);
            EditTextUtils.showKeyboard(commentMessage);
        }
    }

    public void setupAddCommentControl(View view) {
        commentMessage = (EditText) view.findViewById(R.id.add_comment_message);
        addCommentButton = view.findViewById(R.id.add_comment);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeAddCommentRequest();
            }
        });
    }

    private void addCommentToList(String text) {
        Comment comment = new Comment();
        VkUser user = EventsApp.getInstance().getCurrentUser();
        comment.avatar = user.avatar;
        comment.userName = user.name + " " + user.lastName;
        comment.text = text;
        comment.date = (int) (System.currentTimeMillis() / 1000);
        getElements().set(0, comment);
        getAdapter().notifyDataSetChanged();
        commentMessage.setText("");
        EventFragment eventFragment = (EventFragment) getNavigationActivityInterface().getLatestBackStackFragment();
        eventFragment.addTopComment(comment);
    }

    public void executeAddCommentRequest() {
        Editable text = commentMessage.getText();
        if (!TextUtils.isEmpty(text)) {
            // show loading
            getElements().add(0, null);
            getAdapter().notifyDataSetChanged();
            final String commentText = getCommentText(text.toString());
            addCommentButton.setEnabled(false);
            getRequestManager().addComment(commentText, eventId,
                    VKSdk.getAccessToken().accessToken, new AddCommentCallback(this, commentText));
        } else {
            Toasts.message(getActivity(), R.string.enter_comment_message);
        }
    }

    void onCommentAdded(IOException e, String commentText) {
        if (e == null) {
            addCommentToList(commentText);
        } else {
            Toasts.error(getActivity(), R.string.no_internet_connection);
            getElements().remove(0);
            getAdapter().notifyDataSetChanged();
        }
        addCommentButton.setEnabled(true);
    }

    public String getCommentText(String text) {
        return text.replaceAll("\\n{2,}", "\n\n");
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.comments);
    }
}
