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
public class EventsAdapter extends LazyLoadingListAdapter<Event, EventHolder> {
    public EventsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.event_item;
    }

    @Override
    protected EventHolder createViewHolder(View view, int position) {
        return new EventHolder(view);
    }

    @Override
    protected void reuseView(Event event, EventHolder holder, int position, View view) {
        holder.name.setText(event.name);
        UiUtils.setPeopleNumber(holder.peopleCount, event);
        holder.description.setText(event.description);
        long dateMillis = event.date * 1000l;
        String date = TimeUtils.getAlternativeDisplayDateTime(view.getContext(), dateMillis);
        holder.date.setText(date);
    }
}
