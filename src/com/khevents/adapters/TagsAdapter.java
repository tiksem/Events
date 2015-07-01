package com.khevents.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.khevents.R;
import com.khevents.data.Tag;
import com.utilsframework.android.adapters.navigation.NavigationListAdapter;

/**
 * Created by CM on 7/1/2015.
 */
public class TagsAdapter extends NavigationListAdapter<Tag, TagHolder> {
    public TagsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.tag;
    }

    @Override
    protected TagHolder createViewHolder(View view) {
        TagHolder holder = new TagHolder();
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.eventsCount = (TextView) view.findViewById(R.id.events_count);

        return holder;
    }

    @Override
    protected void reuseView(Tag tag, TagHolder holder, int position, View view) {
        holder.name.setText(tag.name);
        holder.eventsCount.setText(tag.eventsCount + " " + view.getContext().getString(
                R.string.events_count_ending));
    }
}
