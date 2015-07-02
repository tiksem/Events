package com.khevents.fragments;

import android.support.v4.app.Fragment;
import com.jsonutils.RequestException;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.network.RequestManager;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;
import com.utilsframework.android.navigation.NavigationListFragment;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class AbstractNavigationListFragment<T> extends NavigationListFragment<T, RequestManager> {
    @Override
    protected RequestManager obtainRequestManager() {
        return EventsApp.getInstance().getRequestManager();
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
    protected boolean shouldOverrideHandlingErrorBehavior(Throwable e) {
        if (e instanceof RequestException) {
            //throw new RuntimeException(e);
        }

        return false;
    }

    public NavigationDrawerActivity getNavigationActivity() {
        return (NavigationDrawerActivity) getActivity();
    }

    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        getNavigationActivity().replaceFragment(newFragment, navigationLevel);
    }

    public void updateActionBarTitle() {
        getNavigationActivity().updateActionBarTitle();
    }
}
