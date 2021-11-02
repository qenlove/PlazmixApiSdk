package me.qenlove.plazmix_sdk.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.qenlove.plazmix_sdk.models.PlayerGroups;
import me.qenlove.plazmix_sdk.models.Badge;
import me.qenlove.plazmix_sdk.models.Image;
import me.qenlove.plazmix_sdk.models.OnlineStatus;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.multipart.StringPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class User {

    private static final Gson GSON = new Gson();
    private static final AsyncHttpClient ASYNC_HTTP_CLIENT = asyncHttpClient();

    public GetRequest.GetRequestBuilder get() {
        return GetRequest.getRequestBuilder();
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class GetRequest {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.get";

        String token;
        String nickname;
        UUID uuid;
        int id;

        @Builder(builderClassName = "GetRequestBuilder", builderMethodName = "getRequestBuilder",
                access = AccessLevel.PUBLIC)
        private GetRequest(String token, String nickname, UUID uuid, int id) {
            this.token = token;
            this.nickname = nickname;
            this.uuid = uuid;
            this.id = id;
        }

        public CompletableFuture<GetResponse> execute() {
            return ASYNC_HTTP_CLIENT.preparePost(REQUEST_URL_FORMAT)
                    .addHeader("Authorization", token)
                    .addBodyPart(new StringPart("nickname", nickname))
                    .addBodyPart(new StringPart("uuid", uuid.toString()))
                    .addBodyPart(new StringPart("id", String.valueOf(id)))
                    .execute(new AsyncCompletionHandler<GetResponse>() {
                        @Override
                        public GetResponse onCompleted(Response response) {
                            return GetResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class GetResponse {

        boolean successful;
        String errorName;
        String comment;

        String nickname;
        UUID uuid;
        int id;
        int level;
        PlayerGroups groups;
        List<UUID> friends;
        List<Badge> badges;
        OnlineStatus onlineStatus;
        Image image;

        static GetResponse fromJsonResponse(String jsonResponse) {
            JsonObject object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            String comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);

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

            return new GetResponse(errorName == null, errorName, comment, nickname, uuid, id, level,
                    playerGroups, friends, badges, onlineStatus, image);
        }

    }

}
