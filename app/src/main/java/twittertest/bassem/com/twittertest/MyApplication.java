package twittertest.bassem.com.twittertest;

import android.app.Application;
import android.provider.SyncStateContract;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;

/**
 * Created by Bassem.Wissa on 10/18/2016.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterHelper.initializeTwitter(this);
    }
}
