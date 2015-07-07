package com.khevents.network;

import com.khevents.data.Comment;
import com.utils.framework.CollectionUtils;
import com.utilsframework.android.network.RequestExecutor;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CM on 7/7/2015.
 */
class Requests {
    static List<VkUser> getUsersFromComments(RequestExecutor requestExecutor, List<Comment> comments)
            throws IOException {
        return VkApiUtils.getUsers(CollectionUtils.transformNonCopy(comments,
                new CollectionUtils.Transformer<Comment, Long>() {
                    @Override
                    public Long get(Comment comment) {
                        return comment.userId;
                    }
                }), requestExecutor);
    }

    static void updateCommentsUserData(RequestExecutor requestExecutor,
                                       List<Comment> comments) throws IOException {
        List<VkUser> users = getUsersFromComments(requestExecutor, comments);
        Iterator<VkUser> userIterator = users.iterator();
        for (Comment comment : comments) {
            VkUser next = userIterator.next();
            comment.userName = next.name + " " + next.lastName;
            comment.avatar = next.avatar;
        }
    }
}
