package me.qenlove.plazmix_sdk.oauth;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.multipart.StringPart;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class PlayerAuthorizationCodeFlow {

    private static final AsyncHttpClient ASYNC_HTTP_CLIENT = asyncHttpClient();

    public ClientRequest.ClientRequestBuilder clientRequest() {
        return ClientRequest.clientRequestBuilder();
    }

    public TokenRequest.TokenRequestBuilder tokenRequest() {
        return TokenRequest.tokenRequestBuilder();
    }

    public record ClientRequest(ResponseType responseType, String clientId, String redirectUri, Scope scope,
                                String state) {

        private static final String REQUEST_URL_FORMAT = "https://auth.plazmix.net/oauth2/authorize?response_type=%s" +
                "&client_id=%s&redirect_uri=%s&scope=%s%s";

        @Builder(builderClassName = "ClientRequestBuilder", builderMethodName = "clientRequestBuilder",
                access = AccessLevel.PUBLIC)
        public ClientRequest {
            Preconditions.checkNotNull(responseType, "Response type not specified");
            Preconditions.checkNotNull(clientId, "Client ID not specified");
            Preconditions.checkNotNull(redirectUri, "Redirect URI not specified");
        }

        public String buildUrl() {
            return String.format(REQUEST_URL_FORMAT, responseType.getRequestValue(), clientId, redirectUri,
                    scope.getRequestValue(), (state != null ? "&" + state : ""));
        }
    }

    public record TokenRequest(String code, String clientSecret, GrantType grantType, String redirectUri) {

        public static final String REQUEST_URL = "https://api.plazmix.net/v1/Oauth2.accessToken";

        @Builder(builderClassName = "TokenRequestBuilder", builderMethodName = "tokenRequestBuilder",
                access = AccessLevel.PUBLIC)
        public TokenRequest {
            Preconditions.checkNotNull(code, "Code not specified");
            Preconditions.checkNotNull(clientSecret, "Client secret not specified");
            Preconditions.checkNotNull(grantType, "Grant type not specified");
            Preconditions.checkNotNull(redirectUri, "Redirect uri not specified");
        }

        public CompletableFuture<TokenResponse> execute() {
            return ASYNC_HTTP_CLIENT.preparePost(REQUEST_URL)
                    .addBodyPart(new StringPart("code", code))
                    .addBodyPart(new StringPart("client_secret", clientSecret))
                    .addBodyPart(new StringPart("grant_type", grantType.getRequestValue()))
                    .addBodyPart(new StringPart("redirect_uri", redirectUri))
                    .execute(new AsyncCompletionHandler<TokenResponse>() {
                        @Override
                        public TokenResponse onCompleted(Response response) {
                            return TokenResponse.fromJsonResponse(response.getResponseBody());
                        }
                    })
                    .toCompletableFuture();
        }
    }

    public record TokenResponse(boolean successful, String comment, String errorName, String accessToken,
                                String tokenType, String expiresIn) {

        static TokenResponse fromJsonResponse(String jsonResponse) {
            var object = JsonParser.parseString(jsonResponse).getAsJsonObject();

            var comment = Optional.ofNullable(object.get("comment"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var errorName = Optional.ofNullable(object.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var accessToken = Optional.ofNullable(object.get("access_token"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var tokenType = Optional.ofNullable(object.get("token_type"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var expiresIn = Optional.ofNullable(object.get("expires_in"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            var successful = accessToken != null;

            return new TokenResponse(successful, comment, errorName, accessToken, tokenType, expiresIn);
        }
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum ResponseType {
        CODE("code");

        String requestValue;
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum Scope {
        PROFILE("profile");

        String requestValue;
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum GrantType {
        AUTHORIZATION_CODE("authorization_code");

        String requestValue;
    }

}