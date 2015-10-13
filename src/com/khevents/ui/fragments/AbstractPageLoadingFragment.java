package com.khevents.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.khevents.EventsApp;
import com.khevents.R;
import com.khevents.network.RequestManager;
import com.utilsframework.android.fragments.PageLoadingFragment;
import com.utilsframework.android.navdrawer.NavigationActivityInterface;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;

import java.io.IOException;

/**
 * Created by CM on 7/2/2015.
 */
public abstract class AbstractPageLoadingFragment<Data> extends PageLoadingFragment<RequestManager, Data> {
    @Override
    protected RequestManager obtainRequestManager() {
        return EventsApp.getInstance().createRequestManager();
    }

    protected NavigationActivityInterface getNavigationActivityInterface() {
        return (NavigationActivityInterface) getActivity();
    }

    protected NavigationDrawerActivity getNavigationDrawerActivity() {
        return (NavigationDrawerActivity) getActivity();
    }

    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        getNavigationActivityInterface().replaceFragment(newFragment, navigationLevel);
    }

    @Override
    protected int getNoConnectionLayoutId() {
        return R.layout.no_connection_layout;
    }

    @Override
    protected int getRetryLoadingButtonId() {
        return R.id.retry;
    }
}
