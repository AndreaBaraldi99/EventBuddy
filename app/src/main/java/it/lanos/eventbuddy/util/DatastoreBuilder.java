package it.lanos.eventbuddy.util;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

public class DatastoreBuilder {
    private static volatile RxDataStore<Preferences> INSTANCE;

    public static RxDataStore<Preferences> getDataStore(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatastoreBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxPreferenceDataStoreBuilder(context, /*name=*/ "settings").build();
                }
            }
        }

        return INSTANCE;
    }
}
