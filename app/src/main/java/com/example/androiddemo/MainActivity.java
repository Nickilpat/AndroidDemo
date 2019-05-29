package com.example.androiddemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
    }

    public void onClick(View view){
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Issue the initial notification with zero progress
        EditText lengthBox = findViewById(R.id.Length);
        final int PROGRESS_MAX = Integer.parseInt(lengthBox.getText().toString());
        final int PROGRESS_CURRENT = 0;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);


        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                int i = 0;
                while (i < PROGRESS_MAX){
                    try {
                        i += 1;
                        Thread.sleep(1000);
                        builder.setProgress(PROGRESS_MAX, i, false);
                        builder.setContentText("Look 20 feet away for "+ (PROGRESS_MAX - i) + " seconds");
                        builder.setOnlyAlertOnce(true);
                        notificationManager.notify(1, builder.build());

                    }
                    catch(InterruptedException ignored){}
                }
                builder.setProgress(0, 0, false);

                notificationManager.notify(1, builder.build());
                notificationManager.cancel(1);
                notificationManager.notify(2, finishbuilder.build());
            }
        }).start();

        Toast success = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
        success.show();
    }

    private final static String App_Package = "com.example.androiddemo";
    private final static String CHANNEL_ID = App_Package + ".LookAway_Channel";
    // --Commented out by Inspection (5/29/2019 12:09 AM):private final static String channelDescription = "General channel for notifications";
    private final static String notificationTitle = "Save your eyes!";
    private final static String notificationContent = "Look 20 feet away for 20 seconds";

    private final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH);

    private final NotificationCompat.Builder finishbuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("All Done!")
            .setContentText("Back to work!")
            .setPriority(NotificationCompat.PRIORITY_HIGH);

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            final NotificationManager notificationManager = getSystemService(NotificationManager.class);
            //channel.setSound();
            notificationManager.createNotificationChannel(channel);
        }
    }



}
