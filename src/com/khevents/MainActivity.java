package com.khevents;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.khevents.vk.VkManager;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;
import com.utilsframework.android.view.UiMessages;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;

public class MainActivity extends NavigationDrawerActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
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
        } else if(selectedItemId == R.id.tags) {
            return new TagsListFragment();
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
