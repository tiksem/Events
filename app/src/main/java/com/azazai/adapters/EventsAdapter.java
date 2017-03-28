package com.azazai.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.ui.UiUtils;
import com.utilsframework.android.adapters.navigation.LazyLoadingListAdapter;
import com.utilsframework.android.time.TimeUtils;

/**
 * Created by CM on 6/21/2015.
 */
public class EventsAdapter extends LazyLoadingListAdapter<Object, EventHolder> {
    protected static final int VIEW_TYPES_COUNT = LazyLoadingListAdapter.VIEW_TYPES_COUNT + 1;
    private static final int OUT_OF_DATE_EVENTS_HEADER = LazyLoadingListAdapter.VIEW_TYPES_COUNT;

    public EventsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (getElement(position) == Event.OUT_OF_DATE_EVENTS_HEADER) {
            return OUT_OF_DATE_EVENTS_HEADER;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES_COUNT;
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        if (viewType == OUT_OF_DATE_EVENTS_HEADER) {
            return R.layout.out_of_date_events_header;
        }

        return R.layout.event_item;
    }

    @Override
    protected EventHolder createViewHolder(View view, int position) {
        return new EventHolder(view);
    }

    @Override
    protected void reuseView(Object object, EventHolder holder, int position, View view) {
        if (object instanceof Event) {
            reuseEventView((Event) object, holder, view);
        }
    }

    private void reuseEventView(Event event, EventHolder holder, View view) {
        holder.name.setText(event.name);
        UiUtils.setPeopleNumber(holder.peopleCount, event);
        holder.description.setText(event.description);
        long dateMillis = event.date * 1000l;
        String date = TimeUtils.getAlternativeDisplayDateTime(view.getContext(), dateMillis);
        holder.date.setText(date);
    }

    @Override
    public boolean isEnabled(int position) {
        if (getElement(position) == Event.OUT_OF_DATE_EVENTS_HEADER) {
            return false;
        }

        return super.isEnabled(position);
    }
}
