package com.azazai.network;

import com.azazai.data.Comment;
import com.utils.framework.KeyProvider;
import com.utils.framework.network.RequestExecutor;

import java.util.Collections;
import java.util.List;

/**
 * Created by CM on 7/7/2015.
 */
class CommentsLazyLoadingList extends UserHolderEntityLazyLoadingList<Comment> {
    public CommentsLazyLoadingList(String rootUrl, long eventId,
                                   List<Comment> topComments,
                                   RequestExecutor requestExecutor,
                                   RequestManager requestManager) {
        super(Comment.class, rootUrl + "getCommentsList", "Comments",
                Collections.<String, Object>singletonMap("id", eventId),
                requestExecutor, requestManager, topComments);
    }

    @Override
    protected KeyProvider<Object, Comment> getKeyProvider() {
        return new KeyProvider<Object, Comment>() {
            @Override
            public Object getKey(Comment comment) {
                return comment.date;
            }
        };
    }
}
