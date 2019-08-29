package ir.iut.komakdast.API.Socket.Objects.Notifs;

import com.google.gson.annotations.Expose;

/**
 * Created by al on 5/14/16.
 */
public class NotifCountHolder {

    @Expose
    int requests;

    @Expose
    int chats;

    public int getChats() {
        return chats;
    }

    public int getRequests() {
        return requests;
    }
}
