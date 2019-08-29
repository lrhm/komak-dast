package ir.iut.komakdast.API.Socket.Interfaces;

import ir.iut.komakdast.API.Socket.Objects.Friends.MatchRequestSFHolder;
import ir.iut.komakdast.API.Socket.Objects.Friends.MatchResultHolder;
import ir.iut.komakdast.API.Socket.Objects.Friends.OnlineFriendStatusHolder;

/**
 * Created by al on 4/28/16.
 */
public interface SocketFriendMatchListener {

    void onMatchRequest(MatchRequestSFHolder request);

    void onOnlineFriendStatus(OnlineFriendStatusHolder status);

    void onMatchResultToSender(MatchResultHolder result);
}
