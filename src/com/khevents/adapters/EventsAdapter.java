package com.khevents.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.ui.UiUtils;
import com.utilsframework.android.adapters.navigation.NavigationListAdapter;
import com.utilsframework.android.time.TimeUtils;

/**
 * Created by CM on 6/21/2015.
 */
public class EventsAdapter extends NavigationListAdapter<Event, EventHolder> {
    public EventsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.event_item;
    }

    @Override
    protected EventHolder createViewHolder(View view) {
        EventHolder holder = new EventHolder();
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.description = (TextView) view.findViewById(R.id.description);
        holder.peopleCount = (TextView) view.findViewById(R.id.people_number);
        return holder;
    }

    @Override
    protected void reuseView(Event event, EventHolder holder, int position, View view) {
        holder.name.setText(event.name);
        UiUtils.setPeopleNumber(holder.peopleCount, event);
        holder.description.setText(event.description);
        String date = TimeUtils.getAlternativeDisplayDateTime(event.date * 1000l);
        holder.date.setText(date);
    }
}
