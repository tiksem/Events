package com.azazai.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Request implements VkUserHolderEntity {
    public Event event;
    public long userId;
    @JsonIgnore
    public String avatar;
    @JsonIgnore
    public String userName;

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUsernameAndAvatar(String userName, String avatar) {
        this.userName = userName;
        this.avatar = avatar;
    }
}
