package ir.treeco.aftabe2.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;

import ir.treeco.aftabe2.Synchronization.Synchronize;
import ir.treeco.aftabe2.View.Custom.ToastMaker;
import ir.treeco.aftabe2.View.Dialog.DialogAdapter;

/**
 * Created by al on 5/13/16.
 */
public class StoreAdapter {

    public static final String SKU_VERY_SMALL_COIN = "very_small_coin";
    public static final String SKU_SMALL_COIN = "small_coin";
    public static final String SKU_MEDIUM_COIN = "medium_coin";
    public static final String SKU_BIG_COIN = "big_coin";

    public static final int AMOUNT_VERY_SMALL_COIN = 500;
    public static final int AMOUNT_SMALL_COIN = 2000;
    public static final int AMOUNT_MEDIUM_COIN = 4000;
    public static final int AMOUNT_BIG_COIN = 12500;
    //    public static final int COMMENT_BAZAAR = 50;
    public static final int TELEGRAM = 50;
    public static final int INSTA = 50;


    static final int[] amounts = new int[]{
            AMOUNT_VERY_SMALL_COIN,
            AMOUNT_SMALL_COIN,
            AMOUNT_MEDIUM_COIN,
            AMOUNT_BIG_COIN
    };

    public final static int[] revenues = new int[]{
            AMOUNT_VERY_SMALL_COIN,
            AMOUNT_SMALL_COIN,
            AMOUNT_MEDIUM_COIN,
            AMOUNT_BIG_COIN,
//            COMMENT_BAZAAR,
            TELEGRAM,
            INSTA
    };


    final static int[] prices = new int[]{1000, 3000, 4000, 10000, -1, -1, -1, -1};

    public static int getTelegramAmount() {
        return TELEGRAM;
    }

    public static int getInstaAmount() {
        return INSTA;
    }



//    public static int getCommentBazaarAmount() {
////        return COMMENT_BAZAAR;
////    }

    static final String[] SKUs = new String[]{
            SKU_VERY_SMALL_COIN,
            SKU_SMALL_COIN,
            SKU_MEDIUM_COIN,
            SKU_BIG_COIN
    };

    private static HashMap<String, Integer> skuPrice;

    private static HashMap<String, Integer> skuAmount;

    public static int[] getRevenues() {
        return revenues;
    }

    public static int[] getPrices() {
        return prices;
    }

    public static String[] getSKUs() {
        return SKUs;
    }

    public static Integer getSkuAmount(String sku) {

        if (skuAmount == null) {
            skuAmount = new HashMap<>();
            for (int i = 0; i < SKUs.length; i++) {
                skuAmount.put(SKUs[i], amounts[i]);
            }
        }

        return skuAmount.get(sku);

    }

    public static boolean isInstaUsed() {
        return Prefs.getBoolean("com.instagram.android_view", false);
    }


    public static boolean isTelegramUsed() {
        return Prefs.getBoolean("org.telegram.messenger_view", false);
    }

    public static void useInsta() {
        Prefs.putBoolean("com.instagram.android_view", true);

        Tools.storeKey();
    }

    public static void useTelegram() {
        Prefs.putBoolean("org.telegram.messenger_view", true);

        Tools.storeKey();
    }

    public static Integer getPrice(String sku) {

        if (skuPrice == null) {
            skuPrice = new HashMap<>();

            for (int i = 0; i < SKUs.length; i++) {
                skuPrice.put(SKUs[i], prices[i]);
            }
        }

        return skuPrice.get(sku);
    }

    private static long lastClickTapsellAvailable = 0;



    public static boolean startViewPages(Context context, String appName, String name, String parse) {
        boolean isAppInstalled = isAppAvailable(context, appName);
        if (isAppInstalled) {
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(parse));
                myIntent.setPackage(appName);
                context.startActivity(myIntent);

            } catch (Exception e) {
                return false;
            }

        } else {

            ToastMaker.show(context, "نصب نیست " + name, Toast.LENGTH_SHORT);
        }
        return isAppInstalled;
    }

    public static boolean startInstaIntent(Context context) {
        String appName = "com.instagram.android";

        String parse = "http://instagram.com/_u/aftabe2";

        String name = "اینستا";

        return startViewPages(context, appName, name, parse);


    }

    public static boolean startTelegramIntent(Context context) {
        String appName = "org.telegram.messenger";

        String parse = "https://telegram.me/aftabe2";

        String name = "تلگرام";

        return startViewPages(context, appName, name, parse);
    }

    public static boolean isAppAvailable(Context context, String appName) {
        try {

            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
