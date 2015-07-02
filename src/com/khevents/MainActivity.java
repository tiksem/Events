package com.khevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import com.khevents.fragments.CreatedUserEventsListFragment;
import com.khevents.fragments.AllEventsListFragment;
import com.khevents.fragments.SubscribedUserEventsListFragment;
import com.khevents.fragments.TagsListFragment;
import com.khevents.vk.VkManager;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;
import com.utilsframework.android.threading.OnFinish;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKUIHelper;

public class MainActivity extends NavigationDrawerActivity {

    public static final int SUBSCRIBED_EVENTS_TAB = 0;
    private static final int CREATED_EVENTS_TAB = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        VkManager.getAccessToken(this, R.string.vk_login_error, new OnFinish<VKAccessToken>() {
            @Override
            public void onFinish(VKAccessToken token) {

            }
        });
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected int getCurrentSelectedNavigationItemId() {
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
        if (selectedItemId == R.id.my_events) {
            return 2;
        }

        return 1;
    }

    @Override
    protected String getActionBarTitle(int selectedItemId, int tabIndex, int navigationLevel) {
        if (selectedItemId == R.id.tags) {
            return getString(R.string.tags);
        } else if(selectedItemId == R.id.my_events) {
            return getString(R.string.my_events);
        }

        return super.getActionBarTitle(selectedItemId, tabIndex, navigationLevel);
    }
}
