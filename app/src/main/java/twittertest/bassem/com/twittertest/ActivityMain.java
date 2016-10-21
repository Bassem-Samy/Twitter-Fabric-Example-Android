package twittertest.bassem.com.twittertest;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import twittertest.bassem.com.twittertest.fragments.FragmentFollowerInformation;
import twittertest.bassem.com.twittertest.fragments.FragmentUserFollowers;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;


public class ActivityMain extends AppCompatActivity {

    private String currentFragmentTag = null;
    final String CURRENT_FRAGMENT_EXTRA = "current_fragment";
    FrameLayout containerFrameLayout;
    FragmentUserFollowers fragmentUserFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        containerFrameLayout = (FrameLayout) findViewById(R.id.frm_container);
        if (savedInstanceState != null)
            currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_EXTRA, null);
        initializeFragments();
    }

    private void initializeFragments() {
        if (currentFragmentTag != null && currentFragmentTag.equalsIgnoreCase(Constants.FRAGMENT_FOLLOWERINFORMATION_TAG))
            loadFollowerInformation();
        else
            loadUserFollowersFragment();

    }

    public void loadFollowerInformation() {
        if (getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_FOLLOWERINFORMATION_TAG) == null) {
            FragmentFollowerInformation fragmentFollowerInformation = new FragmentFollowerInformation();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            currentFragmentTag = Constants.FRAGMENT_FOLLOWERINFORMATION_TAG;
            transaction.replace(R.id.frm_container, fragmentFollowerInformation, Constants.FRAGMENT_FOLLOWERINFORMATION_TAG).addToBackStack(null).commit();
        }
    }

    private void loadUserFollowersFragment() {
        if (fragmentUserFollowers == null)
            fragmentUserFollowers = new FragmentUserFollowers();
        if (getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_USERFOLLOWERS_TAG) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            currentFragmentTag = Constants.FRAGMENT_USERFOLLOWERS_TAG;
            transaction.replace(R.id.frm_container, fragmentUserFollowers, Constants.FRAGMENT_USERFOLLOWERS_TAG).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(CURRENT_FRAGMENT_EXTRA, currentFragmentTag);
        super.onSaveInstanceState(outState, outPersistentState);
    }

}
