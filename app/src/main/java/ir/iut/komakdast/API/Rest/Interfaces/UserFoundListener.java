package ir.iut.komakdast.API.Rest.Interfaces;

import ir.iut.komakdast.Object.User;

/**
 * Created by al on 3/4/16.
 */
public interface UserFoundListener {

    void onGetUser(User user);

    void onGetError();

    void onGetMyUser(User myUser);

    void onForceLogout();

}
