package it.lanos.eventbuddy.UI.Worker;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import it.lanos.eventbuddy.UI.EventFragment;
import it.lanos.eventbuddy.util.SharedPreferencesUtil;

public class UpdateEventsWorker extends Worker {
    private Context context;



    public UpdateEventsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
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

            return Result.success();
        } catch (Exception e) {
            return  Result.retry();
        }
    }
}
