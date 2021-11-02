package me.qenlove.plazmix_sdk.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerGroups {

    Rank mainGroup;
    List<Rank> subordinateGroups;

    public static PlayerGroups fromJsonObject(JsonObject object) {
        Rank mainGroup = Optional.ofNullable(object.get("main_group"))
                .map(jsonElement -> {
                    JsonObject mainGroupObject = jsonElement.getAsJsonObject();
                    return Rank.valueOf(mainGroupObject.get("technical_name").getAsString());
                })
                .orElse(null);

        //todo Do something about it
        List<Rank> subordinateGroups = Optional.ofNullable(object.get("groups"))
                .map(jsonElement -> {
                    List<Rank> addableList = new ArrayList<>();
                    JsonArray array = jsonElement.getAsJsonArray();
                    for (JsonElement groupElement : array) {
                        addableList.add(Rank.valueOf(groupElement.getAsJsonObject().get("technical_name")
                                .getAsString()));
                    }
                    return addableList;
                })
                .orElse(new ArrayList<>());

        return new PlayerGroups(mainGroup, subordinateGroups);
    }

    public static PlayerGroups empty() {
        return new PlayerGroups(null, new ArrayList<>());
    }

}
