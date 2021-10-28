package com.example.allnotifications;

import android.app.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

public class ReplyActivity extends AppCompatActivity {
    private EditText reply;
    private NotificationManagerCompat manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        manager = NotificationManagerCompat.from(this);
        manager.cancel(R.id.DIRECT_REPLY_NOTIFICATION_ID);

        TextView request = findViewById(R.id.request);
        reply = findViewById(R.id.reply_text);
        Intent intent = getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            CharSequence requestText = remoteInput.getCharSequence(getResources().getString(R.string.KEY_TEXT_REPLY));
            request.setText(requestText);
        }
    }

    public void reply(View view) {
        String replyText = reply.getText().toString();

        Notification repliedNotification =
                new Notification.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_media_pause)
                        .setContentText("Ответ: " + replyText)
                        .build();

        manager.notify(R.id.DIRECT_REPLY_NOTIFICATION_ID, repliedNotification);
        onBackPressed();
    }
}
