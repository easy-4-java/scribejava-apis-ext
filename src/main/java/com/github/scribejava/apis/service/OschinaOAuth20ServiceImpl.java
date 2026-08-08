package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * OAuth 2.0 service implementation for OSChina (Open Source China).
 * <p>
 * Extends {@link OAuth20Service} to add a {@code dataType=json} query parameter
 * to every signed request, which instructs the OSChina API to return responses
 * in JSON format.
 * </p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see com.github.scribejava.apis.OschinaApi20
 * @see OAuth20Service
 */
public class OschinaOAuth20ServiceImpl extends OAuth20Service {

    /**
     * Constructs a new OSChina OAuth 2.0 service.
     *
     * @param api    the {@link DefaultApi20} API descriptor for OSChina
     * @param config the OAuth configuration containing API key, secret, callback, etc.
     */
    public OschinaOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    /**
     * Signs the given request by adding a {@code dataType=json} query parameter
     * and then delegating to the parent implementation.
     *
     * @param accessToken the OAuth 2.0 access token to use for signing
     * @param request     the HTTP request to be signed
     */
    @Override
    public void signRequest(OAuth2AccessToken accessToken, AbstractRequest request) {
        request.addQuerystringParameter("dataType", "json");
        super.signRequest(accessToken, request);
    }
}
