package ir.iut.komakdast.Service;


import android.os.Bundle;
import ir.iut.komakdast.Util.Logger;

import com.google.gson.Gson;

import ir.iut.komakdast.Adapter.DBAdapter;
import ir.iut.komakdast.Service.NotifObjects.NotifHolder;
import ir.iut.komakdast.Util.NotificationManager;

/**
 * Created by al on 4/24/16.
 */
public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {

    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    private static final String TAG = "GcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {


//        String message = data.getString("message");
//        if (message != null)
//            Logger.d(TAG, message);

        String notif = data.getString("notif");

        if (notif != null) {
            NotifHolder notifHolder = new Gson().fromJson(notif, NotifHolder.class);


            Logger.d(TAG, "got gcm " + notif);
            Logger.d(TAG, "serilized is  " + new Gson().toJson(notifHolder));
            DBAdapter dbAdapter = DBAdapter.getInstance(getApplicationContext());

            if (notifHolder.isMatchRequest()
                    && dbAdapter.getCoins() < 100)
                return;

//            (M)getApplication()
            ir.iut.komakdast.Util.NotificationManager manager = new NotificationManager(getApplicationContext());
            manager.createNotification(notifHolder);
        }

//        for(String key :data .keySet()){
//            Object obj = data.get(key);   //later parse it as per your required type
//            Logger.d(TAG, key + ":" + obj.toString());
//        }

    }


}
