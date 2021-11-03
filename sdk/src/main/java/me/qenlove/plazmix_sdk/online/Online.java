package me.qenlove.plazmix_sdk.online;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Builder;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.multipart.StringPart;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class Online {

    private static AsyncHttpClient ASYNC_HTTP_CLIENT = asyncHttpClient();

    public Online withSpecifiedAsyncHttpClient(AsyncHttpClient asyncHttpClient) {
        ASYNC_HTTP_CLIENT = asyncHttpClient;
        return this;
    }

    public OnlineRequest.OnlineRequestBuilder now() {
        return OnlineRequest.onlineRequestBuilder().day(null);
    }

    public OnlineRequest.OnlineRequestBuilder getFromIdentification() {
        return OnlineRequest.onlineRequestBuilder();
    }

    public static record OnlineRequest(String token, String day) {

        private static final String REQUEST_URL_FORMAT = "https://api.plazmix.net/v1/Online.now";

        @Builder(builderClassName = "OnlineRequestBuilder", builderMethodName = "onlineRequestBuilder",
                access = AccessLevel.PUBLIC)
        public OnlineRequest {
            Preconditions.checkNotNull(token, "Token not specified");
            Preconditions.checkNotNull(day, "Day not specified");
        }

        public CompletableFuture<OnlineResponse> execute() {
            var brb = (day == null ? ASYNC_HTTP_CLIENT.prepareGet(REQUEST_URL_FORMAT)
                    : ASYNC_HTTP_CLIENT.preparePost(REQUEST_URL_FORMAT).addBodyPart(new StringPart("day", day)));
            return brb.addHeader("Application-Token", token)
                    .execute(new AsyncCompletionHandler<OnlineResponse>() {
                        @Override
                        public OnlineResponse onCompleted(Response response) {
                            return OnlineResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    public static record OnlineResponse(boolean successful, String errorName, String comment, OnlineCount current,
                              OnlineCount peak, OnlineCount minimal) {

        static OnlineResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var current = Optional.ofNullable(object.get("current"))
                    .map(JsonElement::getAsJsonObject)
                    .map(OnlineCount::fromJsonObject)
                    .orElse(null);
            var peak = Optional.ofNullable(object.get("peak"))
                    .map(JsonElement::getAsJsonObject)
                    .map(OnlineCount::fromJsonObject)
                    .orElse(null);
            var minimal = Optional.ofNullable(object.get("minimal"))
                    .map(JsonElement::getAsJsonObject)
                    .map(OnlineCount::fromJsonObject)
                    .orElse(null);
            return new OnlineResponse(errorName == null, errorName, comment, current, peak, minimal);
        }
    }

}
