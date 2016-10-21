package twittertest.bassem.com.twittertest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;
import twittertest.bassem.com.twittertest.receivers.GetUserFollowersReceiver;

/**
 * Created by Bassem Samy on 10/21/2016.
 */

public class UserFollowersService extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final String TAG = "UserFollowersService";
    private long userId;
    private int cursor;
    private int pageSize;

    public UserFollowersService() {
        super(UserFollowersService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER_EXTRA);
            userId = intent.getLongExtra(Constants.USER_ID_EXTRA, 0);
            cursor = intent.getIntExtra(Constants.CURSOR_EXTRA, -1);
            pageSize = intent.getIntExtra(Constants.PAGESIZE_EXTRA, 0);
            Bundle bundle = new Bundle();

            try {
                receiver.send(STATUS_RUNNING, Bundle.EMPTY);
                Object res= TwitterHelper.GetFollowers(userId,pageSize,cursor);
                // put result in bundle
                receiver.send(STATUS_FINISHED, bundle);

            } catch (Exception ex) {
                bundle.putString(Intent.EXTRA_TEXT, ex.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
            this.stopSelf();
        }
    }
}
