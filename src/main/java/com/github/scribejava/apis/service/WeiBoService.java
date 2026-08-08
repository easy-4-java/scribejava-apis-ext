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
 * High-level OAuth 2.0 service for Sina Weibo that encapsulates the complete
 * authentication workflow: obtaining the login URL, exchanging an authorization code
 * for an access token, retrieving the user's UID, fetching user profile information,
 * and posting status updates.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see SinaWeiboApi20
 * @see SinaOAuth20ServiceImpl
 */
public class WeiBoService {

    /** URL endpoint for retrieving the authenticated user's UID. */
    private static final String U_ID_URL = "https://api.weibo.com/2/account/get_uid.json";

    /** URL template for retrieving user profile information by UID. */
    private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json?uid=%s";

    /** URL template for posting a status update (share) to Weibo. */
    private static final String USER_SHARE_URL = "https://api.weibo.com/2/statuses/share.json?status=%s";

    private String scope       = "all";
    private String callbackUrl = "http://lzclzc.tunnel.qydev.com/scribejava_Auth/oauth/weibo/callback.action";
    private String apiKey      = "3703387386";
    private String apiSecret   = "f525745eaa5fbaee169b23dae552049f";
    private OAuth20Service oauthService;

    /**
     * Constructs a new Weibo OAuth service, initializing the underlying
     * {@link OAuth20Service} with the configured API key, secret, scope, and callback URL.
     */
    public WeiBoService() {
        oauthService = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                .scope(scope).callback(callbackUrl).build(SinaWeiboApi20.instance());
    }

    /**
     * Returns the Weibo login page URL to which the user should be redirected
     * to authorize the application.
     *
     * @return the Weibo OAuth 2.0 authorization URL
     */
    public String getLoginUrl() {
        return oauthService.getAuthorizationUrl();
    }

    /**
     * Exchanges the authorization code received from Weibo's callback for an access token.
     *
     * @param code the authorization code returned by Weibo after successful user login
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
     * Retrieves the authenticated user's UID from Weibo.
     *
     * @param accessToken the access token obtained during the OAuth flow
     * @return the user's Weibo UID as a string
     * @throws IOException if an I/O error occurs during the HTTP request
     */
    public String getUserid(String accessToken) throws IOException {
        String url = String.format(U_ID_URL);
        Response oauthResponse = request(oauthService, accessToken, url, Verb.GET);
        String responseJson = oauthResponse.getBody();
        String uid = JSONObject.parseObject(responseJson).getString("uid");
        return uid;
    }

    /**
     * Retrieves the user's profile information from Weibo by UID.
     *
     * @param accessToken the access token obtained during the OAuth flow
     * @param uid         the user's Weibo UID
     * @return the raw JSON response string containing the user's profile data
     * @throws IOException if an I/O error occurs during the HTTP request
     */
    public String getUserInfo(String accessToken, String uid) throws IOException {
        String url = String.format(USER_INFO_URL, uid);
        Response oauthResponse = request(oauthService, accessToken, url, Verb.GET);
        String responseJson = oauthResponse.getBody();
        return responseJson;
    }

    /**
     * Posts a status update (share) to the authenticated user's Weibo timeline.
     *
     * @param accessToken the access token obtained during the OAuth flow
     * @param status      the status text to share
     * @return the raw JSON response string from the Weibo API
     * @throws IOException if an I/O error occurs during the HTTP request
     */
    public String UserShare(String accessToken, String status) throws IOException {
        String url = String.format(USER_SHARE_URL, status);
        Response oauthResponse = request(oauthService, accessToken, url, Verb.POST);
        String responseJson = oauthResponse.getBody();
        return responseJson;
    }

    /**
     * Executes an OAuth-signed HTTP request against the Weibo API.
     *
     * @param service     the {@link OAuth20Service} to use for signing
     * @param accessToken the access token string for authentication
     * @param url         the target URL to request
     * @param verb        the HTTP method (GET, POST, etc.)
     * @return the {@link Response} from the server
     */
    public Response request(OAuth20Service service, String accessToken, String url, Verb verb) {
        OAuth2AccessToken token = new OAuth2AccessToken(accessToken);
        OAuthRequest oauthRequest = new OAuthRequest(verb, url, null);
        service.signRequest(token, oauthRequest);
        return null;
    }
}
