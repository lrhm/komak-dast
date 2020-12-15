package xyz.lrhm.komakdast.API.Rest.Utils;

import com.google.gson.annotations.Expose;

/**
 * Created by al on 5/18/16.
 */
public class Veryfier {

    @Expose
    boolean olduser;

    public boolean isOldUser() {
        return olduser;
    }
}
