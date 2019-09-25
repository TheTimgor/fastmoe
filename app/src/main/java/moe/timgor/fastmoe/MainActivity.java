package moe.timgor.fastmoe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public static final String CHANNEL_ID = "fastmoe";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, Givelink.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,
                0, notificationIntent, 0);
        Intent downloadIntent = new Intent(this, Downloadimg.class);
        PendingIntent pendingDownloadIntent = PendingIntent.getService(this,
                0, downloadIntent, 0);



        Notification notification =
                new Notification.Builder(this.getApplicationContext(), CHANNEL_ID)
                        .setContentTitle("fastmoe via timgor.moe/api/random")
                        .setContentText("Click to copy")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setOngoing(true)
                        .addAction(R.mipmap.ic_launcher_round, "Download", pendingDownloadIntent)
                        .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
