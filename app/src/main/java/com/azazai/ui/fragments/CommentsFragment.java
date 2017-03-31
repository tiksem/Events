package com.azazai.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.azazai.EventsApp;
import com.azazai.R;
import com.azazai.adapters.CommentsAdapter;
import com.azazai.data.Comment;
import com.azazai.ui.FinishListenerShowingToastOnError;
import com.utils.framework.collections.LazyLoadingList;
import com.utils.framework.strings.Strings;
import com.utilsframework.android.adapters.ListAdapter;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.navdrawer.BackPressedListener;
import com.utilsframework.android.social.SocialUtils;
import com.utilsframework.android.threading.MainThreadExecutor;
import com.utilsframework.android.view.EditTextUtils;
import com.utilsframework.android.view.ExtendedEditText;
import com.utilsframework.android.view.Toasts;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 7/7/2015.
 */
public class CommentsFragment extends AbstractLazyLoadingListFragment<Comment>
        implements ActionBarTitleProvider {
    private static final String EVENT_ID = "eventId";
    private static final String TOP_COMMENTS = "topComments";
    public static final String REQUEST_ADD_COMMENT_FOCUS = "requestAddCommentFocus";
    private long eventId;
    private ExtendedEditText commentMessage;
    private View addCommentButton;
    private Comment selectedMenuComment;
    private Comment editingComment;
    private String textBeforeEdit;

    public static CommentsFragment create(long eventId, List<Comment> topComments, boolean requestAddCommentFocus) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putLong(EVENT_ID, eventId);
        args.putBoolean(REQUEST_ADD_COMMENT_FOCUS, requestAddCommentFocus);
        args.putParcelableArrayList(TOP_COMMENTS, new ArrayList<>(topComments));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventId = getArguments().getLong(EVENT_ID);
    }

    @Override
    protected ListAdapter<Comment> createAdapter() {
        CommentsAdapter adapter = new CommentsAdapter(this);
        adapter.setCommentsRequestedForDeleting(getDisabledItems());
        return adapter;
    }

    @Override
    protected LazyLoadingList<Comment> getLazyLoadingList(String filter) {
        if (isRefreshing()) {
            return getRequestManager().getComments(eventId);
        }

        Bundle arguments = getArguments();
        ArrayList<Comment> topComments = arguments.getParcelableArrayList(TOP_COMMENTS);
        if (topComments.size() <= EventFragment.TOP_COMMENTS_COUNT) {
            return LazyLoadingList.decorate(topComments);
        }

        return getRequestManager().getComments(topComments, eventId);
    }

    @Override
    protected void onListItemClicked(Comment item, int position) {
        SocialUtils.openVkUserProfile(getActivity(), item.userId);
    }

    @Override
    protected int getRootLayout() {
        return R.layout.comments;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupAddCommentControl(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onListViewIsShown() {
        super.onListViewIsShown();

        if (getArguments().getBoolean(REQUEST_ADD_COMMENT_FOCUS)) {
            getArguments().putBoolean(REQUEST_ADD_COMMENT_FOCUS, false);
            showKeyboard();
        }
    }

    private void showKeyboard() {
        EditTextUtils.showKeyboard(commentMessage);
    }

    public void setupAddCommentControl(View view) {
        commentMessage = (ExtendedEditText) view.findViewById(R.id.add_comment_message);
        addCommentButton = view.findViewById(R.id.add_comment);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitButtonClick();
            }
        });
        commentMessage.setBackPressedListener(new BackPressedListener() {
            @Override
            public boolean shouldOverrideDefaultBackPressedBehavior() {
                if (editingComment != null) {
                    clearCommentMessage();
                    return true;
                }

                return false;
            }
        });
    }

    private void onSubmitButtonClick() {
        if (editingComment != null) {
            executeEditCommentRequest();
        } else {
            executeAddCommentRequest();
        }
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

    private void executeEditCommentRequest() {
        Editable text = commentMessage.getText();
        if (TextUtils.isEmpty(text)) {
            showEmptyTextToast();
            return;
        }
        if (Strings.charSequenceEquals(text, textBeforeEdit)) {
            hideKeyboard();
            return;
        }

        final String commentText = getCommentText(text.toString());

        final Comment comment = editingComment;
        clearCommentMessage();

        getRequestManager().editComment(commentText, comment.id,
                VKSdk.getAccessToken().accessToken,
                new FinishListenerShowingToastOnError(getContext()) {
                    @Override
                    public void onSuccess() {
                        comment.text = commentText;
                        getAdapter().notifyItemChanged(comment);
                    }
                });
    }

    private void clearCommentMessage() {
        commentMessage.setText("");
        hideKeyboard();
        commentMessage.clearFocus();
        editingComment = null;
    }

    private void hideKeyboard() {
        EditTextUtils.hideKeyboard(commentMessage);
    }

    private void showEmptyTextToast() {
        Toasts.toast(getActivity(), R.string.enter_comment_message);
    }

    public void executeAddCommentRequest() {
        Editable text = commentMessage.getText();
        if (!TextUtils.isEmpty(text)) {
            // show loading
            getElements().add(0, null);
            getAdapter().notifyDataSetChanged();
            final String commentText = getCommentText(text.toString());
            clearCommentMessage();
            addCommentButton.setEnabled(false);
            getRequestManager().addComment(commentText, eventId,
                    VKSdk.getAccessToken().accessToken,
                    new FinishListenerShowingToastOnError(getContext()) {
                        @Override
                        public void onFinish() {
                            addCommentButton.setEnabled(true);
                        }

                        @Override
                        public void onError() {
                            getElements().remove(0);
                            getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public void onSuccess() {
                            addCommentToList(commentText);
                        }
                    });
        } else {
            showEmptyTextToast();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.comment_menu, menu);
        selectedMenuComment = (Comment) v.getTag();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.delete) {
            onDeleteCommentRequested(selectedMenuComment);
            return true;
        } else if(itemId == R.id.edit) {
            onEditCommentRequested(selectedMenuComment);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void onEditCommentRequested(Comment comment) {
        textBeforeEdit = comment.text;
        commentMessage.setText(textBeforeEdit);
        editingComment = comment;
        EditTextUtils.moveCursorToEnd(commentMessage);
        MainThreadExecutor.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyboard();
            }
        }, 100);
    }

    private void onDeleteCommentRequested(final Comment comment) {
        String accessToken = VKSdk.getAccessToken().accessToken;
        setItemClickEnabled(comment, false);
        getAdapter().notifyItemChanged(comment);
        getRequestManager().deleteComment(accessToken, comment.id,
                new FinishListenerShowingToastOnError(getContext()) {
            @Override
            public void onSuccess() {
                getAdapter().removeItem(comment);
                getDisabledItems().remove(comment);
            }

            @Override
            public void onError() {
                setItemClickEnabled(comment, true);
                getAdapter().notifyItemChanged(comment);
            }
        });
    }

    public String getCommentText(String text) {
        return text.replaceAll("\\n{2,}", "\n\n");
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.comments);
    }
}
