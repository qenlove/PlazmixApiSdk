package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;
import me.qenlove.plazmix_sdk.models.Rank;

import java.util.UUID;

public class UserStaffExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        PlazmixApiClient api = new PlazmixApiClient();
        api.user()
                .staff()
                .rank(Rank.MODERATOR)
                .build()
                .execute()
                .thenAccept(staffResponse -> {
                    if (staffResponse.isSuccessful()) {
                        staffResponse.getStaff().forEach(userData -> {
                            System.out.println("==============");
                            System.out.println("Found " + userData.getNickname());
                            System.out.println("Rank " + userData.getGroups().getMainGroup().getName());
                        });
                    }
                });
    }

}
