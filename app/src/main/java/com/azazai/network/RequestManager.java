package com.azazai.network;

import android.content.Context;
import android.util.Log;

import com.azazai.data.Icon;
import com.azazai.data.Request;
import com.fasterxml.jackson.databind.JsonNode;
import com.jsonutils.Json;
import com.azazai.data.Comment;
import com.azazai.data.Event;
import com.azazai.data.Tag;
import com.utils.framework.collections.LazyLoadingList;
import com.utils.framework.collections.cache.Cache;
import com.utils.framework.collections.cache.EmptyCache;
import com.utils.framework.collections.cache.LruCache;
import com.utils.framework.io.Network;
import com.utils.framework.network.RequestExecutorWithOfflineCaching;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.ExecuteTimeLogger;
import com.utils.framework.network.GetRequestExecutor;
import com.utilsframework.android.network.LegacyRequestManager;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.threading.ThrowingRunnable;
import com.vkandroid.VkApiUtils;
import com.vkandroid.VkUser;
import com.vkandroid.VkUsersLazyLoadingList;

import java.io.IOException;
import java.util.*;

/**
 * Created by CM on 6/21/2015.
 */
public class RequestManager extends LegacyRequestManager {
    public static final String TAG = "RequestManager";
    private static final int MAX_DATABASE_CACHE_SIZE = 100;
    public static final int MAX_ICONS_CACHE_SIZE = 500;
    public static final int MAX_CREATED_EVENTS_CACHE_SIZE = 500;
    public static final int MAX_SUBSCRIBED_EVENTS_CACHE_SIZE = 500;

    // for debug purposes, don't use it directly
    public static boolean internetConnectionEnabled = true;

    private static final RequestExecutor networkRequestExecutor = new GetRequestExecutor() {
        @Override
        public String executeRequest(String url, Map<String, Object> args) throws IOException {
            if (internetConnectionEnabled) {
                Log.i(TAG, "url = " + Network.getUrl(url, args));
                ExecuteTimeLogger.timeStart("request");
                String result = super.executeRequest(url, args);
                ExecuteTimeLogger.timeEnd("request", TAG);
                return result;
            } else {
                throw new IOException("YO!");
            }
        }
    };
    private RequestExecutorWithOfflineCaching requestExecutor;

    private String rootUrl;
    private static Cache<String, List<Icon>> iconsCache;
    private static Cache<String, List<Object>> createdEventsCache;
    private static Cache<String, List<Object>> subscribedEventsCache;

    public RequestManager(Context context, String rootUrl) {
        this.rootUrl = rootUrl;
        //StringSQLiteCache cache = new StringSQLiteCache(context, TAG, maxCacheRecords);
        EmptyCache<String, String> cache = new EmptyCache<>();
        requestExecutor = new RequestExecutorWithOfflineCaching(networkRequestExecutor, cache);
    }

    public static void init() {
        iconsCache = new LruCache<>(MAX_ICONS_CACHE_SIZE);
        createdEventsCache = new LruCache<>(MAX_CREATED_EVENTS_CACHE_SIZE);
        subscribedEventsCache = new LruCache<>(MAX_SUBSCRIBED_EVENTS_CACHE_SIZE);
    }

    public LazyLoadingList<Object> getEvents(String query) {
        if (query == null) {
            return new AllEventsLazyLoadingList(rootUrl, requestExecutor, this);
        } else {
            return new AllEventsLazyLoadingList(rootUrl, query, requestExecutor, this);
        }
    }

    public LazyLoadingList<Tag> getTags() {
        return new TagsLazyLoadingList(requestExecutor, this, rootUrl);
    }

    public LazyLoadingList<Object> getEventsByTag(String tag) {
        return new EventsByTagLazyLoadingList(rootUrl, tag, requestExecutor, this);
    }

