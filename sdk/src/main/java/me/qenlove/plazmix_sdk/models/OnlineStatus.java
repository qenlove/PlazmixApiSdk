package me.qenlove.plazmix_sdk.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public record OnlineStatus(OnlineStatus.OnlineValue status, String comment) {

    public static OnlineStatus fromJsonObject(JsonObject object) {
        OnlineValue onlineValue = Optional.ofNullable(object.get("status"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .map(String::toUpperCase)
                .map(OnlineValue::valueOf)
                .orElse(OnlineValue.OFFLINE);
        String comment = Optional.ofNullable(object.get("comment"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .orElse(null);
        return new OnlineStatus(onlineValue, comment);
    }

    public enum OnlineValue {
        ONLINE, OFFLINE
    }

}
