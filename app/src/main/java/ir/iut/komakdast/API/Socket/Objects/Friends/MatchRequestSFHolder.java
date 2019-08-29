package ir.iut.komakdast.API.Socket.Objects.Friends;

import com.google.gson.annotations.Expose;

import ir.iut.komakdast.Object.User;

/**
 * Created by al on 4/28/16.
 * user to server
 * user dst must be friends with src -> request match
 * <p/>
 * server to user , match requested
 */
public class MatchRequestSFHolder {


    @Expose
    User friend;

    @Expose
    String time;


    public User getFriend() {
        return friend;
    }

    public String getTime() {
        return time;
    }

    public String getFriendId() {
        return friend.getId();
    }

}
