package com.vkandroid;

import com.utilsframework.android.network.GetRequestExecutor;
import com.utilsframework.android.network.RequestExecutor;
import com.jsonutils.Json;
import com.utils.framework.ArrayUtils;
import com.utils.framework.strings.Strings;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by CM on 6/17/2015.
 */
public class VkApiUtils {
    public static String getUsersRequestUrl(List<Long> ides, List<String> fields) {
        if (ides.isEmpty()) {
            throw new IllegalArgumentException("Empty ides");
        }

        String url = "https://api.vk.com/method/users.get?user_ids=" + Strings.joinObjects(ides, ',');
        if (!fields.isEmpty()) {
            url += "&fields=" + Strings.join(fields, ',');
        }

        return url;
    }

    public static List<VkUser> getUsers(List<Long> ides, RequestExecutor requestExecutor)
            throws IOException {
        String vkUrl = VkApiUtils.getUsersRequestUrl(ides, Collections.singletonList("photo_100"));
        String response = requestExecutor.executeRequest(vkUrl, null);
        return Json.readList(response, "response", VkUser.class);
    }

    public static List<VkUser> getUsers(List<Long> ides)
            throws IOException {
        return getUsers(ides, new GetRequestExecutor());
    }

    public static List<VkUser> getUsers(long... ides) throws IOException {
        return getUsers(ArrayUtils.asList(ides));
    }
}
