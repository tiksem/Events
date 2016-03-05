package com.azazai.network;

import com.utils.framework.strings.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 6/22/2015.
 */
public class EventArgs {
    public Long id;
    public String name;
    public String description;
    public int date;
    public String address;
    public int peopleNumber;
    public List<String> tags;
    public String accessToken;
    public String eventType = "public";

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("date", date);
        result.put("address", address);
        result.put("peopleNumber", peopleNumber);
        result.put("tags", Strings.join(tags, ','));
        result.put("token", accessToken);
        result.put("type", eventType);
        if (id != null) {
            result.put("id", id);
        }

        return result;
    }
}
