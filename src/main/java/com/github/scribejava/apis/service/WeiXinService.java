/*
 * Copyright (c) 2008-2018 jeebiz.net.
 */
package com.github.scribejava.apis.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.github.scribejava.apis.SinaWeiboApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * High-level OAuth 2.0 service for WeChat (WeiXin) that provides user authentication
 * and profile retrieval capabilities. Although named WeiXinService, this implementation
 * internally uses the Sina Weibo API infrastructure.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see SinaWeiboApi20
 * @see WeiXinOAuth20ServiceImpl
 */
public class WeiXinService {

    /** URL endpoint for retrieving the user's UID. */
    private static final String U_ID_URL = "https://api.weibo.com/2/account/get_uid.json";

    /** URL template for retrieving user profile information by UID. */
    private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json?uid=%s";

    private String callbackUrl = "http://lzclzc.tunnel.qydev.com/scribejava_Auth/oauth/weibo/callback.action";
    private String apiKey      = "3703387386";
    private String apiSecret   = "f525745eaa5fbaee169b23dae552049f";
    private String scope       = "all";
    private OAuth20Service oauthService;

    /**
     * Constructs a new WeiXin service, initializing the underlying
     * {@link OAuth20Service} with the configured API key, secret, scope, and callback URL.
     */
    public WeiXinService() {
        oauthService = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                .scope(scope).callback(callbackUrl).build(SinaWeiboApi20.instance());
    }

    /**
     * Returns the login page URL to which the user should be redirected
     * to authorize the application.
     *
     * @return the OAuth 2.0 authorization URL
     */
    public String getLoginUrl() {
        return oauthService.getAuthorizationUrl();
    }

    /**
     * Exchanges the authorization code received from the callback for an access token.
     *
     * @param code the authorization code returned after successful user login
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
     * Retrieves the authenticated user's UID.
     *
     * @param accessToken the access token obtained during the OAuth flow
     * @return the user's UID as a string
     * @throws IOException if an I/O error occurs during the HTTP request
     */
    public String getUserid(String accessToken) throws IOException {
        String url = String.format(U_ID_URL);
        Response oauthResponse = request(oauthService, accessToken, url);
        String responseJson = oauthResponse.getBody();
        String uid = JSONObject.parseObject(responseJson).getString("uid");
        return uid;
    }

    /**
     * Retrieves the user's profile information by UID.
     *
     * @param accessToken the access token obtained during the OAuth flow
     * @param uid         the user's UID
     * @return the raw JSON response string containing the user's profile data
     * @throws IOException if an I/O error occurs during the HTTP request
     */
    public String getUserInfo(String accessToken, String uid) throws IOException {
        String url = String.format(USER_INFO_URL, uid);
        Response oauthResponse = request(oauthService, accessToken, url);
        String responseJson = oauthResponse.getBody();
        return responseJson;
    }

    /**
     * Executes an OAuth-signed HTTP GET request against the API.
     *
     * @param service     the {@link OAuth20Service} to use for signing
     * @param accessToken the access token string for authentication
     * @param url         the target URL to request
     * @return the {@link Response} from the server
     */
    public Response request(OAuth20Service service, String accessToken, String url) {
        OAuth2AccessToken token = new OAuth2AccessToken(accessToken);
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, url, null);
        service.signRequest(token, oauthRequest);
        return null;
    }

}
