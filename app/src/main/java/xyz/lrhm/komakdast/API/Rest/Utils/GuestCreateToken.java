package xyz.lrhm.komakdast.API.Rest.Utils;

import com.google.gson.annotations.Expose;

import xyz.lrhm.komakdast.Util.Tools;

/**
 * Created by al on 3/4/16.
 */
public class GuestCreateToken {

    @Expose
    boolean guest = true;

    @Expose
    String imei ;

    @Expose
    double seed;

    public GuestCreateToken(String imei) {
        this.imei = imei;
        seed = Tools.getSeed();
    }
}
