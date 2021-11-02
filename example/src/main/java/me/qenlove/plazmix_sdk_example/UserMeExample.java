package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;

import java.util.UUID;

public class UserMeExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        PlazmixApiClient api = new PlazmixApiClient();
        api.user()
                .me()
                .build()
                .execute()
                .thenAccept(meResponse -> {
                    if (meResponse.isSuccessful()) {
                        System.out.println("Authorized as " + meResponse.getUserData().getNickname());
                    }
                });
    }

}
