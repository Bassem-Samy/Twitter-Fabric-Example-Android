package twittertest.bassem.com.twittertest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.google.gson.JsonElement;
import com.j256.ormlite.dao.Dao;

import java.util.concurrent.Callable;

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
                receiver.send(Constants.STATUS_RUNNING, Bundle.EMPTY);
                retrofit2.Response<JsonElement> res = TwitterHelper.GetFollowers(userId, pageSize, cursor);
                if (res.isSuccessful()) {
                    GetUserFollowersResponse response = GsonHelper.parseUserFollowersResponse(res.body(), this);
                    updateDatabase(response, bundle, receiver);

                } else {
                    //Try parse error and put in bundle
                    
                    receiver.send(Constants.STATUS_ERROR, bundle);

                }
            } catch (Exception ex) {
                bundle.putString(Intent.EXTRA_TEXT, ex.toString());
                receiver.send(Constants.STATUS_ERROR, bundle);
            }
            this.stopSelf();
        }
    }

    private void updateDatabase(final GetUserFollowersResponse response, final Bundle bundle, final ResultReceiver receiver) {
        try {
            final Dao<Follower, Integer> followersDao = dbHelper.getFollowerDao();
            followersDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    if (response != null && response.getFollowers() != null) {
                        for (Follower f : response.getFollowers())
                            followersDao.createOrUpdate(f);
                    }
                    bundle.putParcelable(Constants.RESULT_EXTRA, response);
                    receiver.send(Constants.STATUS_FINISHED, bundle);
                    return null;
                }
            });


        } catch (Exception ex) {
            Log.e("save followers", ex.toString());
        }
    }


}
