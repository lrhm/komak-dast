package xyz.lrhm.komakdast.Adapter;

import xyz.lrhm.komakdast.API.Rest.AppAPIAdapter;
import xyz.lrhm.komakdast.Util.Logger;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import xyz.lrhm.komakdast.API.Rest.Interfaces.UserFoundListener;
import xyz.lrhm.komakdast.Object.User;
import xyz.lrhm.komakdast.Util.Tools;

/**
 * Created by al on 5/31/16.
 */
public class HiddenAdapter {

    private static final String KEY = "hdn_usr_komakdast";
    User hiddenUsr;
    private static HiddenAdapter instance;
    private static Object lock = new Object();

    public static HiddenAdapter getInstance() {

        synchronized (lock) {
            if (instance == null)
                instance = new HiddenAdapter();
        }
        return instance;

    }

    private HiddenAdapter() {
        if (Prefs.contains(KEY))
            hiddenUsr = new Gson().fromJson(Prefs.getString(KEY, ""), User.class);
    }

    public boolean isInitialized() {
        return Prefs.contains(KEY);
    }

    public User getHiddenUsr() {
        return hiddenUsr;
    }

    public void setHiddenUsr(User hiddenUsr) {
        this.hiddenUsr = hiddenUsr;
        Prefs.putString(KEY, new Gson().toJson(hiddenUsr));

        Logger.d(KEY, "hiddn created");
    }

    public void createHiddenUsr() {

        Logger.d(KEY, "createHiddnUsr");

        if (Tools.getCachedUser(null) != null || Tools.isThereOldUserToken()) {
            return;
        }

        if (hiddenUsr != null || isInitialized()) {

            return;
        }

        AppAPIAdapter.createHdnUser(new UserFoundListener() {
            @Override
            public void onGetUser(User user) {

            }

            @Override
            public void onGetError() {

            }

            @Override
            public void onGetMyUser(User myUser) {

            }

            @Override
            public void onForceLogout() {

            }
        });
    }
}
