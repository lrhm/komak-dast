package ir.iut.komakdast.API.Socket.Objects.GameResult;

import com.google.gson.annotations.Expose;

import ir.iut.komakdast.Object.User;

/**
 * Created by al on 3/15/16.
 */
public class GameResultHolder {

    @Expose
    OnlineLevel[] levels;

    @Expose
    User opponent;


    public User getOpponent() {
        return opponent;
    }

    public OnlineLevel[] getLevels() {
        return levels;
    }

}
