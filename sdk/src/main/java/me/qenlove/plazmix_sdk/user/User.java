package me.qenlove.plazmix_sdk.user;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Builder;
import me.qenlove.plazmix_sdk.models.OnlineStatus;
import me.qenlove.plazmix_sdk.models.Rank;
import me.qenlove.plazmix_sdk.models.RankType;
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

    public record GetRequest(String token, String nickname, UUID uuid, int id) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.get";

        @Builder(builderClassName = "GetRequestBuilder", builderMethodName = "getRequestBuilder",
                access = AccessLevel.PUBLIC)
        public GetRequest {
            Preconditions.checkNotNull(token, "Token not specified");
            Preconditions.checkNotNull(nickname, "Nickname not specified");
            Preconditions.checkNotNull(uuid, "UUID not specified");
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

    public record GetResponse(boolean successful, String errorName, String comment, UserData userData) {

        static GetResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var userData = UserData.fromJsonObject(object);
            return new GetResponse(errorName == null, errorName, comment, userData);
        }
    }

    public record OnlineStatusRequest(String token, String nickname, UUID uuid, int id) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.onlineStatus";

        @Builder(builderClassName = "OnlineStatusRequestBuilder", builderMethodName = "onlineStatusRequestBuilder",
                access = AccessLevel.PUBLIC)
        public OnlineStatusRequest {
            Preconditions.checkNotNull(token, "Token not specified");
            Preconditions.checkNotNull(nickname, "Nickname not specified");
            Preconditions.checkNotNull(uuid, "UUID not specified");
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

    public record OnlineStatusResponse(boolean successful, String errorName, String comment,
                                       OnlineStatus status) {

        static OnlineStatusResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null); //todo COMMENT deserialized twice
            var status = Optional.ofNullable(object.get("status"))
                    .map(JsonElement::getAsJsonObject)
                    .map(OnlineStatus::fromJsonObject)
                    .orElse(null);
            return new OnlineStatusResponse(errorName == null, errorName, comment, status);
        }
    }

    public record StaffRequest(String token, Rank rank) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.staff";

        @Builder(builderClassName = "StaffRequestBuilder", builderMethodName = "staffRequestBuilder",
                access = AccessLevel.PUBLIC)
        public StaffRequest {
            Preconditions.checkNotNull(token, "Token not specified");
            Preconditions.checkNotNull(rank, "Rank not specified");
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

    public record StaffResponse(boolean successful, String errorName, String comment,
                                       List<UserData> staff) {

        static StaffResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var staff = Optional.ofNullable(object.get("staffs"))
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

    public record MeRequest(String token) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/User.me";

        @Builder(builderClassName = "MeRequestBuilder", builderMethodName = "meRequestBuilder",
                access = AccessLevel.PUBLIC)
        public MeRequest {
            Preconditions.checkNotNull(token, "Token not specified");
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

    public record MeResponse(boolean successful, String errorName, String comment,
                                UserData userData) {

        static MeResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var userData = UserData.fromJsonObject(object);
            return new MeResponse(errorName == null, errorName, comment, userData);
        }
    }

}
