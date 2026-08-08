/*
 * Copyright (c) 2008-2018 jeebiz.net.
 */
package com.github.scribejava.apis;

/**
 * Common OAuth 2.0 parameter name constants used across various Chinese API implementations
 * such as QQ, WeChat (WeiXin), Sina Weibo, and OSChina.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see com.github.scribejava.core.oauth.OAuth20Service
 * @see com.github.scribejava.core.builder.api.DefaultApi20
 */
public class OAuth2Constants {

    /** Parameter key for the user's unique identifier. */
    public static final String ID = "id";

    /** Parameter key for the user's OpenID, commonly used in QQ and WeChat APIs. */
    public static final String OPENID = "openid";

    /** Parameter key for the OAuth consumer key (API key). */
    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";

    /** Parameter key for the OAuth grant type (e.g. "authorization_code"). */
    public static final String GRANT_TYPE = "grant_type";

    /** Parameter key for the OAuth response type (e.g. "code"). */
    public static final String RESPONSE_TYPE = "response_type";

}
