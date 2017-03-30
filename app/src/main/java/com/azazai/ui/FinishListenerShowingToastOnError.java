package com.azazai.ui;

import android.content.Context;

import com.utilsframework.android.threading.OnFinish;

import java.io.IOException;

public abstract class FinishListenerShowingToastOnError implements OnFinish<IOException> {
    private Context context;

    public FinishListenerShowingToastOnError(Context context) {
        this.context = context;
    }

    @Override
    public final void onFinish(IOException e) {
        if (e == null) {
            onSuccess();
        } else {
            ErrorMessages.showNetworkErrorToast(context, e);
            onError();
        }
        onFinish();
    }

    public void onError() {}
    public void onSuccess() {}
    public void onFinish() {}
}
