/*
 * Copyright (c) 2008-2018 jeebiz.net.
 */
package com.github.scribejava.apis;

import com.github.scribejava.apis.service.OschinaOAuth20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * OAuth 2.0 API implementation for OSChina (Open Source China).
 * <p>
 * Provides the authorization and access token endpoints required to authenticate
 * users via OSChina's OAuth 2.0 flow. Uses the singleton pattern via
 * {@link #instance()}.
 * </p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see OschinaOAuth20ServiceImpl
 * @see DefaultApi20
 */
public class OschinaApi20 extends DefaultApi20 {

    /** The OSChina OAuth 2.0 authorization endpoint URL. */
    public static final String AUTHORIZE_URL = "http://www.oschina.net/action/oauth2/authorize";

    /** The OSChina OAuth 2.0 access token endpoint URL. */
    public static final String ACCESS_TOKEN_URL = "http://www.oschina.net/action/openapi/token";

    /**
     * Protected constructor to enforce singleton usage via {@link #instance()}.
     */
    protected OschinaApi20() {
    }

    private static class InstanceHolder {
        private static final OschinaApi20 INSTANCE = new OschinaApi20();
    }

    /**
     * Returns the singleton instance of the OSChina OAuth 2.0 API.
     *
     * @return the singleton {@link OschinaApi20} instance
     */
    public static OschinaApi20 instance() {
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
     * Returns the base authorization URL for OSChina OAuth 2.0.
     *
     * @return the OSChina authorization URL
     */
    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    /**
     * Returns the access token endpoint URL for OSChina OAuth 2.0.
     *
     * @return the OSChina access token URL
     */
    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
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
     * Creates a new {@link OschinaOAuth20ServiceImpl} for the given configuration.
     *
     * @param config the OAuth configuration containing API key, secret, callback, etc.
     * @return a new {@link OschinaOAuth20ServiceImpl} instance
     */
    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new OschinaOAuth20ServiceImpl(this, config);
    }

}
