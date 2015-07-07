package com.khevents.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by CM on 7/7/2015.
 */
public class Comment {
    public long userId;
    public String text;
    public int date;
    @JsonIgnore
    public String userName;
}
