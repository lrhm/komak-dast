package xyz.lrhm.komakdast.API.Rest;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.lrhm.komakdast.API.Rest.Utils.AppListHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.CoinDiffHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.ContactsHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.CountHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.ForceObject;
import xyz.lrhm.komakdast.API.Rest.Utils.FriendRequestSent;
import xyz.lrhm.komakdast.API.Rest.Utils.GCMTokenHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.GoogleToken;
import xyz.lrhm.komakdast.API.Rest.Utils.GuestCreateToken;
import xyz.lrhm.komakdast.API.Rest.Utils.IndexHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.LSHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.LeaderboardContainer;
import xyz.lrhm.komakdast.API.Rest.Utils.LocationHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.SMSCodeHolder;
import xyz.lrhm.komakdast.API.Rest.Utils.SMSRequestToken;
import xyz.lrhm.komakdast.API.Rest.Utils.SMSToken;
import xyz.lrhm.komakdast.API.Rest.Utils.SMSValidateToken;
import xyz.lrhm.komakdast.API.Rest.Utils.UsernameCheck;
import xyz.lrhm.komakdast.API.Rest.Utils.Veryfier;
import xyz.lrhm.komakdast.Object.PackageObject;
import xyz.lrhm.komakdast.Object.User;
import xyz.lrhm.komakdast.API.Rest.Utils.LoginInfo;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by al on 3/4/16.
 */
public interface AppService {

    @POST("/api/users/login")
    Call<LoginInfo> getMyUserLogin(@Body GoogleToken googleToken);

    @POST("/api/users/login")
    Call<LoginInfo> getGuestUserLogin(@Body GuestCreateToken guestCreate);

    @POST("/api/smstokens")
    Call<SMSValidateToken> getSMSToken(@Body SMSRequestToken smsToken);

    @POST("/api/users/login")
    Call<LoginInfo> getSMSUserLogin(@Body SMSToken smsToken);

    @POST("/api/users/verifygtk")
    Call<Veryfier> verifyGCMOldUser(@Body GoogleToken googleToken);

    @GET("/api/users/me")
    Call<User> getMyUser(@Query("access_token") String accessToken);

    @GET("/api/users/{user_id}")
    Call<User> getUser(@Query("user_atk") String accessToken, @Path("user_id") String userId);

    @GET("/api/users/count")
    Call<UsernameCheck> checkUserName(@Query("where[name]") String username);

    @GET("/api/users/")
    Call<User[]> searchByUsername(@Query("access_token") String accessToken, @Query("filter[where][name][like]") String username);

    @GET("/api/users/")
    Call<User[]> searchByEmail(@Query("access_token") String accessToken, @Query("filter[where][email]") String mail);

    @GET("/api/users/")
    Call<User[]> searchByPhoneNumber(@Query("access_token") String accessToken, @Query("filter[where][phone]") String phoneNumber);

    @PUT("/api/smstokens/{user_id}")
    Call<SMSValidateToken> checkSMSCodeReq(@Body SMSCodeHolder smsCodeHolder, @Path("user_id") String userId);

    @GET("/api/users/")
    Call<LeaderboardContainer> getLeaderboard(@Query("access_token") String accessToken, @Query("filter[order]") String filter);

    @PUT("/api/users/{user_id}")
    Call<User> updateCoin(@Body CoinDiffHolder coinDiffHolder, @Path("user_id") String userId, @Query("access_token") String accessToken);

    @PUT("/api/users/{user_id}")
    Call<User> updatePackagesList(@Body AppListHolder apps, @Path("user_id") String userId, @Query("access_token") String accessToken);


    @PUT("/api/users/{user_id}/requests/rel/{friend_id}")
    Call<FriendRequestSent> requestFriend(@Path("user_id") String userId, @Path("friend_id") String friendId,
                                          @Query("access_token") String accessToken);

    @GET("/api/users/{user_id}/requests")
    Call<User[]> getListOfSentFriendRequests(@Path("user_id") String userId, @Query("access_token") String accessToken);

    @DELETE("/api/users/{user_id}/requests/rel/{friend_id}")
    Call<HashMap<String, Object>> setCancelFriendRequest(@Path("user_id") String userId, @Path("friend_id") String friendId,
                                                         @Query("access_token") String accessToken);

    @GET("/api/users/myrequests")
    Call<User[]> getListOfFriendRequestsToMe(@Query("access_token") String accessToken);

    @GET("/api/users/{user_id}/friends/")
    Call<User[]> getListOfMyFriends(@Path("user_id") String userId, @Query("access_token") String accessToken);

    @DELETE("/api/users/{user_id}/friends/rel/{friend_id}")
    Call<HashMap<String, Object>> setRemoveFriend(@Path("user_id") String userId, @Path("friend_id") String friendId,
                                                  @Query("access_token") String accessToken);

    @PUT("/api/users/{user_id}")
    Call<User> updateGCMToken(@Path("user_id") String userId, @Query("access_token") String accessToken,
                              @Body GCMTokenHolder gcmTokenHolder);

    @POST("/api/users/{user_id}/locs")
    Call<HashMap<String, Object>> putLocation(@Path("user_id") String userId,
                                              @Query("access_token") String accessToken, @Body LocationHolder locationHolder);

    @POST("/api/users/{user_id}/contacts")
    Call<HashMap<String, String>> putContacts(@Path("user_id") String userId,
                                              @Query("access_token") String accessToken, @Body ContactsHolder contactsHolder);


    @GET("/api/p2s")
    Call<PackageObject[]> getAllPackages();

    @GET("/api/p2s/{id}")
    Call<PackageObject> getPackage(@Path("id") String id);

    @GET("/api/p2s/count")
    Call<CountHolder> getPackagesCount();

    @GET("/api/users/buypackage2/{id}")
    Call<ArrayList<Integer>> buyPackages(@Path("id") String packageId, @Query("access_token") String accessToken);

    @GET("/api/users/mycontacts")
    Call<User[]> checkCTS(@Query("access_token") String accessToken);

    @GET("/api/versions")
    Call<ForceObject> checkForceUpdate();

    @POST("/api/users/buypackage2/{id}")
    Call<HashMap<String, Object>> updateLastLevelSolved(@Path("id") String packageId,
                                                        @Query("access_token") String accessToken,
                                                        @Body IndexHolder indexHolder);

    @POST("/api/users/lgs")
    Call<HashMap<String, Object>> updateLS(
            @Query("access_token") String accessToken, @Body LSHolder lsHolder);


}