package ir.iut.komakdast.Adapter;

import android.app.Activity;
import android.content.Context;

import ir.iut.komakdast.Util.Logger;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import ir.iut.komakdast.Util.Tools;
import ir.iut.komakdast.View.Dialog.SkipAlertDialog;

public class CoinAdapter {
    public static final int LEVEL_COMPELETED_PRIZE = 30;
    public static final int ALPHABET_HIDING_COST = 40;
    public static final int LETTER_REVEAL_COST = 50;
    public static final int SKIP_LEVEL_COST = 130;
    public static final String SHARED_PREF_COIN_DIFF = "aftabe_wc";

    private static final String LAST_TIME_USER_UPDATE = "last_time_user_upd";
    private static final String LAST_TIME_DIFF_SENT = "last_time_diff_sent";
    private static final String LAST_TIME_DIFF_TO_SERVER = "last_time_diff_sent2";

    private static final String TAG = "CoinAdapter";
    private DBAdapter db;
    private Context context;
    private Tools tools;
    private Activity mActivity;
    private static Object coinLock = new Object();

    public CoinAdapter(Context context, Activity activity) {
        this.context = context;
        db = DBAdapter.getInstance(context);
        tools = new Tools(context);
        mActivity = activity;
    }

    public interface CoinsChangedListener {
        void changed(int newAmount);
    }

    public boolean spendCoins(int amount) {
        return spendCoinsGeneric(amount, false);
    }

    public boolean spendCoinDiffless(int amount) {
        return spendCoinsGeneric(amount, true);
    }

    private boolean spendCoinsGeneric(int amount, boolean diffless) {
        int nextAmount = getCoinsCount() - amount;
        if (nextAmount < 0) {
//            ToastMaker.show(context, context.getString(R.string.not_enought_coins), Toast.LENGTH_SHORT);

            new SkipAlertDialog(context,  "سکه کافی ندارید").show();

            return false;
        }

        Logger.d(TAG, "spend coin " + amount);
        if (!diffless)
            addCoinDiff(-amount);
        setCoinsCount(nextAmount);
        return true;
    }

    public void earnCoins(int amount) {
        earnCoinsGeneric(amount, false);
    }

    public void earnCoinDiffless(int amount) {
        earnCoinsGeneric(amount, true);

    }

    private void earnCoinsGeneric(int amount, boolean diffless) {
        Logger.d(TAG, "earn coin " + amount + " is diffless " + diffless);
        int nextAmount = getCoinsCount() + amount;
        if (!diffless)
            addCoinDiff(amount);
        setCoinsCount(nextAmount);
    }

    public static int getCoinDiff() {

        synchronized (coinLock) {
            return Prefs.getInt(SHARED_PREF_COIN_DIFF, 0);
        }
    }

//    public static void setCoinDiff(int coinDiff) {
//        synchronized (coinLock) {
//            Prefs.putInt(SHARED_PREF_COIN_DIFF, coinDiff);
//
//        }
//    }

    public static void addCoinDiff(int diff) {
        Logger.d(TAG, "add coin diff " + diff);
        synchronized (coinLock) {
            Prefs.putInt(SHARED_PREF_COIN_DIFF, getCoinDiff() + diff);
        }
    }


    public int getCoinsCount() {
        return db.getCoins();
    }

    public void setCoinsCount(int nextAmount) {
        db.updateCoins(nextAmount);
        tools.backUpDB(context);

        for (CoinsChangedListener listener : listeners)
            listener.changed(nextAmount);
    }

    private static ArrayList<CoinsChangedListener> listeners = new ArrayList<>();

    public void setCoinsChangedListener(CoinsChangedListener listener) {
        listeners.add(listener);
        listener.changed(getCoinsCount());
    }

    public static void onDiffsent() {
        Prefs.putLong(LAST_TIME_DIFF_SENT, System.currentTimeMillis());
    }

    public static void onDiffToServer() {
        Prefs.putLong(LAST_TIME_DIFF_TO_SERVER, System.currentTimeMillis());
    }

    public static void onUserGet() {
        Prefs.putLong(LAST_TIME_USER_UPDATE, System.currentTimeMillis());
    }

    public static boolean shouldCheckUser() {
        return Prefs.getLong(LAST_TIME_USER_UPDATE, 1) > Prefs.getLong(LAST_TIME_DIFF_SENT, 0)
                && Prefs.getLong(LAST_TIME_USER_UPDATE, 1) > Prefs.getLong(LAST_TIME_DIFF_TO_SERVER, 0);
    }


}
