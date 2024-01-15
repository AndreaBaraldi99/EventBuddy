package it.lanos.eventbuddy.data.source.Worker;

import static androidx.core.content.ContextCompat.getSystemService;
import static it.lanos.eventbuddy.util.Constants.CHANNEL_ID;
import static it.lanos.eventbuddy.util.Constants.EVENT_NUM_KEY;
import static it.lanos.eventbuddy.util.Constants.FRESH_TIMEOUT;
import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.BottomNavigationBarActivity;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.util.ServiceLocator;
import it.lanos.eventbuddy.util.SharedPreferencesUtil;

public class UpdateEventsWorker extends Worker {
    private final SharedPreferencesUtil sharedPreferencesUtil;
    private final IEventsRepository eventsRepository;
    private int eventNum;


    public UpdateEventsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sharedPreferencesUtil = new SharedPreferencesUtil(context);
        eventsRepository = ServiceLocator.getInstance().getEventsRepository((Application) context);
        String input = workerParams.getInputData().getString(EVENT_NUM_KEY);
        if (input != null) {
            eventNum = Integer.parseInt(input);
        }
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.app_name);
            String description = getApplicationContext().getString(R.string.notification_content);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(getApplicationContext(), NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        while (!this.isStopped()) {
            String lastUpdate = "0";
            if (sharedPreferencesUtil.readStringData(
                    SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE) != null) {
                lastUpdate = sharedPreferencesUtil.readStringData(
                        SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE);
            }
            it.lanos.eventbuddy.data.source.models.Result result = eventsRepository.fetchEvents(Long.parseLong(lastUpdate)).getValue();
            if (result != null && result.isSuccess()) {
                int newEventNum = ((it.lanos.eventbuddy.data.source.models.Result.Success) result).getData().size();
                Intent intent = new Intent(getApplicationContext(), BottomNavigationBarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
                if (newEventNum > eventNum) {
                    String title = getApplicationContext().getString(R.string.app_name);
                    String content = getApplicationContext().getString(R.string.notification_content);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(title)
                            .setContentText(content)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    eventNum = newEventNum;

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        Thread.currentThread().interrupt();

                    }
                    notificationManager.notify(0, builder.build());
                }
            }
            try {
                Thread.sleep(FRESH_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Result.success();
    }
}
