package me.qenlove.plazmix_sdk.moderation;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Builder;
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

public class ModerationWarnings {

    private static final AsyncHttpClient ASYNC_HTTP_CLIENT = asyncHttpClient();

    public GetModeratorRequest.GetModeratorRequestBuilder getModerator() {
        return GetModeratorRequest.getModeratorRequestBuilder();
    }

    public GetRequest.GetRequestBuilder get() {
        return GetRequest.getRequestBuilder();
    }

    public record GetModeratorRequest(String token, UUID uuid) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/ModeratorAlert.getModerator";

        @Builder(builderClassName = "GetModeratorRequestBuilder", builderMethodName = "getModeratorRequestBuilder",
                access = AccessLevel.PUBLIC)
        public GetModeratorRequest {
            Preconditions.checkNotNull(token, "Token not specified");
            Preconditions.checkNotNull(uuid, "UUID not specified");
        }

        public CompletableFuture<GetModeratorResponse> execute() {
            return ASYNC_HTTP_CLIENT.preparePost(REQUEST_URL_FORMAT)
                    .addHeader("Application-Token", token)
                    .addBodyPart(new StringPart("uuid", uuid.toString()))
                    .execute(new AsyncCompletionHandler<GetModeratorResponse>() {
                        @Override
                        public GetModeratorResponse onCompleted(Response response) {
                            return GetModeratorResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    public record GetModeratorResponse(boolean successful, String errorName, String comment, List<Warning> warnings) {

        static GetModeratorResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var warnings = Optional.ofNullable(object.get("alerts"))
                    .map(JsonElement::getAsJsonArray)
                    .map(jsonElements -> {
                        List<Warning> addableMap = new ArrayList<>();
                        for (JsonElement element : jsonElements) {
                            addableMap.add(Warning.fromJsonObject(element.getAsJsonObject()));
                        }
                        return addableMap;
                    })
                    .orElse(new ArrayList<>());
            return new GetModeratorResponse(errorName == null, errorName, comment, warnings);
        }
    }

    public record GetRequest(String token, int alertId) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/ModeratorAlert.get";

        @Builder(builderClassName = "GetRequestBuilder", builderMethodName = "getRequestBuilder",
                access = AccessLevel.PUBLIC)
        public GetRequest {
            Preconditions.checkNotNull(token, "Token not specified");
        }

        public CompletableFuture<GetResponse> execute() {
            return ASYNC_HTTP_CLIENT.preparePost(REQUEST_URL_FORMAT)
                    .addHeader("Application-Token", token)
                    .addBodyPart(new StringPart("alert_id", String.valueOf(alertId)))
                    .execute(new AsyncCompletionHandler<GetResponse>() {
                        @Override
                        public GetResponse onCompleted(Response response) {
                            return GetResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    public record GetResponse(boolean successful, String errorName, String comment, Warning warning) {

        static GetResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var warning = Warning.fromJsonObject(object);
            return new GetResponse(errorName == null, errorName, comment, warning);
        }
    }

}
