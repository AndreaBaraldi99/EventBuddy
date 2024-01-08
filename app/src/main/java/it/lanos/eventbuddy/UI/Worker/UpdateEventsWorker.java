package it.lanos.eventbuddy.UI.Worker;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import it.lanos.eventbuddy.util.SharedPreferencesUtil;

public class UpdateEventsWorker extends Worker {
    private Context context;



    public UpdateEventsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String lastUpdate = "0";
        

        Data outputData = new Data.Builder().putString("update", lastUpdate).build();

        return Result.success(outputData);
    }
}
