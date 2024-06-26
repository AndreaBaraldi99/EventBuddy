package it.lanos.eventbuddy.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Utility class to read and write data using SharedPreferences API.
 * Doc can be read here: <a href="https://developer.android.com/training/data-storage/shared-preferences">...</a>
 */
public class SharedPreferencesUtil {

    private final Context context;

    public SharedPreferencesUtil(Application application) {
        this.context = application.getApplicationContext();
    }

    public SharedPreferencesUtil(Context context) {
        this.context = context;
    }

    /**
     * Writes a String value using SharedPreferences API.
     * @param sharedPreferencesFileName The name of file where to write data.
     * @param key The key associated with the value to write.
     * @param value The value to write associated with the key.
     */
    public void writeStringData(String sharedPreferencesFileName, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Returns the String value associated with the key passed as argument
     * using SharedPreferences API.
     * @param sharedPreferencesFileName The name of file where to read the data.
     * @param key The key associated with the value to read.
     * @return The String value associated with the key passed as argument.
     */
    public String readStringData(String sharedPreferencesFileName, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

}
