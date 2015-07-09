package com.khevents.network;

import com.jsonandroid.JsonAsyncNavigationList;
import com.khevents.data.Event;
import com.utils.framework.network.RequestExecutor;

import java.util.List;
import java.util.Map;

/**
 * Created by CM on 6/21/2015.
 */
class EventsNavigationList extends JsonAsyncNavigationList<Event> {
    private boolean actualEventsLoaded = false;
    private int actualEventsLoadedCount = -1;

    public EventsNavigationList(String url, String jsonKey,
                                Map<String, Object> args,
                                RequestExecutor requestExecutor) {
        super(Event.class, url, jsonKey, args, requestExecutor);
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

    @Override
    protected int getOffset() {
        if (actualEventsLoaded) {
            if (actualEventsLoadedCount < 0) {
                actualEventsLoadedCount = getLoadedElementsCount();
            }

            return super.getOffset() - actualEventsLoadedCount;
        } else {
            return super.getOffset();
        }
    }
}
