package me.qenlove.plazmix_sdk.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qenlove.plazmix_sdk.models.Badge;
import me.qenlove.plazmix_sdk.models.Image;
import me.qenlove.plazmix_sdk.models.OnlineStatus;
import me.qenlove.plazmix_sdk.models.PlayerGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserData {

    private static final Gson GSON = new Gson();

    String nickname;
    UUID uuid;
    int id;
    int level;
    PlayerGroups groups;
    List<UUID> friends;
    java.util.List<Badge> badges;
    OnlineStatus onlineStatus;
    Image image;

    static UserData fromJsonObject(JsonObject object) {
        String nickname = Optional.ofNullable(object.get("nickname"))
                .map(JsonElement::getAsString)
                .orElse(null);
        UUID uuid = Optional.ofNullable(object.get("uuid"))
                .map(JsonElement::getAsString)
                .map(UUID::fromString)
                .orElse(null);
        int id = Optional.ofNullable(object.get("id"))
                .map(JsonElement::getAsInt)
                .orElse(-1);
        int level = Optional.ofNullable(object.get("level"))
                .map(JsonElement::getAsInt)
                .orElse(-1);
        PlayerGroups playerGroups = Optional.ofNullable(object.get("groups"))
                .map(JsonElement::getAsJsonObject)
                .map(PlayerGroups::fromJsonObject)
                .orElse(PlayerGroups.empty());
        List<UUID> friends = Optional.ofNullable(object.get("friends"))
                .map(jsonElement -> GSON.<List<UUID>>fromJson(jsonElement,
                        TypeToken.getParameterized(ArrayList.class, UUID.class).getType()))
                .orElse(new ArrayList<>());
        List<Badge> badges = Optional.ofNullable(object.get("badges"))
                .map(JsonElement::getAsJsonArray)
                .map(jsonElements -> {
                    List<Badge> addableList = new ArrayList<>();
                    for (JsonElement element : jsonElements) {
                        addableList.add(Badge.valueOf(element.getAsJsonObject().get("technical_name").getAsString()
                                .toUpperCase()));
                    }
                    return addableList;
                })
                .orElse(new ArrayList<>());
        OnlineStatus onlineStatus = Optional.ofNullable(object.get("online"))
                .map(JsonElement::getAsJsonObject)
                .map(OnlineStatus::fromJsonObject)
                .orElse(null);
        Image image = Optional.ofNullable(object.get("image"))
                .map(JsonElement::getAsJsonObject)
                .map(Image::fromJsonObject)
                .orElse(null);
        return new UserData(nickname, uuid, id, level, playerGroups, friends, badges, onlineStatus, image);
    }

}
