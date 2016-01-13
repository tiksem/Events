package com.azazai.network;

import com.jsonandroid.JsonAsyncNavigationList;
import com.azazai.data.Comment;
import com.utils.framework.KeyProvider;
import com.utils.framework.network.RequestExecutor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 7/7/2015.
 */
class CommentsNavigationList extends JsonAsyncNavigationList<Comment> {
    public CommentsNavigationList(String rootUrl, long eventId,
                                  List<Comment> topComments,
                                  RequestExecutor requestExecutor, RequestManager requestManager) {
        super(Comment.class, rootUrl + "getCommentsList", "Comments",
                Collections.<String, Object>singletonMap("id", eventId),
                requestExecutor, requestManager, topComments);
    }

    @Override
    protected List<Comment> getElements(String url, Map<String, Object> args,
                                        RequestExecutor requestExecutor,
                                        Class<Comment> aClass) throws IOException {
        List<Comment> comments = super.getElements(url, args, requestExecutor, aClass);
        Requests.updateCommentsUserData(requestExecutor, comments);
        return comments;
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
