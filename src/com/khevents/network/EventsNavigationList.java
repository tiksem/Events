package com.khevents.network;

import com.jsonandroid.JsonAsyncNavigationList;
import com.khevents.data.Event;
import com.utilsframework.android.network.RequestExecutor;

import java.util.List;

/**
 * Created by CM on 6/21/2015.
 */
class EventsNavigationList extends JsonAsyncNavigationList<Event> {
    private boolean actualEventsLoaded = false;

    public EventsNavigationList(String rootUrl, RequestExecutor requestExecutor) {
        super(Event.class, rootUrl + "getEventsList", "events", null, requestExecutor);
    }

    @Override
    protected boolean isLastPage(List<Event> elements, int limit) {
        boolean isLastPage = super.isLastPage(elements, limit);
        if(!isLastPage) {
            return false;
        }

        if (actualEventsLoaded) {
            return true;
        } else {
            actualEventsLoaded = true;
            getArgs().put("timeOut", true);
            return false;
        }
    }
}
