package it.lanos.eventbuddy.UI.Worker;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import it.lanos.eventbuddy.util.SharedPreferencesUtil;

public class UpdateEventsWorker extends Worker {
    private SharedPreferencesUtil sharedPreferencesUtil;


    public UpdateEventsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sharedPreferencesUtil = new SharedPreferencesUtil(context);
    }
    /*
    @NonNull
    @Override
    public Result doWork() {

        String lastUpdate = "0";
        

        Data outputData = new Data.Builder().putString("update", lastUpdate).build();

        return Result.success(outputData);
    }

     */

    @NonNull
    @Override
    public Result doWork() {
        try {
            String lastUpdate = "0";

            if (sharedPreferencesUtil.readStringData(
                    SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE) != null) {
                lastUpdate = sharedPreferencesUtil.readStringData(
                        SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE);
            }
            Log.d("UPDATE", lastUpdate);
            Data outputData = new Data.Builder().putString("lastUpdate", lastUpdate).build();

            return Result.success(outputData);
        } catch (Exception e) {
            return  Result.retry();
        }
    }
}
