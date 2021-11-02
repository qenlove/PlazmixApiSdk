package me.qenlove.plazmix_sdk_example;

import me.qenlove.plazmix_sdk.PlazmixApiClient;

import java.util.UUID;

public class ModeratorWarningsExample {

    @SuppressWarnings("All")
    public static void main(String... args) {
        var api = new PlazmixApiClient();
        api.moderationWarnings()
                .getModerator()
                .uuid(UUID.randomUUID())
                .build()
                .execute()
                .thenAccept(moderatorResponse -> {
                    if (moderatorResponse.successful()) {
                        System.out.println("Moders warnings count: " + moderatorResponse.warnings());
                    }
                });

        var warningId = 12345; //specific warning
        api.moderationWarnings()
                .get()
                .alertId(warningId)
                .build()
                .execute()
                .thenAccept(getResponse -> {
                    if (getResponse.successful()) {
                        System.out.println("Warning from " + getResponse.warning().authorUuid());
                        System.out.println("Reason " + getResponse.warning().reason());
                    }
                });
    }

}
