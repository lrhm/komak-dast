package ir.iut.komakdast.API.Socket.Objects.Notifs;

import com.google.gson.annotations.Expose;

import ir.iut.komakdast.Util.Tools;

/**
 * Created by al on 6/28/16.
 */
public class AdNotification {

    @Expose
    String title;

    @Expose
    String content;

    @Expose
    String imgUrl;

    @Expose
    String redirectURL;


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImgUrl() {
        return Tools.getUrl() + "api/pictures/notif/download/" + imgUrl;
    }

    public String getRedirectURL() {
        return redirectURL;
    }
}
