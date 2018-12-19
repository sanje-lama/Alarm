package com.example.stl.standup;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.stl.standup.R.id.alarmToggle;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {




            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastMessage;
                if (isChecked) {

                    long repeatInterval = AlarmManager.ELAPSED_REALTIME_WAKEUP;

                    long triggerTime = SystemClock.elapsedRealtime()
                            + repeatInterval;


                    if (alarmManager != null) {
                        alarmManager.setInexactRepeating
                                (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                        triggerTime, repeatInterval,
                                        notifyPendingIntent);
                    }
                    toastMessage = getString(R.string.alarm_on_toast);

                } else {

                    mNotificationManager.cancelAll();

                    if (alarmManager != null) {
                        alarmManager.cancel(notifyPendingIntent);
                    }

                    toastMessage = getString(R.string.alarm_off_toast);

                }

                Toast.makeText(MainActivity.this, toastMessage,
                        Toast.LENGTH_SHORT).show();
            }

            public void createNotificationChannel() {

                mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >=
                        android.os.Build.VERSION_CODES.O) {

                    NotificationChannel notificationChannel = new NotificationChannel
                            (PRIMARY_CHANNEL_ID, "Stand up notification",NotificationManager.IMPORTANCE_HIGH);

                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setDescription("Notify every 15 minutes");
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }


                }



        });
    }
}
