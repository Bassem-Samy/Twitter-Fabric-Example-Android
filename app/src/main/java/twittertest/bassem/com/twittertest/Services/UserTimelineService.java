package twittertest.bassem.com.twittertest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonElement;
import com.twitter.sdk.android.core.models.Tweet;

import twittertest.bassem.com.twittertest.Models.GetUserFollowersResponse;
import twittertest.bassem.com.twittertest.fragments.FragmentFollowerInformation;
import twittertest.bassem.com.twittertest.fragments.FragmentUserFollowers;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.GsonHelper;
import twittertest.bassem.com.twittertest.helpers.MyUtilities;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;

/**
 * Created by Bassem Samy on 10/24/2016.
 */

public class UserTimelineService extends IntentService {
    private long userId;
    private int pageSize;

    public UserTimelineService() {
        super(UserTimelineService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            userId = intent.getLongExtra(Constants.USER_ID_EXTRA, 0);
            pageSize = intent.getIntExtra(Constants.PAGESIZE_EXTRA, 0);
            try {
                if (MyUtilities.checkForInternet(this)) {
                    retrofit2.Response<JsonElement> res = TwitterHelper.GetUserTimeline(userId, pageSize);
                    if (res.isSuccessful()) {
                        sendBackBroadCast(res.body().toString());

                    } else {

                        sendBackBroadCast(null);

                    }
                } else {
                }
            } catch (Exception ex) {
                sendBackBroadCast(null);


            }
        }
    }

    private void sendBackBroadCast(String body) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(FragmentFollowerInformation.FollowerInfoBroadcastReceiver.PROCESS_USER_INFO_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_EXTRA, body);
        broadcastIntent.putExtras(bundle);
        sendBroadcast(broadcastIntent);
    }
}
