package twittertest.bassem.com.twittertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.sql.SQLException;

import twittertest.bassem.com.twittertest.Models.Follower;
import twittertest.bassem.com.twittertest.fragments.FragmentFollowerInformation;
import twittertest.bassem.com.twittertest.fragments.FragmentUserFollowers;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.DatabaseHelper;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;


public class ActivityMain extends AppCompatActivity {

    private String currentFragmentTag = null;
    final String CURRENT_FRAGMENT_EXTRA = "current_fragment";
    FrameLayout containerFrameLayout;
    FragmentUserFollowers fragmentUserFollowers;
    ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        containerFrameLayout = (FrameLayout) findViewById(R.id.frm_container);
        if (savedInstanceState != null)
            currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_EXTRA, null);
        initializeFragments();
        prepareFloatingMenu();

    }


    private void initializeFragments() {
        if (currentFragmentTag != null && currentFragmentTag.equalsIgnoreCase(Constants.FRAGMENT_FOLLOWERINFORMATION_TAG))
            loadFollowerInformation(null);
        else
            loadUserFollowersFragment();

    }

    public void loadFollowerInformation(Follower follower) {
        if (getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_FOLLOWERINFORMATION_TAG) == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.FOLLOWER_EXTRA, follower);
            FragmentFollowerInformation fragmentFollowerInformation = new FragmentFollowerInformation();
            fragmentFollowerInformation.setArguments(bundle);
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

    private void prepareFloatingMenu() {
        com.getbase.floatingactionbutton.FloatingActionButton languageButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.menu_language);
        languageButton.setOnClickListener(languageChangeOnClickListener);
        com.getbase.floatingactionbutton.FloatingActionButton signoutButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.menu_signout);

        signoutButton.setOnClickListener(signoutOnClickListener);
    }

    View.OnClickListener languageChangeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyApplication.ChangeLanguage(ActivityMain.this);
        }
    };
    View.OnClickListener signoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TwitterHelper.SignoutUser();
            try {
                new DatabaseHelper(ActivityMain.this).clearFollowerTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Intent loginIntent = new Intent(ActivityMain.this, ActivityLogin.class);
            startActivity(loginIntent);
            finish();
        }
    };
}
