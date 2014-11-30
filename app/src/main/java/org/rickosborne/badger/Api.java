package org.rickosborne.badger;

import org.rickosborne.badger.data.CheckIn;
import org.rickosborne.badger.data.User;
import org.rickosborne.badger.util.SecuredRestBuilder;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.http.*;

import java.util.Collection;

public interface Api {

    public static final String BADGER_URL = "http://192.168.42.205:8080";
    public static final String TOKEN_PATH = "/oauth/token";
    public static final String USER_ID = "userId";
    public static final String QUERY = "query";
    public static final String CLIENT_ID = "badgerMobile";

    @GET("/users") public Collection<User> findAllUsers();
    @GET("/users/me") public User getMe();
    @GET("/users?q={query}") public Collection<User> findUsers(@Path(QUERY) String query);
    @GET("/users/{userId}") public User getUserById(@Path(USER_ID) long userId);
    @GET("/users/{userId}/patients") public Collection<User> getPatientsForUser(@Path(USER_ID) long userId);
    @GET("/users/{userId}/checkIns") public Collection<CheckIn> getCheckInsForUser(@Path(USER_ID) long userId);
    @POST("/users/{userId}/checkIns") public CheckIn checkInUser(@Path(USER_ID) long userId, @Body CheckIn checkIn);
    @POST("/users") public User createUser(@Body User user);
    @PUT("/users/{userId}") public User updateUser(@Path(USER_ID) long userId, @Body User user);

}
