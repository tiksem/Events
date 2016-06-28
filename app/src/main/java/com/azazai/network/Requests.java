package com.azazai.network;

import com.azazai.data.Comment;
import com.azazai.data.UserIdProvider;
import com.azazai.data.VkUserHolderEntity;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Transformer;
import com.utils.framework.network.RequestExecutor;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CM on 7/7/2015.
 */
class Requests {
    static <T extends UserIdProvider>
    List<VkUser> getUsersFromIdProviders(RequestExecutor requestExecutor,
                                         List<T> userIdProviders)
            throws IOException {
        return VkApiUtils.getUsers(CollectionUtils.transformNonCopy(userIdProviders,
                new Transformer<T, Long>() {
                    @Override
                    public Long get(T o) {
                        return o.getUserId();
                    }
                }), requestExecutor);
    }

    static void updateUserData(RequestExecutor requestExecutor,
                               List<? extends VkUserHolderEntity> entities)
            throws IOException {
        List<VkUser> users = getUsersFromIdProviders(requestExecutor, entities);
        Iterator<VkUser> userIterator = users.iterator();
        for (VkUserHolderEntity entity : entities) {
            VkUser next = userIterator.next();
            final String userName = next.name + " " + next.lastName;
            entity.setUsernameAndAvatar(userName, next.avatar);
        }
    }
}
