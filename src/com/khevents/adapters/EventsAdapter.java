package com.khevents.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
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
        holder.timeoutIcon = (ImageView) view.findViewById(R.id.timeout_icon);
        return holder;
    }

    @Override
    protected void reuseView(Event event, EventHolder holder, int position, View view) {
        holder.name.setText(event.name);
        UiUtils.setPeopleNumber(holder.peopleCount, event);
        holder.description.setText(event.description);
        long dateMillis = event.date * 1000l;
        String date = TimeUtils.getAlternativeDisplayDateTime(view.getContext(), dateMillis);
        holder.date.setText(date);
        holder.timeoutIcon.setVisibility(System.currentTimeMillis() < dateMillis ? View.GONE : View.VISIBLE);
    }
}
