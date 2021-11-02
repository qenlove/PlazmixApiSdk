package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;
import me.qenlove.plazmix_sdk.models.Rank;

public class UserStaffExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        var api = new PlazmixApiClient();
        api.user()
                .staff()
                .rank(Rank.MODERATOR)
                .build()
                .execute()
                .thenAccept(staffResponse -> {
                    if (staffResponse.successful()) {
                        staffResponse.staff().forEach(userData -> {
                            System.out.println("==============");
                            System.out.println("Found " + userData.nickname());
                            System.out.println("Rank " + userData.groups().getMainGroup().getName());
                        });
                    }
                });
    }

}
