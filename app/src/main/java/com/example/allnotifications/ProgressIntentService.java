package com.example.allnotifications;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ProgressIntentService extends IntentService {
    public ProgressIntentService() {
        super("ProgressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.NORMAL_CHANNEL);
        builder.setContentTitle("Загрузка")
                .setContentText("Загружаем картинку")
                .setSmallIcon(android.R.drawable.ic_dialog_map);

        for(int i = 0; i < 100; i++)
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            builder.setProgress(100, i, false);
            manager.notify(R.id.PROGRESS_PENDING_ID, builder.build());
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        builder
                .setContentText("Загрузка завершена")
                .setProgress(100,100, false);
        manager.notify(R.id.PROGRESS_PENDING_ID, builder.build());

    }
}
