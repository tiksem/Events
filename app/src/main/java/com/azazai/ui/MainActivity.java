package com.azazai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.azazai.EventsApp;
import com.azazai.Level;
import com.azazai.R;
import com.azazai.data.Event;
import com.azazai.debug.DebugUtils;
import com.azazai.ui.fragments.EventFragment;
import com.squareup.picasso.Picasso;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.NavigationDrawerMenuActivity;
import com.utilsframework.android.social.SocialUtils;
import com.vk.sdk.VKUIHelper;
import com.vkandroid.VkUser;

public class MainActivity extends NavigationDrawerMenuActivity {
    public static final String NOTIFICATION_EVENT = "NOTIFICATION_EVENT";

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

        setupNavigationHeader();

        if (commentNotificationEvent != null) {
            if (!commentNotificationEvent.isCanceled) {
                selectTab(EventsAppFragmentFactory.CREATED_EVENTS_TAB);
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.executePendingTransactions();
            Fragment fragment = EventFragment.create(commentNotificationEvent);
            replaceFragment(fragment, Level.EVENT_PAGE);
        }
    }

    @Override
    protected void onPreCreate() {
        super.onPreCreate();
        commentNotificationEvent = getIntent().getParcelableExtra(NOTIFICATION_EVENT);
    }

    @Override
    protected int getMenuId() {
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
    protected String getActionBarTitle(int selectedItemId, int tabIndex, int navigationLevel) {
        if (selectedItemId == R.id.tags) {
            return getString(R.string.tags);
        } else if(selectedItemId == R.id.my_events) {
            if (navigationLevel == Level.FIND_EVENTS) {
                return getString(R.string.events);
            }

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
        final VkUser currentUser = EventsApp.getInstance().getCurrentUser();
        Picasso.with(this).load(currentUser.avatar).into(avatar);
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
        EventsApp.getInstance().logout();
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

    @Override
    protected FragmentFactory createFragmentFactory() {
        return new EventsAppFragmentFactory();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (EventsApp.DEBUG) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                DebugUtils.showInternetConnectionDialog(this);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
