package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;

import java.util.UUID;

public class UserOnlineStatusExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        var api = new PlazmixApiClient();
        api.user()
                .onlineStatus()
                .id(1)
                .nickname("test")
                .uuid(UUID.randomUUID())
                .build()
                .execute()
                .thenAccept(onlineStatusResponse -> {
                    if (onlineStatusResponse.successful()) {
                        System.out.println("You are " + onlineStatusResponse.status().status());
                        System.out.println("Comment " + onlineStatusResponse.status().comment());
                    }
                });
    }

}
