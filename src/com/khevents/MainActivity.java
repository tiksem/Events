package com.khevents;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;

public class MainActivity extends NavigationDrawerActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getNavigationLayoutId() {
        return R.layout.navigation;
    }

    @Override
    protected int getCurrentSelectedNavigationItemId() {
        return R.id.events;
    }

    @Override
    public Fragment createFragmentBySelectedItem(int selectedItemId, int tabIndex, int navigationLevel) {
        if (selectedItemId == R.id.events) {
            return new EventsListFragment();
        }

        return null;
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, ActionBar.Tab tab) {

    }

    @Override
    public int getTabsCount(int selectedItemId, int navigationLevel) {
        return 1;
    }
}
