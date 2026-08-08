/*
 * Copyright (c) 2008-2018 jeebiz.net.
 */
package com.github.scribejava.apis;

import com.github.scribejava.apis.service.WeiXinOAuth20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * OAuth 2.0 API implementation for WeChat (WeiXin).
 * <p>
 * Provides the authorization and access token endpoints required to authenticate
 * users via WeChat's OAuth 2.0 QR-code-based login flow. Uses the singleton
 * pattern via {@link #instance()}.
 * </p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see WeiXinOAuth20ServiceImpl
 * @see DefaultApi20
 */
public class WeiXinApi20 extends DefaultApi20 {

    /** The WeChat OAuth 2.0 QR-code authorization endpoint URL. */
    public static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/qrconnect";

    /** The WeChat OAuth 2.0 access token endpoint URL. */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * Protected constructor to enforce singleton usage via {@link #instance()}.
     */
    protected WeiXinApi20() {
    }

    private static class InstanceHolder {
        private static final WeiXinApi20 INSTANCE = new WeiXinApi20();
    }

    /**
     * Returns the singleton instance of the WeChat OAuth 2.0 API.
     *
     * @return the singleton {@link WeiXinApi20} instance
     */
    public static WeiXinApi20 instance() {
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
     * Returns the access token endpoint URL for WeChat OAuth 2.0.
     *
     * @return the WeChat access token URL
     */
    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    /**
     * Returns the base authorization URL for WeChat OAuth 2.0.
     *
     * @return the WeChat QR-code authorization URL
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
     * Creates a new {@link WeiXinOAuth20ServiceImpl} for the given configuration.
     *
     * @param config the OAuth configuration containing API key, secret, callback, etc.
     * @return a new {@link WeiXinOAuth20ServiceImpl} instance
     */
    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new WeiXinOAuth20ServiceImpl(this, config);
    }

}
