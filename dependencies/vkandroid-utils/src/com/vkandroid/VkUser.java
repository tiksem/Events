package com.vkandroid;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
