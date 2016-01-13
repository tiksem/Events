package com.azazai.network;

import java.io.IOException;

/**
 * Created by CM on 6/22/2015.
 */
public interface OnEventCreationFinished {
    void onComplete(int id, IOException error);
}
