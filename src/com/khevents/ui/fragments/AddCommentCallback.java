package com.khevents.ui.fragments;

import com.utilsframework.android.fragments.WeakFragmentReferenceHolder;
import com.utilsframework.android.threading.OnFinish;

import java.io.IOException;

/**
 * Created by CM on 9/18/2015.
 */
public class AddCommentCallback extends WeakFragmentReferenceHolder<CommentsFragment> implements OnFinish<IOException> {
    private String commentText;

    public AddCommentCallback(CommentsFragment fragment, String commentText) {
        super(fragment);
        this.commentText = commentText;
    }

    @Override
    public void onFinish(IOException e) {
        if (getView() != null) {
            getFragment().onCommentAdded(e, commentText);
        }
    }
}
