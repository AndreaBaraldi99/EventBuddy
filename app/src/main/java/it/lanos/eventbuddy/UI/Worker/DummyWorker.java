package it.lanos.eventbuddy.UI.Worker;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import it.lanos.eventbuddy.UI.EventFragment;

public class DummyWorker extends Worker {

    public DummyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("UPDATEDUMMY", "entrato nel doWork del dummy-worker");

        return Result.success();
    }
}
