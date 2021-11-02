package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;
import me.qenlove.plazmix_sdk.oauth.PlayerAuthorizationCodeFlow;

public class OAuthExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        PlazmixApiClient api = new PlazmixApiClient();
        String url = api.oAuth()
                .playerAuthorizationCodeFlow()
                .clientRequest()
                .responseType(PlayerAuthorizationCodeFlow.ResponseType.CODE)
                .clientId("app_id")
                .redirectUri("redirect_uri_here")
                .scope(PlayerAuthorizationCodeFlow.Scope.PROFILE)
                .state("Return this please")
                .build()
                .buildUrl();
        // redirect client to url

        String code = "qwertyuiop"; // received from client at redirect_uri
        api.oAuth()
                .playerAuthorizationCodeFlow()
                .tokenRequest()
                .code(code)
                .clientSecret("app_secret")
                .grantType(PlayerAuthorizationCodeFlow.GrantType.AUTHORIZATION_CODE)
                .redirectUri("redirect_uri_here")
                .build()
                .execute()
                .thenAccept(tokenResponse -> {
                    System.out.println("The request was " +
                            (tokenResponse.isSuccessful() ? "successful" : "not successful"));
                    if (tokenResponse.isSuccessful()) {
                        System.out.println("Access token: " + tokenResponse.getAccessToken());
                        System.out.println("Token type: " + tokenResponse.getTokenType());
                        System.out.println("Expires: " + tokenResponse.getExpiresIn());
                    } else {
                        System.out.println("Error name: " + tokenResponse.getErrorName());
                        System.out.println("Comment: " + tokenResponse.getComment());
                    }
                });
    }

}
