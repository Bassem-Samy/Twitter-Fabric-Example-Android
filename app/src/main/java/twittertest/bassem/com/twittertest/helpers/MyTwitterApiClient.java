package twittertest.bassem.com.twittertest.helpers;

import com.google.gson.JsonElement;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Bassem Samy on 10/20/2016.
 */


public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }


    public TwitterUserFollowersService getUserFollowersService() {
        return getService(TwitterUserFollowersService.class);
    }

    public TwitterUserTimeLine getUserTimelineService() {
        return getService(TwitterUserTimeLine.class);
    }
}


interface TwitterUserFollowersService {
    @GET("/1.1/followers/list.json")
    Call<JsonElement> list(@Query("user_id") long id, @Query("count") int pageSize, @Query("cursor") String cursor, @Query("include_user_entities") boolean includeUserEntities, @Query("skip_status") boolean skipStatuses);

}

interface TwitterUserTimeLine {
    @GET("/1.1/statuses/user_timeline.json")
    Call<JsonElement> list(@Query("user_id") long id, @Query("count") int pageSize);
}

