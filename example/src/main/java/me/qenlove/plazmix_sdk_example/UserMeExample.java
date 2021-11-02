package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;

public class UserMeExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        var api = new PlazmixApiClient();
        api.user()
                .me()
                .build()
                .execute()
                .thenAccept(meResponse -> {
                    if (meResponse.successful()) {
                        System.out.println("Authorized as " + meResponse.userData().nickname());
                    }
                });
    }

}
