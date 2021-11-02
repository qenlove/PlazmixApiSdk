package me.qenlove.plazmix_sdk.user;

import com.google.common.base.Preconditions;
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
import me.qenlove.plazmix_sdk.models.*;
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

    public OnlineStatusRequest.OnlineStatusRequestBuilder onlineStatus() {
        return OnlineStatusRequest.onlineStatusRequestBuilder();
    }

    public StaffRequest.StaffRequestBuilder staff() {
        return StaffRequest.staffRequestBuilder();
    }

    public MeRequest.MeRequestBuilder me() {
        return MeRequest.meRequestBuilder();
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record GetRequest(String token, String nickname, UUID uuid, int id) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.get";

        @Builder(builderClassName = "GetRequestBuilder", builderMethodName = "getRequestBuilder",
                access = AccessLevel.PUBLIC)
        public GetRequest {
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
    public record GetResponse(boolean successful, String errorName, String comment, UserData userData) {

        static GetResponse fromJsonResponse(String jsonResponse) {
            JsonObject object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            String comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            UserData userData = UserData.fromJsonObject(object);
            return new GetResponse(errorName == null, errorName, comment, userData);
        }
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record OnlineStatusRequest(String token, String nickname, UUID uuid, int id) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.onlineStatus";

        @Builder(builderClassName = "OnlineStatusRequestBuilder", builderMethodName = "onlineStatusRequestBuilder",
                access = AccessLevel.PUBLIC)
        public OnlineStatusRequest {
        }

        public CompletableFuture<OnlineStatusResponse> execute() {
            return ASYNC_HTTP_CLIENT.preparePost(REQUEST_URL_FORMAT)
                    .addHeader("Application-Token", token)
                    .addBodyPart(new StringPart("nickname", nickname))
                    .addBodyPart(new StringPart("uuid", uuid.toString()))
                    .addBodyPart(new StringPart("id", String.valueOf(id)))
                    .execute(new AsyncCompletionHandler<OnlineStatusResponse>() {
                        @Override
                        public OnlineStatusResponse onCompleted(Response response) {
                            return OnlineStatusResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record OnlineStatusResponse(boolean successful, String errorName, String comment,
                                       OnlineStatus status) {

        static OnlineStatusResponse fromJsonResponse(String jsonResponse) {
            JsonObject object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            String comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null); //todo COMMENT deserialized twice
            OnlineStatus status = Optional.ofNullable(object.get("status"))
                    .map(JsonElement::getAsJsonObject)
                    .map(OnlineStatus::fromJsonObject)
                    .orElse(null);
            return new OnlineStatusResponse(errorName == null, errorName, comment, status);
        }
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record StaffRequest(String token, Rank rank) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.staff";

        @Builder(builderClassName = "StaffRequestBuilder", builderMethodName = "staffRequestBuilder",
                access = AccessLevel.PUBLIC)
        public StaffRequest {
            Preconditions.checkState(rank.getRankType() == RankType.STAFF, "Specified rank " +
                    "doesn't belong to staff");
        }

        public CompletableFuture<StaffResponse> execute() {
            return ASYNC_HTTP_CLIENT.preparePost(REQUEST_URL_FORMAT)
                    .addHeader("Application-Token", token)
                    .addBodyPart(new StringPart("staff_group", rank.getRankType().toString()))
                    .execute(new AsyncCompletionHandler<StaffResponse>() {
                        @Override
                        public StaffResponse onCompleted(Response response) {
                            return StaffResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record StaffResponse(boolean successful, String errorName, String comment,
                                       List<UserData> staff) {

        static StaffResponse fromJsonResponse(String jsonResponse) {
            JsonObject object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            String comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            List<UserData> staff = Optional.ofNullable(object.get("staffs"))
                    .map(JsonElement::getAsJsonArray)
                    .map(jsonElements -> {
                        List<UserData> addableList = new ArrayList<>();
                        for (JsonElement element : jsonElements) {
                            addableList.add(UserData.fromJsonObject(element.getAsJsonObject()));
                        }
                        return addableList;
                    })
                    .orElse(new ArrayList<>());
            return new StaffResponse(errorName == null, errorName, comment, staff);
        }

    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record MeRequest(String token) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.me";

        @Builder(builderClassName = "MeRequestBuilder", builderMethodName = "meRequestBuilder",
                access = AccessLevel.PUBLIC)
        public MeRequest {
        }

        public CompletableFuture<MeResponse> execute() {
            return ASYNC_HTTP_CLIENT.prepareGet(REQUEST_URL_FORMAT)
                    .addHeader("Authorization", token)
                    .execute(new AsyncCompletionHandler<MeResponse>() {
                        @Override
                        public MeResponse onCompleted(Response response) {
                            return MeResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public record MeResponse(boolean successful, String errorName, String comment,
                                UserData userData) {

        static MeResponse fromJsonResponse(String jsonResponse) {
            JsonObject object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            String comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            UserData userData = UserData.fromJsonObject(object);
            return new MeResponse(errorName == null, errorName, comment, userData);
        }

    }

}
