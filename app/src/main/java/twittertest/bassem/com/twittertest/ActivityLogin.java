package twittertest.bassem.com.twittertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import twittertest.bassem.com.twittertest.helpers.TwitterHelper;

public class ActivityLogin extends AppCompatActivity {
    TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        twitterLoginButton.setCallback(twitterLoginCallback);
twitterLoginButton.setText(R.string.login_with_twitter);
        checkUserSession();
    }

    /**
     * checks if the user has a session (already logged in before) continue to home page, else do nothing
     */
    private void checkUserSession() {
        if (TwitterHelper.checkIfUserLoggedIn())
            navigateToMainPage();
    }

    private void navigateToMainPage() {
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
        finish();
    }

    Callback<TwitterSession> twitterLoginCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            navigateToMainPage();
        }

        @Override
        public void failure(TwitterException exception) {
            Toast.makeText(ActivityLogin.this, getString(R.string.login_failed_msg), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
