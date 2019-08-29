package ir.iut.komakdast;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.pixplicity.easyprefs.library.Prefs;

import io.fabric.sdk.android.Fabric;


import ir.iut.komakdast.Util.ImageManager;
import ir.iut.komakdast.Util.LengthManager;


public class MainApplication extends Application {


    private LengthManager lengthManager;
    private ImageManager imageManager;
    private final static String TAG = "MainApplication";


    @Override
    public void onCreate() {

        super.onCreate();
        Fabric.with(this, new Crashlytics());


        new Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        lengthManager = new LengthManager(this);
        imageManager = ImageManager.getInstance(this);




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