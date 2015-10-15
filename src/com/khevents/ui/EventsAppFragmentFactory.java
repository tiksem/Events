package com.khevents.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import com.khevents.R;
import com.khevents.ui.fragments.AllEventsListFragment;
import com.khevents.ui.fragments.CreatedUserEventsListFragment;
import com.khevents.ui.fragments.SubscribedUserEventsListFragment;
import com.khevents.ui.fragments.TagsListFragment;
import com.utilsframework.android.navdrawer.FragmentFactory;

/**
 * Created by stykhonenko on 15.10.15.
 */
public class EventsAppFragmentFactory implements FragmentFactory {
    public static final int SUBSCRIBED_EVENTS_TAB = 0;
    public static final int CREATED_EVENTS_TAB = 1;

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
}
