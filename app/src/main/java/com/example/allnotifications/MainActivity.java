package com.example.allnotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
    private NotificationManagerCompat mNotificationManagerCompat;

    public static final String NORMAL_CHANNEL = "NORMAL_CHANNEL";
    public static final String IMPORTANT_CHANNEL = "IMPORTANT_CHANNEL";

    private int numberOfMessages = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManagerCompat = NotificationManagerCompat.from(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String nameNI = getResources().getString(R.string.NOT_IMPORTANT_CHANNEL_NAME);
            NotificationChannel channelNI = new NotificationChannel(
                    NORMAL_CHANNEL,
                    nameNI,
                    NotificationManager.IMPORTANCE_LOW
            );
            channelNI.setDescription(
                    getResources().getString(R.string.NOT_IMPORTANT_CHANNEL_DESCRIPTION)
            );
            channelNI.enableVibration(false);
            mNotificationManagerCompat.createNotificationChannel(channelNI);


            String nameI = getResources().getString(R.string.IMPORTANT_CHANNEL_NAME);
            NotificationChannel channelI = new NotificationChannel(
                    IMPORTANT_CHANNEL,
                    nameI,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelI.setDescription(
                    getResources().getString(R.string.IMPORTANT_CHANNEL_DESCRIPTION)
            );
            channelI.enableVibration(true);
            channelI.setVibrationPattern(new long[]{
                    500, 200, 100, 300, 200
            });
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            channelI.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.new_message_tone), audioAttributes);

            mNotificationManagerCompat.createNotificationChannel(channelI);
        }
    }

    public void simpleNotification(View view)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);

        builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
        builder.setContentTitle("Что-то произошла");
        builder.setContentText("Произошло что-то очень не важное");

        builder.setLargeIcon(
                BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_edit)
        );

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent a2pending = PendingIntent.getActivity(
                this, R.id.BROWSER_PENDING_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        builder.setContentIntent(a2pending);
        builder.setAutoCancel(true);

        mNotificationManagerCompat.notify(
                R.id.SIMPLE_NOTIFICATION_ID,
                builder.build()
        );
    }

    public void simpleCancel(View view)
    {
        mNotificationManagerCompat.cancel(R.id.SIMPLE_NOTIFICATION_ID);
    }

    public void browserNotification(View view){
        Intent browserIntent = new Intent();
        browserIntent.setAction(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse("https://www.google.com"));

        PendingIntent browserPending = PendingIntent.getActivity(
                this, R.id.BROWSER_PENDING_ID,
                browserIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
        builder.setContentTitle("Запустить браузер");
        builder.setContentText("Посмотреть google.com");

        builder.setContentIntent(browserPending).setAutoCancel(true);
        mNotificationManagerCompat.notify(R.id.GOOGLE_NOTIFICATION_ID, builder.build());
    }

    public void complexNotification(View view){
        Intent browserIntent = new Intent();
        browserIntent.setAction(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse("https://www.louvre.fr"));
        PendingIntent browserPending = PendingIntent.getActivity(
                this, R.id.BROWSER_PENDING_ID,
                browserIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        Intent mapIntent = new Intent();
        mapIntent.setAction(Intent.ACTION_VIEW);
        mapIntent.setData(Uri.parse("geo:48.85,2.34"));
        PendingIntent mapPending = PendingIntent.getActivity(
                this, R.id.MAP_PENDING_ID,
                mapIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
        builder.setContentTitle("Экскурсия в Лувр");
        builder.setContentText("Начинается через 5 мин");

        builder.addAction(
                android.R.drawable.sym_def_app_icon,
                "In browser", browserPending
        );
        builder.addAction(
                android.R.drawable.sym_def_app_icon,
                "On map", mapPending
        );

        builder.setContentIntent(mapPending).setAutoCancel(true);
        mNotificationManagerCompat.notify(R.id.LOUVRE_NOTIFICATION_ID, builder.build());
    }

    @SuppressLint("DefaultLocale")
    public void bigPicture(View view){
        TaskStackBuilder task = TaskStackBuilder.create(this);
        task.addNextIntent(new Intent(this, MainActivity.class));
        //task.addNextIntent(new Intent(this, MainActivity2.class));

        PendingIntent stackPending = task.getPendingIntent(
                R.id.TASK_PENDING_ID,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("Новые сообщения");
        style.bigPicture(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_delete));
        style.setSummaryText("У вас "+ numberOfMessages++ + " новых сообщений");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, IMPORTANT_CHANNEL);
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentIntent(stackPending)
                .setAutoCancel(true)
                .setStyle(style)
                .setLights(Color.RED, 500, 500);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            builder.setPriority(Notification.PRIORITY_MAX)
                    .setLights(Color.RED, 500, 500)
                    .setVibrate(new long[]{
                            200, 100, 300, 100, 500
                    })
                    .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.new_message_tone));
        }

        mNotificationManagerCompat.notify(R.id.LOUVRE_NOTIFICATION_ID, builder.build());
    }

    public void inboxStyle(View view){
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle("Новые сообщения");
        style.addLine("У вас 5 писем от Петра");
        style.addLine("У вас 15 писем от Натальи");
        style.addLine("У вас 13 писем от Марии");
        style.addLine("У вас 7 писем от Александра");
        style.addLine("У вас 2 писем от Луки");
        style.addLine("У вас 99 писем от Павла");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);
        builder.setSmallIcon(android.R.drawable.ic_media_next)
                .setAutoCancel(true)
                .setStyle(style);

        mNotificationManagerCompat.notify(R.id.INBOX_STYLE_NOTIFICATION_ID, builder.build());
    }

    @SuppressLint("RemoteViewLayout")
    public void custom(View view){
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, R.id.BROWSER_PENDING_ID,
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.louvre.fr")),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        RemoteViews remote = new RemoteViews(
                getPackageName(),
                R.layout.custom
        );

        remote.setImageViewResource(R.id.picture, android.R.drawable.ic_media_pause);
        remote.setTextViewText(R.id.text, "My message");
        remote.setOnClickPendingIntent(R.id.buttonCustom, pendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);
        builder.setSmallIcon(android.R.drawable.ic_media_next)
                .setAutoCancel(true)
                .setContent(remote);

        mNotificationManagerCompat.notify(R.id.CUSTOM_NOTIFICATION_ID, builder.build());
    }

    public void inlineReply(View view){
        final String replyLabel = "Ответ";

        RemoteInput remoteInput = new RemoteInput.Builder(
                getResources().getString(R.string.KEY_TEXT_REPLY))
                .setLabel(replyLabel)
                .build();

        Intent intent = new Intent(this, ReplyActivity.class);
        PendingIntent replyPendingIntent = PendingIntent.getActivity(
                this,
                R.id.DIRECT_REPLY_NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.btn_star,"Ответить", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        Notification newMessageNotification = new NotificationCompat.Builder(this, NORMAL_CHANNEL)
                .setSmallIcon(android.R.drawable.btn_minus)
                .setContentTitle("Как насчет в кафе?")
                .setContentText("У меня тут выдалось свободное время, что думаешь?")
                .addAction(replyAction)
                .build();


        mNotificationManagerCompat.notify(
                R.id.DIRECT_REPLY_NOTIFICATION_ID,
                newMessageNotification
        );
    }

    public void progress(View view){
        Intent service = new Intent(this, ProgressIntentService.class);
        startService(service);
    }
}