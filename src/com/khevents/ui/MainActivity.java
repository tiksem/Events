package com.khevents.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.khevents.EventsApp;
import com.khevents.Level;
import com.khevents.R;
import com.khevents.data.Event;
import com.khevents.ui.fragments.*;
import com.khevents.vk.VkInitManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;
import com.utilsframework.android.social.SocialUtils;
import com.vk.sdk.VKUIHelper;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;

public class MainActivity extends NavigationDrawerActivity {
    public static final String NOTIFICATION_EVENT = "NOTIFICATION_EVENT";

    public static final int SUBSCRIBED_EVENTS_TAB = 0;
    private static final int CREATED_EVENTS_TAB = 1;

    private static boolean isRunning = false;
    private Event commentNotificationEvent;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        isRunning = true;

        if (EventsApp.getInstance().getCurrentUser() != null) {
            onVkInitFinished();
        } else {
            new VkInitManager(this) {
                @Override
                protected void onVkUserReached(VkUser vkUser) {
                    super.onVkUserReached(vkUser);
                    onVkInitFinished();
                }
            }.execute(false);
        }
    }

    @Override
    protected void onPreCreate() {
        super.onPreCreate();
        commentNotificationEvent = getIntent().getParcelableExtra(NOTIFICATION_EVENT);
    }

    private void onVkInitFinished() {
        setupNavigationHeader();

        if (commentNotificationEvent != null) {
            if (!commentNotificationEvent.isCanceled) {
                selectTab(CREATED_EVENTS_TAB);
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.executePendingTransactions();
            Fragment fragment = EventFragment.create(commentNotificationEvent);
            replaceFragment(fragment, Level.EVENT_PAGE);
        }
    }

    @Override
    protected int getNavigationMenuId() {
        return R.menu.navigation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        isRunning = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected int getCurrentSelectedNavigationItemId() {
        if (commentNotificationEvent != null) {
            return R.id.my_events;
        }

        return R.id.events;
    }

    @Override
    public Fragment createFragmentBySelectedItem(int selectedItemId, int tabIndex, int navigationLevel) {
        if (navigationLevel == 0) {
            if (selectedItemId == R.id.events) {
                return new AllEventsListFragment();
            } else if(selectedItemId == R.id.tags) {
                return new TagsListFragment();
            } else if(selectedItemId == R.id.my_events) {
                if (tabIndex == SUBSCRIBED_EVENTS_TAB) {
                    return new SubscribedUserEventsListFragment();
                } else if(tabIndex == CREATED_EVENTS_TAB) {
                    return new CreatedUserEventsListFragment();
                }
            }
        }

        throw new RuntimeException("Invalid fragment request");
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabLayout.Tab tab) {
        if (currentSelectedItem == R.id.my_events) {
            if (tabIndex == 0) {
                tab.setText(R.string.subscribed);
            } else {
                tab.setText(R.string.created);
            }
        }
    }

    @Override
    public int getTabsCount(int selectedItemId, int navigationLevel) {
        if (navigationLevel == 0) {
            if (selectedItemId == R.id.my_events) {
                return 2;
            }
        }

        return 1;
    }

    @Override
    protected String getActionBarTitle(int selectedItemId, int tabIndex, int navigationLevel) {
        if (selectedItemId == R.id.tags) {
            return getString(R.string.tags);
        } else if(selectedItemId == R.id.my_events) {
            return getString(R.string.my_events);
        } else if(selectedItemId == R.id.events) {
            return getString(R.string.events);
        }

        return super.getActionBarTitle(selectedItemId, tabIndex, navigationLevel);
    }

    public void setupNavigationHeader() {
        NavigationView navigationView = getNavigationView();
        View header = View.inflate(this, R.layout.header, null);
        ImageView avatar = (ImageView) header.findViewById(R.id.avatar);
        TextView userName = (TextView) header.findViewById(R.id.userName);
        VkUser currentUser = EventsApp.getInstance().getCurrentUser();
        ImageLoader.getInstance().displayImage(currentUser.avatar, avatar);
        userName.setText(currentUser.name + " " + currentUser.lastName);
        navigationView.addHeaderView(header);

        View.OnClickListener openProfileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialUtils.openVkUserProfile(MainActivity.this, currentUser.id);
            }
        };
        userName.setOnClickListener(openProfileClickListener);
        avatar.setOnClickListener(openProfileClickListener);

        header.findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        VkApiUtils.logout(MainActivity.this);
        finish();
        AndroidUtilities.startActivity(MainActivity.this, WelcomeActivity.class);
    }

    @Override
    protected int getToolbarLayoutId() {
        return R.layout.toolbar;
    }

    public static boolean isRunning() {
        return isRunning;
    }
}
