package ir.iut.komakdast.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import ir.iut.komakdast.Adapter.ContactsAdapter;
import ir.iut.komakdast.Adapter.DBAdapter;
import ir.iut.komakdast.Adapter.LocationAdapter;
import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.Logger;
import ir.iut.komakdast.Util.PackageTools;
import ir.iut.komakdast.Util.SizeManager;
import ir.iut.komakdast.Util.Tools;

public class LoadingActivity extends Activity implements Runnable {

    private static final String TAG = "LoadingActivity";
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        ImageView imageView = (ImageView) findViewById(R.id.loading_logi);

        SizeManager.initSizes(this);

        Glide.with(this).load(R.drawable.new_icon).into(imageView);

//        imageView.setImageBitmap(ImageManager.getInstance(this).loadImageFromResource(R.drawable.interpreting, logiConverter.mWidth, logiConverter.mHeight));
//        synegy.setImageBitmap(ImageManager.getInstance(this).loadImageFromResource(R.drawable.synergy, synergyConverter.mWidth, synergyConverter.mHeight , ImageManager.ScalingLogic.FIT));
//


        startTime = System.currentTimeMillis();

        Logger.d("Loading", "inLoading");


        new Handler().postDelayed(this, 600);

    }


    @Override
    public void run() {

        initUtils();
        new LocationAdapter(this);
        new ContactsAdapter(this);
        long diff = System.currentTimeMillis() - startTime;

        if (diff < 1000)
            try {
                Thread.sleep(1000 - diff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Logger.d(TAG, "onCreate LoadingActivity");

        if (getIntent() != null && getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object obj = getIntent().getExtras().get(key);   //later parse it as per your required type
                Logger.d("LoadingActivity", key + ":" + obj.toString());
            }
            intent.putExtras(getIntent().getExtras());
        }

//        if (!Prefs.getBoolean(IntroActivity.INTRO_SHOWN, false)) {
//            intent = new Intent(this, IntroActivity.class);
//        }

        startActivity(intent);
        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    private void initUtils() {


        Tools tools = new Tools(this);

        if (Prefs.getBoolean("firstAppRun", true)) {
            Tools.checkKey();
        }

        tools.checkDB(this);

        DBAdapter db = DBAdapter.getInstance(getApplication());

        PackageTools.getInstance(this).checkOfflinePackageCompatibility();

        if (Prefs.getBoolean("firstAppRun", true)) {

            db.insertCoins(500);
            PackageTools.getInstance(this).copyLocalpackages();
            Prefs.putBoolean("firstAppRun", false);
        }


    }

}
