package twittertest.bassem.com.twittertest.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.twitter.sdk.android.core.models.Tweet;

import twittertest.bassem.com.twittertest.Models.GetUserFollowersResponse;
import twittertest.bassem.com.twittertest.Services.UserTimelineService;

/**
 * Created by Bassem Samy on 10/21/2016.
 */

public class GsonHelper {
    public static GetUserFollowersResponse parseUserFollowersResponse(JsonElement json) {
        GetUserFollowersResponse response;
        Gson gson = new Gson();
        try {
            response = gson.fromJson(json, GetUserFollowersResponse.class);
        } catch (Exception ex) {
            return null;
        }
        return response;
    }

    public static Tweet[] parseUserTimeLineResponse(String json) {
        try {
            Gson gson
                    = new Gson();
            Tweet[] tweets = gson.fromJson(json, Tweet[].class);
            return tweets;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Tweet[0];
    }
}
