package me.qenlove.plazmix_sdk.online;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public record OnlineCount(int count, String timestamp) {

    static OnlineCount fromJsonObject(JsonObject jsonObject) {
        var count = Optional.ofNullable(jsonObject.get("count"))
                .map(JsonElement::getAsInt)
                .orElse(-1);
        var timestamp = Optional.ofNullable(jsonObject.get("timestamp"))
                .map(JsonElement::getAsString)
                .orElse(null);
        return new OnlineCount(count, timestamp);
    }

}
