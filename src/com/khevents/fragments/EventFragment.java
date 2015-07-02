package com.khevents.fragments;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.data.Event;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utilsframework.android.fragments.Fragments;
import com.vkandroid.VkUser;

import java.io.IOException;

/**
 * Created by CM on 7/2/2015.
 */
public class EventFragment extends AbstractPageLoadingFragment<VkUser> {
    public static final String EVENT = "event";
    public static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();
    private Event event;

    public static EventFragment create(Event event) {
        return Fragments.createFragmentWith1Arg(new EventFragment(), EVENT, event);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        event = getArguments().getParcelable(EVENT);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.event_page;
    }

    @Override
    protected VkUser loadOnBackground() throws IOException {
        return getRequestManager().getVkUserById(event.userId);
    }

    @Override
    protected void setupContent(VkUser user, View content) {
        TextView name = (TextView) content.findViewById(R.id.name);
        name.setText(user.name + " " + user.lastName);
        ImageView avatar = (ImageView) content.findViewById(R.id.avatar);
        IMAGE_LOADER.displayImage(user.avatar, avatar);
    }
}
