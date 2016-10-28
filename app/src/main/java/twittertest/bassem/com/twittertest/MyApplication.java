package twittertest.bassem.com.twittertest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.net.ConnectException;

import io.fabric.sdk.android.Fabric;
import twittertest.bassem.com.twittertest.helpers.Constants;
import twittertest.bassem.com.twittertest.helpers.LocaleHelper;
import twittertest.bassem.com.twittertest.helpers.TwitterHelper;

/**
 * Created by Bassem.Wissa on 10/18/2016.
 */

public class MyApplication extends Application {

    public String selectedLang;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterHelper.initializeTwitter(this);
        LocaleHelper localeHelper = new LocaleHelper();
        selectedLang = localeHelper.GetLanguage(this);
        Log.e("selectedlang",selectedLang);
        updateLang(selectedLang);
    }

    private void updateLang(String lang) {
        LocaleHelper localeHelper = new LocaleHelper();
        selectedLang = lang;
        if (selectedLang == null || selectedLang.equals("")) {
            localeHelper.SetLocale(this, Constants.enLang);
        } else {
            localeHelper.SetLocale(this, selectedLang);
            localeHelper.UpdateResources(this, selectedLang);
        }
    }

    public static void ChangeLanguage(Context context) {
        String language = "en";

        LocaleHelper helper = new LocaleHelper();
        if (helper.GetLanguage(context).equalsIgnoreCase(language)) {
            language = "ar";
        }
        helper.SetLocale(context, language);
        Intent loginIntent = new Intent(context, ActivityLogin.class);
        context.startActivity(loginIntent);
        ((AppCompatActivity)context).finish();

    }

}
