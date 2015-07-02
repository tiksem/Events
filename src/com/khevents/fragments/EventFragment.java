package com.khevents.fragments;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.VkUsersListActivity;
import com.khevents.data.Event;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.time.TimeUtils;
import com.utilsframework.android.view.UiMessages;
import com.vk.sdk.VKSdk;
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
        TextView userName = (TextView) content.findViewById(R.id.userName);
        userName.setText(user.name + " " + user.lastName);

        TextView name = (TextView) content.findViewById(R.id.name);
        name.setText(event.name);

        TextView description = (TextView) content.findViewById(R.id.description);
        description.setText(event.description);

        TextView peopleNumber = (TextView) content.findViewById(R.id.people_number);
        peopleNumber.setText(event.subscribersCount + "/" + event.peopleNumber);

        TextView dateView = (TextView) content.findViewById(R.id.date);
        String date = TimeUtils.getAlternativeDisplayDateTime(event.date * 1000l);
        dateView.setText(getString(R.string.event_date_title) + " " + date);

        ImageView avatar = (ImageView) content.findViewById(R.id.avatar);
        IMAGE_LOADER.displayImage(user.avatar, avatar);

        initSubscribeButton(content);

        content.findViewById(R.id.people_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubscribers();
            }
        });
    }

    private void showSubscribers() {
        UiMessages.message(getActivity(), "Subscribers are shown");// TODO remove this
    }

    private void initSubscribeButton(View content) {
        Button subscribeButton = (Button) content.findViewById(R.id.subscribe);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeButton.setEnabled(false);
                CharSequence text = subscribeButton.getText();
                subscribeButton.setText(R.string.please_wait);
                getRequestManager().subscribe(event.id, VKSdk.getAccessToken().accessToken,
                        new OnFinish<IOException>() {
                    @Override
                    public void onFinish(IOException e) {
                        subscribeButton.setEnabled(true);
                        if (e != null) {
                            subscribeButton.setText(text);
                        } else {
                            if (text.equals(getString(R.string.i_will_go))) {
                                subscribeButton.setText(R.string.i_will_not_go);
                                UiMessages.message(getActivity(), R.string.subscribe_message);
                            } else {
                                subscribeButton.setText(R.string.i_will_go);
                            }
                        }
                    }
                });
            }
        });
    }
}
