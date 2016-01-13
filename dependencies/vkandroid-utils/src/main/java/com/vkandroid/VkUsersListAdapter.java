package com.vkandroid;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utilsframework.android.adapters.navigation.NavigationListAdapter;

/**
 * Created by CM on 6/17/2015.
 */
public class VkUsersListAdapter extends NavigationListAdapter<VkUser, VkUserViewHolder> {
    public VkUsersListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.vk_user_list_item;
    }

    @Override
    protected VkUserViewHolder createViewHolder(View view) {
        VkUserViewHolder holder = new VkUserViewHolder();
        holder.avatar = (ImageView) view.findViewById(R.id.avatar);
        holder.name = (TextView) view.findViewById(R.id.name);
        return holder;
    }

    @Override
    protected void reuseView(VkUser vkUser, VkUserViewHolder holder, int position, View view) {
        holder.name.setText(vkUser.name + " " + vkUser.lastName);
        ImageLoader.getInstance().displayImage(vkUser.avatar, holder.avatar);
    }
}
