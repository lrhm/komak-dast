package ir.iut.komakdast.Util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.util.Random;

import ir.iut.komakdast.Object.PackageObject;
import ir.iut.komakdast.R;

import ir.iut.komakdast.View.Activity.LoadingActivity;

/**
 * Created by al on 5/1/16.
 */
public class NotificationManager {


    private static final String TAG = "NotificationManager";

    private Context context;

    public NotificationManager(Context context) {
        this.context = context;
    }

    private Context getBaseContext() {
        return context;
    }

    public void showNewPackageNotification(PackageObject packageObject) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.notificon)
                .setContentTitle("پکیج جدید افتابه")
                .setContentText(packageObject.getName())
                .setAutoCancel(true);


    }

    private NotificationCompat.Builder createBasicNotification(String title, String content, int drawable) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(drawable).setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
        return mBuilder;
    }

    private void showNotification(NotificationCompat.Builder builder, int id) {
        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) getBaseContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, builder.build());
    }

    public static void dismissNotification(Context context, int id) {
        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
    }

    public static void dismissALLNotification(Context context) {
        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }



}
