package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;

import java.util.UUID;

public class UserGetExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        var api = new PlazmixApiClient();
        api.user()
                .get()
                .id(1)
                .nickname("test")
                .uuid(UUID.randomUUID())
                .build()
                .execute()
                .thenAccept(getResponse -> {
                    if (getResponse.successful()) {
                        System.out.println("Welcome back, " + getResponse.userData().nickname());
                    }
                });
    }

}
