package xyz.lrhm.komakdast;

import android.app.Application;


import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pixplicity.easyprefs.library.Prefs;



import xyz.lrhm.komakdast.Util.ImageManager;
import xyz.lrhm.komakdast.Util.LengthManager;


public class MainApplication extends Application {


    private LengthManager lengthManager;
    private ImageManager imageManager;
    private final static String TAG = "MainApplication";


    @Override
    public void onCreate() {

        super.onCreate();


        new Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();




        lengthManager = new LengthManager(this);
        imageManager = ImageManager.getInstance(this);

//        if(true){
//            throw new RuntimeException("Test Crash"); // Force a crash
//
//        }



    }

    public LengthManager getLengthManager() {
        return lengthManager;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }


    public void setLengthManager(LengthManager lengthManager) {
        this.lengthManager = lengthManager;
    }

    public void setImageManager(ImageManager imageManager) {
        this.imageManager = imageManager;
    }

}