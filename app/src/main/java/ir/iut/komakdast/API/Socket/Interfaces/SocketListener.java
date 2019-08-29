package ir.iut.komakdast.API.Socket.Interfaces;

import ir.iut.komakdast.API.Socket.Objects.GameResult.GameResultHolder;
import ir.iut.komakdast.API.Socket.Objects.GameStart.GameStartObject;
import ir.iut.komakdast.API.Socket.Objects.Result.ResultHolder;
import ir.iut.komakdast.API.Socket.Objects.UserAction.UserActionHolder;

/**
 * Created by al on 3/14/16.
 */
public interface SocketListener {


    void onGotGame(GameResultHolder gameHolder);

    void onGameStart(GameStartObject gameStartObject);

    void onGotUserAction(UserActionHolder actionHolder);

    void onFinishGame(ResultHolder resultHolder);



}
