package xyz.lrhm.komakdast.Synchronization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import xyz.lrhm.komakdast.Util.Logger;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import xyz.lrhm.komakdast.Adapter.Cache.AppListAdapter;
import xyz.lrhm.komakdast.Object.PackageObject;
import xyz.lrhm.komakdast.R;
import xyz.lrhm.komakdast.Util.GlobalPrefs;
import xyz.lrhm.komakdast.Util.NotificationManager;
import xyz.lrhm.komakdast.Util.PackageTools;

public class Synchronize extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {


        if(!isOnline(context))
            return;

        Date now = Calendar.getInstance().getTime();
        Date past = new Date();
        try {
            SharedPreferences sp = GlobalPrefs.getInstance(context).getSharedPrefs();
            past = new SimpleDateFormat("dd-MM-yyyy").
                    parse(sp.getString(
                            context.getResources()
                                    .getString(R.string.updated_time_shared_preference)
                            , new SimpleDateFormat("dd-MM-yyyy").format(now)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int days = Days.daysBetween(new DateTime(past), new DateTime(now)).getDays();
        if (days >= 1) {

            PackageTools.getInstance(context).checkForNewPackage(new PackageTools.OnNewPackageFoundListener() {
                @Override
                public void onNewPackage(PackageObject packageObject) {

                    NotificationManager manager = new NotificationManager(context);
                    manager.showNewPackageNotification(packageObject);
                }

                @Override
                public void onPackageInvalid(PackageObject packageObject) {

                }

                @Override
                public void onPackageOffer(PackageObject newOffer) {

                }
            });
        }

        AppListAdapter.getInstance(context);

        Logger.d("TAG", "synchronize");

    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}
