package it.lanos.eventbuddy.util;

import static it.lanos.eventbuddy.util.Constants.DATASTORE_NAME;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.core.Single;

public class DatastoreBuilder {
    private static volatile RxDataStore<Preferences> INSTANCE;

    public static RxDataStore<Preferences> getDataStore(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatastoreBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxPreferenceDataStoreBuilder(context, DATASTORE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    public void putStringValue(String Key, String value){
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<Preferences> updateResult =  INSTANCE.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        });
    }

    String getStringValue(String Key) {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<String> value = INSTANCE.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem("null");
        return value.blockingGet();
    }

}
