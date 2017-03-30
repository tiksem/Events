package com.vkandroid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.sdk.VKSdk;

/**
 * Created by CM on 6/16/2015.
 */
public class VkUser {
    @JsonProperty(value = "photo_100")
    public String avatar;
    @JsonProperty(value = "uid")
    public long id;
    @JsonProperty("first_name")
    public String name;
    @JsonProperty("last_name")
    public String lastName;

    public String getFullName() {
        return name + " " + lastName;
    }

    public static long getCurrentUserId() {
        return Long.valueOf(VKSdk.getAccessToken().userId);
    }
}
