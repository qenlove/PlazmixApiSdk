package me.qenlove.plazmix_sdk.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public record OnlineStatus(OnlineStatus.OnlineValue status, String comment) {

    public static OnlineStatus fromJsonObject(JsonObject object) {
        var onlineValue = Optional.ofNullable(object.get("status"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .map(String::toUpperCase)
                .map(OnlineValue::valueOf)
                .orElse(OnlineValue.OFFLINE);
        var comment = Optional.ofNullable(object.get("comment"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .orElse(null);
        return new OnlineStatus(onlineValue, comment);
    }

    public enum OnlineValue {
        ONLINE, OFFLINE
    }

}
