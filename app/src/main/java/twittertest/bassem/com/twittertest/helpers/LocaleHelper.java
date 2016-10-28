package twittertest.bassem.com.twittertest.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by Bassem Samy on 10/28/2016.
 */

public class LocaleHelper {

    private final String SELECTED_LANGUAGE_KEY = "languageKey";

    public String GetLanguage(Context context) {
        return GetPersistedData(context, Locale.getDefault().getLanguage());
    }

    public void SetLocale(Context context, String language) {
        Persist(context, language);
        UpdateResources(context, language);
    }

    private String GetPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE_KEY, defaultLanguage);
    }

    private void Persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE_KEY, language);
        editor.apply();
    }

    public void UpdateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
