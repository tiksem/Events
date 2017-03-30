package com.azazai.ui;

import android.content.Context;

import com.azazai.R;
import com.jsonutils.RequestException;
import com.utilsframework.android.view.Toasts;

import java.io.IOException;

public class ErrorMessages {
    public static String getMessage(Context context, IOException e) {
        if (e instanceof RequestException) {
            return e.getMessage();
        }

        return context.getString(R.string.no_internet_connection);
    }

    public static void showNetworkErrorToast(Context context, IOException e) {
        String message = getMessage(context, e);
        Toasts.toast(context, message);
    }
}
