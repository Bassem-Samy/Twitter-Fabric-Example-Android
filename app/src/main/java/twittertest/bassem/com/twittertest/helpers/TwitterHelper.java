package twittertest.bassem.com.twittertest.helpers;

import android.content.Context;

import com.google.gson.JsonElement;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.io.IOException;

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
//        if (session != null) {
//            TwitterAuthToken authToken = session.getAuthToken();
//            String token = authToken.token;
//            String secret = authToken.secret;
//            Intent intent = new Intent(this, ActivityMain.class);
//            startActivity(intent);
//            finish();
//        }
    }

    public static void GetUser() {
        TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
        MyTwitterApiClient apiclients = new MyTwitterApiClient(currentSession);
        TwitterUserProfileService statusesService = apiclients.getUserProfileService();
        Call<User> call = statusesService.show(currentSession.getUserId());
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                //Do something with result
            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }
        });

    }

    public static Response<JsonElement> GetFollowers(long userId, int pageSize, String cursor) {
        TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
        MyTwitterApiClient apiClient = new MyTwitterApiClient(currentSession);
        TwitterUserFollowersService followersService = apiClient.getUserFollowersService();
        Call<JsonElement> call = followersService.list(userId, pageSize, cursor, false);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static long GetCurrentUserId() {
        if (isTwitterInitialized) {
            TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
            if (currentSession != null)
                return currentSession.getUserId();
        }
        return -1;
    }
}