    public IconsLazyLoadingList getIcons() {
        IconsLazyLoadingList icons = new IconsLazyLoadingList(rootUrl, requestExecutor, this);
        icons.setCache(iconsCache);
        return icons;
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
                if (error == null) {
                    createdEventsCache.clear();
                }

                onFinish.onComplete(id == null ? -1 : id, error);
            }
        });
    }

    public void editEvent(EventArgs args, OnFinish<IOException> onFinish) {
        execute(new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {

            }
        }, onFinish);
    }

    public LazyLoadingList<Object> getEvents(long date, String query) {
        if (query == null) {
            return new AllEventsLazyLoadingList(rootUrl, (int) (date / 1000), requestExecutor, this);
        } else {
            return new AllEventsLazyLoadingList(rootUrl, query, (int) (date / 1000), requestExecutor, this);
        }
    }

    public LazyLoadingList<Object> getCreatedUserEvents(long userId) {
        UserEventsLazyLoadingList list = new UserEventsLazyLoadingList(rootUrl,
                UserEventsLazyLoadingList.Mode.created,
                userId, requestExecutor, this);
        list.setCache(createdEventsCache);
        return list;
    }

    public LazyLoadingList<Object> getSubscribedUserEvents(long userId) {
        UserEventsLazyLoadingList list = new UserEventsLazyLoadingList(rootUrl,
                UserEventsLazyLoadingList.Mode.subscribed,
                userId, requestExecutor, this);
        list.setCache(subscribedEventsCache);
        return list;
    }

    public VkUser getVkUserById(long userId) throws IOException {
        return VkApiUtils.getUser(userId, requestExecutor);
    }

    public boolean isSubscribed(long eventId, long userId) throws IOException {
        String url = rootUrl + "isSubscribed?id=" + eventId + "&userId=" + userId;
        String response = requestExecutor.executeRequest(url, null);
        JsonNode node = Json.toJsonNode(response);
        Json.checkError(node);
        return node.get("isSubscribed").asText().equals("subscribed");
    }

    public void subscribe(final long eventId, final String token,
                          final OnFinish<IOException> onFinish) {
        executeRequestCheckForErrors("subscribe", new HashMap<String, Object>() {
            {
                put("token", token);
                put("id", eventId);
            }
        }, new OnFinish<IOException>() {
            @Override
            public void onFinish(IOException e) {
                if (e == null) {
                    subscribedEventsCache.clear();
                }

                onFinish.onFinish(e);
            }
        });
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

    public LazyLoadingList<VkUser> getSubscribers(long eventId) {
        return new VkUsersLazyLoadingList(rootUrl + "getSubscribers",
                Collections.<String, Object>singletonMap("id", eventId),
                "Subscribers", requestExecutor, this);
    }

    public List<Comment> getTopComments(long eventId, int count) throws IOException {
        String url = rootUrl + "getCommentsList?id=" + eventId + "&offset=0&limit=" + count;
        String json = requestExecutor.executeRequest(url, null);
        List<Comment> comments = Json.readList(json, "Comments", Comment.class);

        Requests.updateUserData(requestExecutor, comments);

        return comments;
    }

    public LazyLoadingList<Comment> getComments(List<Comment> topComments, long eventId) {
        return new CommentsLazyLoadingList(rootUrl, eventId, topComments, requestExecutor, this);
    }

    public LazyLoadingList<Comment> getComments(long eventId) {
        return new CommentsLazyLoadingList(rootUrl, eventId, requestExecutor, this);
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

    public LazyLoadingList<Request> getRequestsByUserId(long userId) {
        return new UserRequestsLazyLoadingList(rootUrl, userId, requestExecutor, this);
    }

    public LazyLoadingList<Request> getRequestsByEvent(Event event) {
        return new EventRequestsLazyLoadingList(rootUrl, event, requestExecutor, this);
    }

    public void acceptOrDenyRequest(boolean accept,
                                    final long eventId,
                                    final long userId,
                                    final String token,
                                    OnFinish<IOException> onFinish) {
        executeRequestCheckForErrors(accept ? "acceptRequest" : "denyRequest",
                new HashMap<String, Object>() {
            {
                put("token", token);
                put("id", eventId);
                put("userId", userId);
            }
        }, onFinish);
    }

    public int getRequestsCount(long eventId) throws IOException {
        String url = rootUrl + "getRequestsCount?id=" + eventId;
        String response = requestExecutor.executeRequest(url, null);
        return Json.toJsonNode(response).get("result").asInt();
    }

    public void deleteComment(final String accessToken,
                              final long commentId,
                              OnFinish<IOException> onFinish) {
        executeRequestCheckForErrors("deleteComment", new HashMap<String, Object>() {
            {
                put("commentId", commentId);
                put("token", accessToken);
            }
        }, onFinish);
    }

    public void clearCreatedEventsCache() {
        createdEventsCache.clear();
    }

    public void clearSubscribedEventsCache() {
        subscribedEventsCache.clear();
    }
}
