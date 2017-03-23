package com.azazai.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.azazai.EventsApp;
import com.azazai.R;
import com.azazai.network.RequestManager;
import com.utilsframework.android.fragments.LegacyPageLoadingFragment;
import com.utilsframework.android.fragments.PageLoadingFragment;
import com.utilsframework.android.navdrawer.FragmentsNavigationInterface;
import com.utilsframework.android.navdrawer.NavigationActivity;

import java.io.IOException;

/**
 * Created by CM on 7/2/2015.
 */
public abstract class AbstractPageLoadingFragment<Data> extends LegacyPageLoadingFragment<Data> {
    @Override
    protected RequestManager obtainRequestManager() {
        return EventsApp.getInstance().createRequestManager();
    }

    protected FragmentsNavigationInterface getNavigationActivityInterface() {
        return (FragmentsNavigationInterface) getActivity();
    }

    protected NavigationActivity getNavigationActivity() {
        return (NavigationActivity) getActivity();
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
