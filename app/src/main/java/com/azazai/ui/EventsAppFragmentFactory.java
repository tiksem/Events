package com.azazai.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import com.azazai.R;
import com.azazai.ui.fragments.AllEventsListFragment;
import com.azazai.ui.fragments.CreatedUserEventsListFragment;
import com.azazai.ui.fragments.SubscribedUserEventsListFragment;
import com.azazai.ui.fragments.TagsListFragment;
import com.azazai.ui.fragments.UserRequestsFragment;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.TabsAdapter;

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
            } else if(selectedItemId == R.id.requests) {
                return new UserRequestsFragment();
            }
        }

        throw new RuntimeException("Invalid fragment request");
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabsAdapter.Tab tab) {
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
