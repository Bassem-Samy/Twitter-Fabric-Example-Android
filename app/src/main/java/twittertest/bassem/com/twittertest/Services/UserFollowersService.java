package twittertest.bassem.com.twittertest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.google.gson.JsonElement;
import com.j256.ormlite.dao.Dao;

import twittertest.bassem.com.twittertest.Models.Follower;
import twittertest.bassem.com.twittertest.Models.GetUserFollowersResponse;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.DatabaseHelper;
import twittertest.bassem.com.twittertest.helpers.GsonHelper;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;

/**
 * Created by Bassem Samy on 10/21/2016.
 */

public class UserFollowersService extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final String TAG = "UserFollowersService";
    private long userId;
    private String cursor;
    private int pageSize;
    private DatabaseHelper dbHelper;

    public UserFollowersService() {
        super(UserFollowersService.class.getName());
        dbHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER_EXTRA);
            userId = intent.getLongExtra(Constants.USER_ID_EXTRA, 0);
            cursor = intent.getStringExtra(Constants.CURSOR_EXTRA);
            pageSize = intent.getIntExtra(Constants.PAGESIZE_EXTRA, 0);

            Bundle bundle = new Bundle();
            try {
                receiver.send(STATUS_RUNNING, Bundle.EMPTY);
                retrofit2.Response<JsonElement> res = TwitterHelper.GetFollowers(userId, pageSize, cursor);
                GetUserFollowersResponse response = GsonHelper.parseUserFollowersResponse(res.body(), this);
                // GetUserFollowersResponse res;
                // put result in bundle
                updateDatabase(response);
                bundle.putParcelable(Constants.RESULT_EXTRA, response);
                receiver.send(STATUS_FINISHED, bundle);

            } catch (Exception ex) {
                bundle.putString(Intent.EXTRA_TEXT, ex.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
            this.stopSelf();
        }
    }

    private void updateDatabase(GetUserFollowersResponse response) {
        try {
            Dao<Follower, Integer> followersDao = dbHelper.getFollowerDao();
            if (response != null && response.getFollowers() != null)
                for (Follower f : response.getFollowers())
                    followersDao.createOrUpdate(f);


        } catch (Exception ex) {
            Log.e("save followers", ex.toString());
        }
    }
}
