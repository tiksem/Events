package com.khevents.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.khevents.EventsApp;
import com.khevents.Level;
import com.khevents.R;
import com.khevents.data.Comment;
import com.khevents.data.Event;
import com.khevents.network.RequestManager;
import com.khevents.ui.UiUtils;
import com.khevents.vk.VkInitManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.framework.CollectionUtils;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.google.GoogleMaps;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.social.SocialUtils;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.time.TimeUtils;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.OnYes;
import com.utilsframework.android.view.Toasts;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkUser;

import java.io.IOException;
import java.util.List;

/**
 * Created by CM on 7/2/2015.
 */
public class EventFragment extends AbstractPageLoadingFragment<VkUser> implements ActionBarTitleProvider {
    public static final String EVENT = "event";
    public static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();
    public static final int TOP_COMMENTS_COUNT = 3;
    private Event event;
    private Button subscribeButton;
    private List<Comment> topComments;
    private TextView peopleNumber;

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
        RequestManager requestManager = getRequestManager();
        event.isSubscribed = requestManager.isSubscribed(event.id, VKSdk.getAccessToken().accessToken);
        topComments = requestManager.getTopComments(event.id, TOP_COMMENTS_COUNT);
        return requestManager.getVkUserById(event.userId);
    }

    @Override
    protected void setupContent(VkUser user, View content) {
        TextView userName = (TextView) content.findViewById(R.id.userName);
        userName.setText(user.name + " " + user.lastName);

        TextView name = (TextView) content.findViewById(R.id.name);
        name.setText(event.name);

        TextView description = (TextView) content.findViewById(R.id.description);
        description.setText(event.description);

        peopleNumber = (TextView) content.findViewById(R.id.people_number);
        updatePeopleNumber();

        TextView dateView = (TextView) content.findViewById(R.id.date);
        String date = TimeUtils.getAlternativeDisplayDateTime(getActivity(), event.date * 1000l);
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

        setupCommentsList(content);

        content.findViewById(R.id.add_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComments(true);
            }
        });

        View.OnClickListener profileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialUtils.openVkUserProfile(getActivity(), event.userId);
            }
        };
        content.findViewById(R.id.userName).setOnClickListener(profileClickListener);
        content.findViewById(R.id.avatar).setOnClickListener(profileClickListener);

        TextView address = (TextView) content.findViewById(R.id.address);
        address.setText(event.address);
        content.findViewById(R.id.address_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleMaps.startSearchAddressActivity(getActivity(), event.address);
            }
        });
    }

    private void updatePeopleNumber() {
        UiUtils.setPeopleNumber(peopleNumber, event);
    }

    private void showSubscribers() {
        replaceFragment(EventSubscribersListFragment.create(event.id), Level.SUBSCRIBERS);
    }

    private int getSubscribeButtonText() {
        return event.isSubscribed ? R.string.i_will_not_go : R.string.i_will_go;
    }

    private void initSubscribeButton(View content) {
        subscribeButton = (Button) content.findViewById(R.id.subscribe);
        if (event.date * 1000l <= System.currentTimeMillis()) {
            subscribeButton.setVisibility(View.GONE);
            content.findViewById(R.id.timeout_text).setVisibility(View.VISIBLE);
            return;
        }

        if (event.userId != Long.valueOf(VKSdk.getAccessToken().userId)) {
            subscribeButton.setText(getSubscribeButtonText());
            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSubscribe();
                }
            });
        } else {
            subscribeButton.setText(R.string.cancel_event);
            subscribeButton.setTextAppearance(getActivity(), R.style.cancelButton);
            subscribeButton.setBackgroundResource(R.drawable.cancel_button);
            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCancelEvent();
                }
            });
        }
    }

    private void requestCancelEvent() {
        Alerts.YesNoAlertSettings settings = new Alerts.YesNoAlertSettings();
        settings.message = getString(R.string.cancel_event_confirm_message);
        settings.onYes = new OnYes() {
            @Override
            public void onYes() {
                cancelEvent();
            }
        };
        Alerts.showYesNoAlert(getActivity(), settings);
    }

    private void cancelEvent() {
        ProgressDialog progressDialog = Alerts.showCircleProgressDialog(getActivity(), R.string.please_wait);
        getRequestManager().cancelEventAsync(event.id, VKSdk.getAccessToken().accessToken,
                new OnFinish<IOException>() {
                    @Override
                    public void onFinish(IOException e) {
                        FragmentActivity activity = getActivity();
                        if (e == null) {
                            Toasts.message(activity, R.string.event_canceled);
                            EventsListFragment.update(activity);
                            activity.onBackPressed();
                        } else {
                            Toasts.error(activity, R.string.no_internet_connection);
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    private void toggleSubscribe() {
        subscribeButton.setEnabled(false);
        subscribeButton.setText(R.string.please_wait);
        getRequestManager().subscribe(event.id, VKSdk.getAccessToken().accessToken,
                new OnFinish<IOException>() {
                    @Override
                    public void onFinish(IOException e) {
                        subscribeButton.setEnabled(true);
                        if (e != null) {
                            subscribeButton.setText(getSubscribeButtonText());
                        } else {
                            event.isSubscribed = !event.isSubscribed;
                            int text = getSubscribeButtonText();
                            if (text == R.string.i_will_not_go) {
                                Toasts.message(getActivity(), R.string.subscribe_message);
                            }

                            subscribeButton.setText(text);
                            if (event.isSubscribed) {
                                event.subscribersCount++;
                            } else {
                                event.subscribersCount--;
                            }
                            updatePeopleNumber();
                        }
                    }
                });
    }

    protected void setupCommentsList(View content) {
        LinearLayout commentsView = (LinearLayout) content.findViewById(R.id.comments);
        for (Comment comment : topComments) {
            View view = UiUtils.createTopCommentLayout(getActivity(), comment);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            commentsView.addView(view);
        }

        commentsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComments(false);
            }
        });

        TextView viewAllComments = (TextView) commentsView.findViewById(R.id.view_all_comments_text);
        if (topComments.isEmpty()) {
            viewAllComments.setText(R.string.no_comments);
        }
    }

    private void openComments(boolean addCommentFocus) {
        replaceFragment(CommentsFragment.create(event.id, topComments, addCommentFocus), Level.COMMENTS);
    }

    public void addTopComment(Comment comment) {
        topComments.add(0, comment);
        if (topComments.size() > TOP_COMMENTS_COUNT) {
            CollectionUtils.removeLast(topComments);
        }
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.event);
    }
}
