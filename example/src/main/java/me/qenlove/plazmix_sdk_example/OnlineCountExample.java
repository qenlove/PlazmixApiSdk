package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;

public class OnlineCountExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        var api = new PlazmixApiClient();
        api.online()
                .now()
                .build()
                .execute()
                .thenAccept(onlineResponse -> {
                    if (onlineResponse.successful()) {
                        System.out.println("ONLINE FOR TODAY:");
                        System.out.println("Minimum: " + onlineResponse.minimal().count());
                        System.out.println("Maximum: " + onlineResponse.peak().count());
                        System.out.println("Current: " + onlineResponse.current().count());
                    }
                });

        api.online()
                .getFromIdentification()
                .day("01-11-2021")
                .build()
                .execute()
                .thenAccept(onlineResponse -> {
                    if (onlineResponse.successful()) {
                        System.out.println("ONLINE FOR November 1st:");
                        System.out.println("Minimum: " + onlineResponse.minimal().count());
                        System.out.println("Maximum: " + onlineResponse.peak().count());
                        System.out.println("Current: " + onlineResponse.current().count());
                    }
                });
    }

}
