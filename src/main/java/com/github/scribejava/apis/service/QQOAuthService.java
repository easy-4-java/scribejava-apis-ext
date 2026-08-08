/*
 * Copyright (c) 2008-2018 jeebiz.net.
 */
package com.github.scribejava.apis.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.scribejava.apis.QQApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * High-level OAuth 2.0 service for QQ (Tencent) that encapsulates the complete
 * authentication workflow: obtaining the login URL, exchanging an authorization code
 * for an access token, retrieving the user's OpenID, and fetching user profile information.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see QQApi20
 * @see QQOAuth20ServiceImpl
 */
public class QQOAuthService {

    /** URL endpoint for retrieving the user's OpenID. */
    private static final String OPEN_ID_URL = "https://graph.qq.com/oauth2.0/me";

    /** URL template for retrieving user profile information; requires API key and OpenID. */
    private static final String USER_INFO_URL = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String callbackUrl = "http://open.qtdebug.com:8080/oauth/qq/callback";
    private String apiKey      = "101292272";
    private String apiSecret   = "5bdbe9403fcc3abe8eba172337904b5a";
    private String scope       = "get_user_info";
    private OAuth20Service oauthService;

    /**
     * Constructs a new QQ OAuth service, initializing the underlying
     * {@link OAuth20Service} with the configured API key, secret, scope, and callback URL.
     */
    public QQOAuthService() {
        oauthService = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                .scope(scope).callback(callbackUrl).build(QQApi20.instance());
    }

    /**
     * Returns the QQ login page URL to which the user should be redirected
     * to authorize the application.
     *
     * @return the QQ OAuth 2.0 authorization URL
     */
    public String getLoginUrl() {
        return oauthService.getAuthorizationUrl();
    }

    /**
     * Exchanges the authorization code received from QQ's callback for an access token.
     *
     * @param code the authorization code returned by QQ after successful user login
     * @return the access token string
     * @throws IOException          if an I/O error occurs during the HTTP request
     * @throws InterruptedException if the calling thread is interrupted while waiting
     * @throws ExecutionException   if the asynchronous computation threw an exception
     */
    public String getAccessToken(String code) throws IOException, InterruptedException, ExecutionException {
        OAuth2AccessToken token = oauthService.getAccessToken(code);
        String accessToken = token.getAccessToken();
        return accessToken;
    }

    /**
     * Retrieves the user's OpenID from QQ. The OpenID is unique per user per application.
     *
     * @param accessToken the access token obtained during the OAuth flow
     * @return the user's QQ OpenID
     * @throws IOException if an I/O error occurs during the HTTP request
     */
    public String getOpenId(String accessToken) throws IOException {
        Response oauthResponse = request(oauthService, accessToken, OPEN_ID_URL);
        String responseBody = oauthResponse.getBody();
        int s = responseBody.indexOf("{");
        int e = responseBody.lastIndexOf("}") + 1;
        String json = responseBody.substring(s, e);
        JSONObject obj = JSONObject.parseObject(json);
        return obj.getString("openid");
    }

    /**
     * Retrieves the user's profile information from QQ, including nickname, avatar, etc.
     *
     * @param accessToken the access token obtained during the OAuth flow
     * @param openId      the user's QQ OpenID
     * @return a {@link JSONObject} containing the user's profile data
     * @throws IOException if an I/O error occurs during the HTTP request
     */
    public JSONObject getUserInfo(String accessToken, String openId) throws IOException {
        String url = String.format(USER_INFO_URL, apiKey, openId);
        Response oauthResponse = request(oauthService, accessToken, url);
        String responseJson = oauthResponse.getBody();
        return JSON.parseObject(responseJson);
    }

    /**
     * Executes an OAuth-signed HTTP request against the QQ API.
     *
     * @param service     the {@link OAuth20Service} to use for signing
     * @param accessToken the access token string for authentication
     * @param url         the target URL to request
     * @return the {@link Response} from the server
     */
    public Response request(OAuth20Service service, String accessToken, String url) {
        OAuth2AccessToken token = new OAuth2AccessToken(accessToken);
        OAuthRequest oauthRequest = new OAuthRequest(service.getApi().getAccessTokenVerb(), url, null);
        service.signRequest(token, oauthRequest);
        return null;
    }
}
