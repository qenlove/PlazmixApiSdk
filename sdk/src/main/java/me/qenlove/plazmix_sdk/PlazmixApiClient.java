package me.qenlove.plazmix_sdk;

import me.qenlove.plazmix_sdk.moderation.ModerationWarnings;
import me.qenlove.plazmix_sdk.oauth.OAuth;
import me.qenlove.plazmix_sdk.online.Online;
import me.qenlove.plazmix_sdk.user.User;

public class PlazmixApiClient {

    public OAuth oAuth() {
        return new OAuth();
    }

    public User user() {
        return new User();
    }

    public Online online() {
        return new Online();
    }

    public ModerationWarnings moderationWarnings() {
        return new ModerationWarnings();
    }

}
