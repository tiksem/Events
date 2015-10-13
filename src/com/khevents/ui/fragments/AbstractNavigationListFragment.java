package com.khevents.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import com.jsonutils.RequestException;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.network.RequestManager;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.navdrawer.NavigationActivityInterface;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;
import com.utilsframework.android.navigation.NavigationListFragment;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class AbstractNavigationListFragment<T> extends NavigationListFragment<T, RequestManager> {
    public static final String UPDATE_ACTION = "com.khevents.ui.fragments.AbstractNavigationListFragment.update";
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver updateReceiver;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (useUpdateBroadcastReceiver()) {
            localBroadcastManager = LocalBroadcastManager.getInstance(activity);
            updateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Fragments.executeWhenViewCreated(AbstractNavigationListFragment.this,
                            new GuiUtilities.OnViewCreated() {
                        @Override
                        public void onViewCreated(View view) {
                            updateNavigationListWithLastFilter();
                        }
                    });
                }
            };
            localBroadcastManager.registerReceiver(updateReceiver, new IntentFilter(UPDATE_ACTION));
        }
    }

    @Override
    protected RequestManager obtainRequestManager() {
        return EventsApp.getInstance().createRequestManager();
    }

    @Override
    protected int getListResourceId() {
        return R.id.list;
    }

    @Override
    protected int getLoadingResourceId() {
        return R.id.loading;
    }

    @Override
    protected int getNoInternetConnectionViewId() {
        return R.id.no_connection;
    }

    @Override
    protected int getRetryLoadingButtonId() {
        return R.id.retry;
    }

    public NavigationActivityInterface getNavigationActivityInterface() {
        return (NavigationActivityInterface) getActivity();
    }

    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        getNavigationActivityInterface().replaceFragment(newFragment, navigationLevel);
    }

    protected boolean useUpdateBroadcastReceiver() {
        return false;
    }

    public static void update(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(UPDATE_ACTION));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (localBroadcastManager != null) {
            localBroadcastManager.unregisterReceiver(updateReceiver);
        }
    }
}
