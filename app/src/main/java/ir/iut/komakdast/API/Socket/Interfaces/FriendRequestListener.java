package ir.iut.komakdast.API.Socket.Interfaces;

import ir.iut.komakdast.Object.User;

/**
 * Created by al on 4/29/16.
 */
public interface FriendRequestListener {

    void onFriendRequest(User user);

    void onFriendRequestAccept(User user);

    void onFriendRequestReject(User user);
}
