package com.khevents.network;

import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.jsonutils.Json;
import com.khevents.data.Event;
import com.utils.framework.Reflection;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.io.Network;
import com.utilsframework.android.IOErrorListener;
import com.utilsframework.android.network.GetRequestExecutor;
import com.utilsframework.android.network.IOErrorListenersSet;
import com.utilsframework.android.network.RequestExecutor;
import com.utilsframework.android.threading.Threading;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by CM on 6/21/2015.
 */
public class RequestManager implements IOErrorListenersSet {
    public static final String TAG = "RequestManager";

    private RequestExecutor requestExecutor = new GetRequestExecutor() {
        @Override
        public String executeRequest(String url, Map<String, Object> args) throws IOException {
            Log.i(TAG, "url = " + Network.getUrl(url, args));
            return super.executeRequest(url, args);
        }
    };
    private String rootUrl;

    public RequestManager(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public NavigationList<Event> getEvents() {
        return new EventsNavigationList(rootUrl, requestExecutor);
    }

    public void createEvent(EventArgs args, OnEventCreationFinished onFinish) {
        Threading.executeAsyncTask(new Threading.Task<IOException, Integer>() {
            @Override
            public Integer runOnBackground() throws IOException {
                String json = requestExecutor.executeRequest(rootUrl + "createEvent", args.toMap());
                return (int) Json.getIdOrThrow(json);
            }

            @Override
            public void onSuccess(Integer id) {
                onFinish.onComplete(id, null);
            }

            @Override
            public void onError(IOException error) {
                onFinish.onComplete(-1, error);
            }
        }, IOException.class);
    }

    @Override
    public void addIOErrorListener(IOErrorListener listener) {

    }

    @Override
    public void removeIOErrorListener(IOErrorListener listener) {

    }
}
