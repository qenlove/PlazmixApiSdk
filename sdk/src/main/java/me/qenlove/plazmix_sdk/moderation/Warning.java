package me.qenlove.plazmix_sdk.moderation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;
import java.util.UUID;

public record Warning(int id, UUID authorUuid, String type, String reason, String createDateTimestamp,
                      boolean active) {

    static Warning fromJsonObject(JsonObject jsonObject) {
        var id = Optional.ofNullable(jsonObject.get("id"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsInt)
                .orElse(-1);
        var authorUuid = Optional.ofNullable(jsonObject.get("author"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .map(UUID::fromString)
                .orElse(null);
        var type = Optional.ofNullable(jsonObject.get("type"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .orElse(null);
        var reason = Optional.ofNullable(jsonObject.get("reason"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .orElse(null);
        var createDateTimestamp = Optional.ofNullable(jsonObject.get("create_date_timestamp"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .orElse(null);
        var active = jsonObject.has("active") && jsonObject.get("active").getAsBoolean();
        return new Warning(id, authorUuid, type, reason, createDateTimestamp, active);
    }

}
