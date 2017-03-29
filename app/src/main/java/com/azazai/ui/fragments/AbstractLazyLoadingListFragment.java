package com.azazai.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.azazai.EventsApp;
import com.azazai.R;
import com.azazai.network.RequestManager;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.fragments.LazyLoadingListFragment;
import com.utilsframework.android.navdrawer.FragmentsNavigationInterface;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class AbstractLazyLoadingListFragment<T> extends LazyLoadingListFragment<T> {
    public static final int CREATE_EVENT = 1;
    public static final String UPDATE_ACTION = "com.azazai.ui.fragments.AbstractLazyLoadingListFragment.update";
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
                    Fragments.executeWhenViewCreated(AbstractLazyLoadingListFragment.this,
                            new GuiUtilities.OnViewCreated() {
                        @Override
                        public void onViewCreated(View view) {
                            update();
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
    protected int getLoadingViewId() {
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

    public FragmentsNavigationInterface getNavigationActivityInterface() {
        return (FragmentsNavigationInterface) getActivity();
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

    @Override
    protected boolean useSwipeRefresh() {
        return true;
    }

    @Override
    public RequestManager getRequestManager() {
        return (RequestManager) super.getRequestManager();
    }
}
