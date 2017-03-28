package com.azazai.adapters;

import android.content.Context;
import android.view.View;

import com.azazai.R;
import com.azazai.data.Icon;
import com.azazai.ui.CircleTransform;
import com.azazai.ui.UiUtils;
import com.squareup.picasso.Picasso;
import com.utilsframework.android.adapters.navigation.LazyLoadingListAdapter;
import com.utilsframework.android.view.GuiUtilities;

public class IconsAdapter extends LazyLoadingListAdapter<Icon, IconHolder> {
    private final Picasso picasso;

    public IconsAdapter(Context context) {
        super(context);
        picasso = Picasso.with(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.icon_item;
    }

    @Override
    protected IconHolder createViewHolder(View view, int itemViewType) {
        return new IconHolder(view);
    }

    @Override
    protected void reuseView(final Icon icon, final IconHolder holder, int position, View view) {
        final String url = icon.getUrl();
        GuiUtilities.executeWhenViewMeasuredUsingLoop(holder.icon, new Runnable() {
            @Override
            public void run() {
                UiUtils.loadEventIcon(picasso, holder.icon, icon);
            }
        });
        holder.tag.setText(icon.tag);
    }
}
