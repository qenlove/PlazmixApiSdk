package me.qenlove.plazmix_sdk;

import me.qenlove.plazmix_sdk.oauth.OAuth;
import me.qenlove.plazmix_sdk.user.User;

public class PlazmixApiClient {

    public OAuth oAuth() {
        return new OAuth();
    }

    public User user() {
        return new User();
    }

}
