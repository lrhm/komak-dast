package xyz.lrhm.komakdast.API.Rest.Interfaces;

import xyz.lrhm.komakdast.Object.User;

/**
 * Created by al on 3/13/16.
 */
public interface BatchUserFoundListener {

    void onGotUserList(User[] users);

    void onGotError();

}
