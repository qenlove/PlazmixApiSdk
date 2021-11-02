package me.qenlove.plazmix_sdk.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Image {

    ImageIdentifier identifier;
    String skinRaw;
    EnumMap<VariantType, Variant> variants;

    public static Image fromJsonObject(JsonObject object) {
        ImageIdentifier identifier = Optional.ofNullable(object.get("identifier"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .map(String::toUpperCase)
                .map(ImageIdentifier::valueOf)
                .orElse(null);
        String skinRaw = Optional.ofNullable(object.get("skin_raw"))
                .map(JsonElement::getAsJsonObject)
                .map(JsonObject::getAsString)
                .orElse(null);

        EnumMap<VariantType, Variant> variants = Optional.ofNullable(object.get("variants"))
                .map(JsonElement::getAsJsonObject)
                .map(obj -> obj.keySet().stream()
                        .map(s -> new AbstractMap.SimpleEntry<>(VariantType.valueOf(s.toUpperCase()),
                                Variant.fromJsonObject(obj.get(s).getAsJsonObject())))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (key1, key2) -> {
                                    throw new IllegalArgumentException("Duplicate keys " + key1 + " and " + key2);
                                },
                                () -> new EnumMap<>(VariantType.class))))
                .orElse(new EnumMap<>(VariantType.class));
        return new Image(identifier, skinRaw, variants);
    }

    public enum ImageIdentifier {
        RAW
    }

    public enum VariantType {
        AVATAR, HEAD, BUST, BODY
    }

    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record Variant(String size100, String size150, String size300) {

        public static Variant fromJsonObject(JsonObject object) {
            String size100 = Optional.ofNullable(object.get("size_100").getAsJsonObject())
                    .map(JsonElement::getAsJsonObject)
                    .map(JsonObject::getAsString)
                    .orElse(null);
            String size150 = Optional.ofNullable(object.get("size_150").getAsJsonObject())
                    .map(JsonElement::getAsJsonObject)
                    .map(JsonObject::getAsString)
                    .orElse(null);
            String size300 = Optional.ofNullable(object.get("size_300").getAsJsonObject())
                    .map(JsonElement::getAsJsonObject)
                    .map(JsonObject::getAsString)
                    .orElse(null);
            return new Variant(size100, size150, size300);
        }
    }

}
