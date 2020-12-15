package xyz.lrhm.komakdast.API.Rest.Utils;

import com.google.gson.annotations.Expose;

import xyz.lrhm.komakdast.Adapter.HiddenAdapter;
import xyz.lrhm.komakdast.Object.TokenHolder;
import xyz.lrhm.komakdast.Object.User;
import xyz.lrhm.komakdast.Util.RandomString;
import xyz.lrhm.komakdast.Util.Tools;

/**
 * Created by al on 3/4/16.
 */
public class SMSToken {

    @Expose
    public String phone;

    @Expose
    public String smsTokenId;

    @Expose
    public String imei;

    @Expose
    public String name;

    @Expose
    public String guestID;

    @Expose
    public double seed;

    public void update(SMSValidateToken smsValidateToken, String checkedUsername) {
        this.imei = RandomString.nextString();
        this.smsTokenId = smsValidateToken.getId();
        this.name = checkedUsername;
        this.phone = smsValidateToken.getPhone();
        seed = Tools.getSeed();
        setGuestID();
    }

    private void setGuestID() {


        if (!Tools.isUserRegistered()) {

            User hdn = HiddenAdapter.getInstance().getHiddenUsr();
            if (hdn != null)
                guestID = hdn.getLoginInfo().getUserId();

            return;
        }
        TokenHolder tokenHolder = Tools.getTokenHolder();
        if (tokenHolder == null)
            return;
        if (!tokenHolder.isGuest())
            return;
        LoginInfo loginInfo = tokenHolder.getLoginInfo();
        if (loginInfo != null)
            guestID = loginInfo.getUserId();
    }
}
