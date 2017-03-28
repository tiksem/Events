package com.azazai.network;

import com.jsonandroid.JsonAsyncLazyLoadingList;
import com.azazai.data.Event;
import com.utils.framework.KeyProvider;
import com.utils.framework.network.RequestExecutor;

import java.util.List;
import java.util.Map;

/**
 * Created by CM on 6/21/2015.
 */
class EventsLazyLoadingList extends JsonAsyncLazyLoadingList<Object> {
    private boolean actualEventsLoaded = false;
    private int actualPagesLoadedCount = -1;
    private boolean outOfDateEventsHeaderAdded = false;

    public EventsLazyLoadingList(String url, String jsonKey,
                                 Map<String, Object> args,
                                 RequestExecutor requestExecutor,
                                 RequestManager requestManager) {
        super(Event.class, url, jsonKey, args, requestExecutor, requestManager);
    }

    @Override
    protected boolean isLastPage(List<Object> elements, int limit) {
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
    protected void onModifyLoadedElements(List<Object> elements) {
        if (actualEventsLoaded && !outOfDateEventsHeaderAdded && !elements.isEmpty()) {
            elements.add(0, Event.OUT_OF_DATE_EVENTS_HEADER);
            outOfDateEventsHeaderAdded = true;
        }
    }

    @Override
    protected int getOffset() {
        if (actualEventsLoaded) {
            if (actualPagesLoadedCount < 0) {
                actualPagesLoadedCount = getLoadedPagesCount();
            }

            return super.getOffset() - actualPagesLoadedCount * getLimit();
        } else {
            return super.getOffset();
        }
    }

    @Override
    protected KeyProvider<Object, Object> getKeyProvider() {
        return new KeyProvider<Object, Object>() {
            @Override
            public Object getKey(Object object) {
                if (object instanceof Event) {
                    return ((Event)object).id;
                }

                return object;
            }
        };
    }
}
