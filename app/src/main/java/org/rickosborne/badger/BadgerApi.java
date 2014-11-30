package org.rickosborne.badger;

import org.rickosborne.badger.util.SecuredRestBuilder;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;

public class BadgerApi {

    public static Api build(String username, String password) {
        return new RestAdapter.Builder()
//                .setEndpoint(Api.BADGER_URL)
        // return new SecuredRestBuilder()
//            .setLoginEndpoint(Api.BADGER_URL + Api.TOKEN_PATH)
//            .setUsername(username)
//            .setPassword(password)
//            .setClientId(Api.CLIENT_ID)
            .setClient(new ApacheClient())
            .setEndpoint(Api.BADGER_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(Api.class);
    }

    public BadgerApi() {}

}
