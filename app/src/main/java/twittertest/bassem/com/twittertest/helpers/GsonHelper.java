package twittertest.bassem.com.twittertest.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import twittertest.bassem.com.twittertest.Models.GetUserFollowersResponse;

/**
 * Created by Mina Samy on 10/21/2016.
 */

public class GsonHelper {
    public static GetUserFollowersResponse parseUserFollowersResponse(JsonElement json, Context mcontext) {
        GetUserFollowersResponse response;
        Gson gson = new Gson();
        try {
            response = gson.fromJson(json, GetUserFollowersResponse.class);
        } catch (Exception ex) {
            return null;
        }
        return response;
    }
}
