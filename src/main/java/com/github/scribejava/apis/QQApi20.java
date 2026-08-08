/*
 * Copyright (c) 2008-2018 jeebiz.net.
 */
package com.github.scribejava.apis;

import com.github.scribejava.apis.service.QQOAuth20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * OAuth 2.0 API implementation for QQ (Tencent).
 * <p>
 * Provides the authorization and access token endpoints required to authenticate
 * users via QQ's OAuth 2.0 flow. Uses the singleton pattern via {@link #instance()}.
 * </p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see QQOAuth20ServiceImpl
 * @see DefaultApi20
 */
public class QQApi20 extends DefaultApi20 {

    /** The QQ OAuth 2.0 authorization endpoint URL. */
    public static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize";

    /** The QQ OAuth 2.0 access token endpoint URL. */
    public static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    /**
     * Protected constructor to enforce singleton usage via {@link #instance()}.
     */
    protected QQApi20() {
    }

    private static class InstanceHolder {
        private static final QQApi20 INSTANCE = new QQApi20();
    }

    /**
     * Returns the singleton instance of the QQ OAuth 2.0 API.
     *
     * @return the singleton {@link QQApi20} instance
     */
    public static QQApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Returns the HTTP verb used to request the access token.
     *
     * @return {@link Verb#GET}
     */
    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    /**
     * Returns the access token endpoint URL for QQ OAuth 2.0.
     *
     * @return the QQ access token URL
     */
    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    /**
     * Returns the base authorization URL for QQ OAuth 2.0.
     *
     * @return the QQ authorization URL
     */
    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    /**
     * Returns the default {@link OAuth2AccessTokenExtractor} for parsing access token responses.
     *
     * @return the {@link TokenExtractor} for {@link OAuth2AccessToken}
     */
    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }

    /**
     * Creates a new {@link QQOAuth20ServiceImpl} for the given configuration.
     *
     * @param config the OAuth configuration containing API key, secret, callback, etc.
     * @return a new {@link QQOAuth20ServiceImpl} instance
     */
    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new QQOAuth20ServiceImpl(this, config);
    }

}
