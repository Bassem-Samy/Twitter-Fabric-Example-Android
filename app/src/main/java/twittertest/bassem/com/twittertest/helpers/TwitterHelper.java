package twittertest.bassem.com.twittertest.helpers;

import android.content.Context;

import com.google.gson.JsonElement;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Bassem Samy on 10/19/2016.
 */

public class TwitterHelper {
    private static boolean isTwitterInitialized = false;

    public static void initializeTwitter(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        isTwitterInitialized = true;
    }

    public static boolean checkIfUserLoggedIn() {
        if (isTwitterInitialized) {
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            if (session != null)
                return true;
            return false;
        }
        return false;

    }


    public static Response<JsonElement> GetFollowers(long userId, int pageSize, String cursor) {
        TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
        MyTwitterApiClient apiClient = new MyTwitterApiClient(currentSession);
        TwitterUserFollowersService followersService = apiClient.getUserFollowersService();
        Call<JsonElement> call = followersService.list(userId, pageSize, cursor, false, false);
        try {
            return call.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Response<JsonElement> GetUserTimeline(long userId, int pageSize) {
        TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
        MyTwitterApiClient apiClient = new MyTwitterApiClient(currentSession);
        TwitterUserTimeLine service = apiClient.getUserTimelineService();
        Call<JsonElement> call = service.list(userId, pageSize);
        try {
            return call.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static long GetCurrentUserId() {
        if (isTwitterInitialized) {
            TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
            if (currentSession != null)
                return currentSession.getUserId();
        }
        return -1;
    }

    public static void SignoutUser() {

        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
    }
}
