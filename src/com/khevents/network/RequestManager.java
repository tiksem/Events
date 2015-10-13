package com.khevents.network;

import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.jsonutils.Json;
import com.khevents.data.Comment;
import com.khevents.data.Event;
import com.khevents.data.Tag;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.collections.cache.EmptyCache;
import com.utils.framework.io.Network;
import com.utils.framework.network.RequestExecutorWithCaching;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.ExecuteTimeLogger;
import com.utilsframework.android.IOErrorListener;
import com.utils.framework.network.GetRequestExecutor;
import com.utilsframework.android.cache.StringSQLiteCache;
import com.utilsframework.android.network.AsyncRequestExecutorManager;
import com.utilsframework.android.network.IOErrorListenersSet;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.threading.ThrowingRunnable;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;
import com.vkandroid.VkUsersNavigationList;

import java.io.IOException;
import java.util.*;

/**
 * Created by CM on 6/21/2015.
 */
public class RequestManager extends AsyncRequestExecutorManager {
    public static final String TAG = "RequestManager";

    private RequestExecutor networkRequestExecutor = new GetRequestExecutor() {
        @Override
        public String executeRequest(String url, Map<String, Object> args) throws IOException {
            Log.i(TAG, "url = " + Network.getUrl(url, args));
            ExecuteTimeLogger.timeStart("request");
            String result = super.executeRequest(url, args);
            ExecuteTimeLogger.timeEnd("request", TAG);
            return result;
        }
    };
    private RequestExecutorWithCaching requestExecutor;

    private String rootUrl;

    public RequestManager(Context context, String rootUrl, int maxCacheRecords) {
        this.rootUrl = rootUrl;
        //StringSQLiteCache cache = new StringSQLiteCache(context, TAG, maxCacheRecords);
        EmptyCache<String, String> cache = new EmptyCache<>();
        requestExecutor = new RequestExecutorWithCaching(networkRequestExecutor, cache);
    }

    public NavigationList<Event> getEvents(String query) {
        if (query == null) {
            return new AllEventsNavigationList(rootUrl, requestExecutor, this);
        } else {
            return new AllEventsNavigationList(rootUrl, query, requestExecutor, this);
        }
    }

    public NavigationList<Tag> getTags() {
        return new TagsNavigationList(requestExecutor, this, rootUrl);
    }

    public NavigationList<Event> getEventsByTag(String tag) {
        return new EventsByTagNavigationList(rootUrl, tag, requestExecutor, this);
    }

    public void createEvent(final EventArgs args, final OnEventCreationFinished onFinish) {
        execute(new Threading.Task<IOException, Integer>() {
            @Override
            public Integer runOnBackground() throws IOException {
                String json = requestExecutor.executeRequest(rootUrl + "createEvent", args.toMap(), false);
                return (int) Json.getIdOrThrow(json);
            }

            @Override
            public void onComplete(Integer id, IOException error) {
                onFinish.onComplete(id == null ? -1 : id, error);
            }
        });
    }


    public NavigationList<Event> getEvents(long date, String query) {
        if (query == null) {
            return new AllEventsNavigationList(rootUrl, (int) (date / 1000), requestExecutor, this);
        } else {
            return new AllEventsNavigationList(rootUrl, query, (int) (date / 1000), requestExecutor, this);
        }
    }

    public NavigationList<Event> getCreatedUserEvents(String token) {
        return new UserEventsNavigationList(rootUrl, UserEventsNavigationList.Mode.created, token,
                requestExecutor, this);
    }

    public NavigationList<Event> getSubscribedUserEvents(String token) {
        return new UserEventsNavigationList(rootUrl, UserEventsNavigationList.Mode.subscribed, token,
                requestExecutor, this);
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

    public void subscribe(final long eventId, final String token, OnFinish<IOException> onFinish) {
        executeRequestCheckForErrors("subscribe", new HashMap<String, Object>() {
            {
                put("token", token);
                put("id", eventId);
            }
        }, onFinish);
    }

    public void cancelEvent(long id, String accessToken) throws IOException {
        String url = rootUrl + "cancelEvent?id=" + id + "&token=" + accessToken;
        String response = requestExecutor.executeRequest(url, null, false);
        Json.checkError(response);
    }

    public void cancelEventAsync(final long id, final String accessToken, OnFinish<IOException> onFinish) {
        execute(new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {
                cancelEvent(id, accessToken);
            }
        }, onFinish);
    }

    public NavigationList<VkUser> getSubscribers(long eventId) {
        return new VkUsersNavigationList(rootUrl + "getSubscribers",
                Collections.<String, Object>singletonMap("id", eventId),
                "Subscribers", requestExecutor, this);
    }

    public List<Comment> getTopComments(long eventId, int count) throws IOException {
        String url = rootUrl + "getCommentsList?id=" + eventId + "&offset=0&limit=" + count;
        String json = requestExecutor.executeRequest(url, null);
        List<Comment> comments = Json.readList(json, "Comments", Comment.class);

        Requests.updateCommentsUserData(requestExecutor, comments);

        return comments;
    }

    public NavigationList<Comment> getComments(List<Comment> topComments, long eventId) {
        return new CommentsNavigationList(rootUrl, eventId, topComments, requestExecutor, this);
    }

    private void executeRequestCheckForErrors(final String url, final Map<String, Object> args,
                                              OnFinish<IOException> onFinish) {
        execute(new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {
                String json = requestExecutor.executeRequest(rootUrl + url, args, false);
                Json.checkError(json);
            }
        }, onFinish);
    }

    public void addComment(final String text, final long eventId, final String accessToken,
                           OnFinish<IOException> onFinish) {
        String url = "addComment";
        Map<String, Object> args = new HashMap<String, Object>() {
            {
                put("text", text);
                put("token", accessToken);
                put("id", eventId);
            }
        };

        executeRequestCheckForErrors(url, args, onFinish);
    }

    public SuggestionsProvider<String> getTagsSuggestionsProvider(IgnoreTagsProvider ignoreTagsProvider) {
        return new TagsSuggestionsProvider(rootUrl, ignoreTagsProvider, requestExecutor);
    }

    public void loginGCMToken(String deviceToken, String oldDeviceToken, String vkToken,
                              OnFinish<IOException> onFinish) {
        Map<String, Object> args = new HashMap<>();
        if (oldDeviceToken != null) {
            args.put("deviceId", oldDeviceToken);
            args.put("newDeviceId", deviceToken);
        } else {
            args.put("deviceId", deviceToken);
        }
        args.put("token", vkToken);

        String url = "registerDevice";
        executeRequestCheckForErrors(url, args, onFinish);
    }

    public Event getEventById(long eventId) throws IOException {
        String url = rootUrl + "getEventById?id=" + eventId;
        String json = requestExecutor.executeRequest(url, null);
        return Json.read(json, Event.class);
    }
}
