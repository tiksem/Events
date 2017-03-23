package com.azazai.ui.fragments;

import android.app.ProgressDialog;

import com.azazai.R;
import com.azazai.adapters.RequestsAdapter;
import com.azazai.data.Request;
import com.azazai.network.RequestManager;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.Toasts;
import com.utilsframework.android.view.YesNoAlertSettings;
import com.vk.sdk.VKSdk;

import java.io.IOException;

public abstract class AbstractRequestsFragment
        extends LazyLoadingListFragmentWithEmptyResults<Request> implements RequestsAdapter.Listener {
    @Override
    protected int getEmptyResultsHintText() {
        return R.string.no_requests;
    }

    @Override
    protected ViewArrayAdapter<Request, ?> createAdapter() {
        return new RequestsAdapter(getContext(), this);
    }

    @Override
    protected void onListItemClicked(Request item, int position) {

    }

    @Override
    public void onAcceptRequestTap(final Request request) {
        executeAcceptOrDeclineRequest(true, request);
    }

    @Override
    public void onDeclineRequestTap(final Request request) {
        Alerts.showYesNoAlert(new YesNoAlertSettings(getContext()) {
            {
                setMessage(R.string.request_decline_confirm);
            }

            @Override
            public void onYes() {
                executeAcceptOrDeclineRequest(false, request);
            }
        });
    }

    private void executeAcceptOrDeclineRequest(final boolean accept, final Request request) {
        final ProgressDialog progressDialog = Alerts.showCircleProgressDialog(getContext(),
                R.string.please_wait);
        getRequestManager().acceptOrDenyRequest(accept, request.event.id, request.userId,
                VKSdk.getAccessToken().accessToken, new OnFinish<IOException>() {
                    @Override
                    public void onFinish(IOException e) {
                        if (e != null) {
                            e.printStackTrace();
                            int message = accept ? R.string.accept_request_failed :
                                    R.string.declining_request_failed;
                            Toasts.toast(getContext(), message);
                        } else {
                            getAdapter().removeItem(request);
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
