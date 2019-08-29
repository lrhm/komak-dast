package ir.iut.komakdast.API.Rest.Utils;

import com.google.gson.annotations.Expose;

import ir.iut.komakdast.Object.User;

/**
 * Created by al on 3/13/16.
 */
public class LeaderboardContainer {

    @Expose
    User[] board;

    @Expose
    User you;

    public User[] getBoard() {
        return board;
    }

    public User getYou() {
        return you;
    }
}
