package com.azazai.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.*;
import com.azazai.Level;
import com.azazai.R;
import com.azazai.data.Comment;
import com.azazai.data.Event;
import com.azazai.network.RequestManager;
import com.azazai.ui.CreateEventActivity;
import com.azazai.ui.UiUtils;
import com.squareup.picasso.Picasso;
import com.utils.framework.Lists;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.google.GoogleMaps;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.social.SocialUtils;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.time.TimeUtils;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.OnYes;
import com.utilsframework.android.view.Toasts;
import com.utilsframework.android.view.YesNoAlertSettings;
import com.vk.sdk.VKSdk;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

import java.io.IOException;
import java.util.List;

/**
 * Created by CM on 7/2/2015.
 */
public class EventFragment extends AbstractPageLoadingFragment<VkUser> implements ActionBarTitleProvider {
    public static final String EVENT = "event";
    public static final int TOP_COMMENTS_COUNT = 3;
    private static final int EDIT_EVENT_CODE = 123;
    private Event event;
    private Button subscribeButton;
    private List<Comment> topComments;
    private TextView peopleNumber;
    private ProgressDialog progressDialog;
    private MenuItem editEventMenuItem;
    private int requestsCount;

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
    protected VkUser loadOnBackground(RequestManager requestManager) throws IOException {
        long userId = VkApiUtils.getUserId();
        event.isSubscribed = requestManager.isSubscribed(event.id, userId);
        topComments = requestManager.getTopComments(event.id, TOP_COMMENTS_COUNT + 1);
        if (shouldShowRequestsLayout()) {
            requestsCount = requestManager.getRequestsCount(event.id);
        }
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
        Picasso.with(getActivity()).load(user.avatar).into(avatar);

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

        editEventMenuItem.setVisible(event.userId == getVkUserId());

        editEventMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                editEvent();
                return true;
            }
        });

        if (shouldShowRequestsLayout()) {
            final View requestsLayout = content.findViewById(R.id.requests_layout);
            requestsLayout.setVisibility(View.VISIBLE);
            requestsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragment(EventRequestsFragment.create(event), Level.EVENT_REQUESTS);
                }
            });
            TextView requestsNumberView = (TextView) content.findViewById(R.id.requests_number);
            requestsNumberView.setText(String.valueOf(requestsCount));
        }
    }

    private boolean shouldShowRequestsLayout() {
        return event.isPrivate && event.userId == VkApiUtils.getUserId();
    }

    private void editEvent() {
        CreateEventActivity.edit(this, EDIT_EVENT_CODE, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_EVENT_CODE && resultCode == Activity.RESULT_OK) {
            reloadPage();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.event, menu);
        editEventMenuItem = menu.findItem(R.id.edit);
        super.onCreateOptionsMenu(menu, inflater);
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

        if (event.isCanceled || event.date * 1000l <= System.currentTimeMillis()) {
            subscribeButton.setVisibility(View.GONE);
            TextView text = (TextView) content.findViewById(R.id.timeout_text);
            text.setVisibility(View.VISIBLE);
            if (event.isCanceled) {
                text.setText(R.string.event_canceled_page_text);
            }

            return;
        }

        if (event.userId != getVkUserId()) {
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

    private long getVkUserId() {
        return Long.valueOf(VKSdk.getAccessToken().userId);
    }

    private void requestCancelEvent() {
        Alerts.showYesNoAlert(new YesNoAlertSettings(getContext()) {
            {
                setMessage(R.string.cancel_event_confirm_message);
            }

            @Override
            public void onYes() {
                cancelEvent();
            }
        });
    }

    private void cancelEvent() {
        progressDialog = Alerts.showCircleProgressDialog(getActivity(), R.string.please_wait);
        getRequestManager().cancelEventAsync(event.id, VKSdk.getAccessToken().accessToken,
                new OnFinish<IOException>() {
                    @Override
                    public void onFinish(IOException e) {
                        onEventCanceled(e);
                    }
                });
    }

    private void onEventCanceled(IOException e) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (e == null) {
                Toasts.toast(activity, R.string.event_canceled);
                EventsListFragment.update(activity);
                activity.onBackPressed();
            } else {
                Toasts.toast(activity, R.string.no_internet_connection);
            }

            progressDialog.dismiss();
        }
    }

    private void toggleSubscribe() {
        subscribeButton.setEnabled(false);
        subscribeButton.setText(R.string.please_wait);
        getRequestManager().subscribe(event.id, VKSdk.getAccessToken().accessToken, new SubscribeToggleCallback(this));
    }

    void onSubscribeToggleFinished(IOException e) {
        subscribeButton.setEnabled(true);
        if (e != null) {
            subscribeButton.setText(getSubscribeButtonText());
        } else {
            event.isSubscribed = !event.isSubscribed;
            int text = getSubscribeButtonText();
            if (text == R.string.i_will_not_go) {
                Toasts.toast(getActivity(), R.string.subscribe_message);
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

    protected void setupCommentsList(View content) {
        LinearLayout commentsView = (LinearLayout) content.findViewById(R.id.comments);
        commentsView.removeViews(1, commentsView.getChildCount() - 1);

        for (Comment comment : topComments.size() <= TOP_COMMENTS_COUNT ? topComments :
                topComments.subList(0, TOP_COMMENTS_COUNT)) {
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
        } else if(topComments.size() <= TOP_COMMENTS_COUNT) {
            viewAllComments.setVisibility(View.GONE);
        }
    }

    private void openComments(boolean addCommentFocus) {
        replaceFragment(CommentsFragment.create(event.id, topComments, addCommentFocus), Level.COMMENTS);
    }

    public void addTopComment(Comment comment) {
        topComments.add(0, comment);
        if (topComments.size() > TOP_COMMENTS_COUNT + 1) {
            Lists.removeLast(topComments);
        }
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.event);
    }
}
