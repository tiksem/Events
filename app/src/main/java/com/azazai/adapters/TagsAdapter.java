package com.azazai.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.azazai.R;
import com.azazai.data.Tag;
import com.utilsframework.android.adapters.navigation.LazyLoadingListAdapter;

/**
 * Created by CM on 7/1/2015.
 */
public class TagsAdapter extends LazyLoadingListAdapter<Tag, TagHolder> {
    public TagsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.tag_item;
    }

    @Override
    protected TagHolder createViewHolder(View view, int position) {
        TagHolder holder = new TagHolder();
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.eventsCount = (TextView) view.findViewById(R.id.events_count);

        return holder;
    }

    @Override
    protected void reuseView(Tag tag, TagHolder holder, int position, View view) {
        holder.name.setText(tag.name);
        holder.eventsCount.setText(view.getContext().getResources().getQuantityString(
                R.plurals.events, tag.eventsCount, tag.eventsCount));
    }
}
