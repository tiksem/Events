package com.khevents.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CM on 7/1/2015.
 */
public class Tag {
    @JsonProperty(value = "tagName")
    public String name;

    @JsonProperty(value = "eventsCount")
    public int eventsCount;
}
