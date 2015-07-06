package com.khevents.network;

import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.jsonutils.Json;
import com.khevents.data.Event;
import com.khevents.data.Tag;
import com.utils.framework.Reflection;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.io.Network;
import com.utilsframework.android.ExecuteTimeLogger;
import com.utilsframework.android.IOErrorListener;
import com.utilsframework.android.network.GetRequestExecutor;
import com.utilsframework.android.network.IOErrorListenersSet;
import com.utilsframework.android.network.RequestExecutor;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.threading.ThrowingRunnable;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;
import com.vkandroid.VkUsersNavigationList;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
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
            ExecuteTimeLogger.timeStart("request");
            String result = super.executeRequest(url, args);
            ExecuteTimeLogger.timeEnd("request", TAG);
            return result;
        }
    };
    private String rootUrl;

    public RequestManager(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public NavigationList<Event> getEvents(String query) {
        if (query == null) {
            return new AllEventsNavigationList(rootUrl, requestExecutor);
        } else {
            return new AllEventsNavigationList(rootUrl, query, requestExecutor);
        }
    }

    public NavigationList<Tag> getTags() {
        return new TagsNavigationList(requestExecutor, rootUrl);
    }

    public NavigationList<Event> getEventsByTag(String tag) {
        return new EventsByTagNavigationList(rootUrl, tag, requestExecutor);
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

    public NavigationList<Event> getEvents(long date, String query) {
        if (query == null) {
            return new AllEventsNavigationList(rootUrl, (int) (date / 1000), requestExecutor);
        } else {
            return new AllEventsNavigationList(rootUrl, query, (int) (date / 1000), requestExecutor);
        }
    }

    public NavigationList<Event> getCreatedUserEvents(String token) {
        return new UserEventsNavigationList(rootUrl, UserEventsNavigationList.Mode.created, token, requestExecutor);
    }

    public NavigationList<Event> getSubscribedUserEvents(String token) {
        return new UserEventsNavigationList(rootUrl, UserEventsNavigationList.Mode.subscribed, token, requestExecutor);
    }

    public VkUser getVkUserById(long userId) throws IOException {
        return VkApiUtils.getUser(userId, requestExecutor);
    }

    public boolean isSubscribed(long eventId, String token) throws IOException {
        String url = rootUrl + "isSubscribed?id=" + eventId + "&token=" + token;
        String response = requestExecutor.executeRequest(url, null);
        JsonNode node = Json.toJsonNode(response);
        Json.checkError(node);
        return node.get("isSubscribed").asBoolean();
    }

    public void subscribe(long eventId, String token, OnFinish<IOException> onFinish) {
        Threading.runOnBackground(new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {
                String response = requestExecutor.executeRequest(rootUrl + "subscribe", new HashMap<String, Object>() {
                    {
                        put("token", token);
                        put("id", eventId);
                    }
                });
                Json.checkError(response);
            }
        }, onFinish, IOException.class);
    }

    public void cancelEvent(long id, String accessToken) throws IOException {
        String url = rootUrl + "cancelEvent?id=" + id + "&token=" + accessToken;
        String response = requestExecutor.executeRequest(url, null);
        Json.checkError(response);
    }

    public void cancelEventAsync(long id, String accessToken, OnFinish<IOException> onFinish) {
        Threading.runOnBackground(new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {
                cancelEvent(id, accessToken);
            }
        }, onFinish, IOException.class);
    }

    public NavigationList<VkUser> getSubscribers(long eventId) {
        return new VkUsersNavigationList(rootUrl + "getSubscribers", Collections.singletonMap("id", eventId),
                "Subscribers", requestExecutor);
    }
}
