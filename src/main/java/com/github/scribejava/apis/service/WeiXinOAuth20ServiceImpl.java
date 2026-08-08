package com.github.scribejava.apis.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.scribejava.apis.OAuth2Constants;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * OAuth 2.0 service implementation for WeChat (WeiXin).
 * <p>
 * Extends {@link OAuth20Service} to extract the user's {@code openid} from the
 * access token raw response using a regex pattern, then adds it along with a
 * {@code dataType=json} query parameter to every signed request.
 * </p>
 * <p>
 * If the {@code openid} cannot be found in the raw response, an {@link OAuthException}
 * is thrown.
 * </p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see com.github.scribejava.apis.WeiXinApi20
 * @see OAuth20Service
 */
public class WeiXinOAuth20ServiceImpl extends OAuth20Service {

    /** Regex pattern to extract the {@code openid} value from the WeChat access token response JSON. */
    private static Pattern openIdPattern = Pattern.compile("\"openid\":\\s*\"(\\S*?)\"");

    /**
     * Constructs a new WeChat OAuth 2.0 service.
     *
     * @param api    the {@link DefaultApi20} API descriptor for WeChat
     * @param config the OAuth configuration containing API key, secret, callback, etc.
     */
    public WeiXinOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    /**
     * Signs the given request by extracting the {@code openid} from the access token's
     * raw response, adding it along with {@code dataType=json} as query parameters,
     * and then delegating to the parent implementation.
     *
     * @param accessToken the OAuth 2.0 access token whose raw response contains the openid
     * @param request     the HTTP request to be signed
     * @throws OAuthException if the {@code openid} cannot be extracted from the raw response
     */
    @Override
    public void signRequest(OAuth2AccessToken accessToken, AbstractRequest request) {
        String response = accessToken.getRawResponse();
        Matcher matcher = openIdPattern.matcher(response);
        if (matcher.find()) {
            request.addQuerystringParameter(OAuth2Constants.OPENID, matcher.group(1));
        } else {
            throw new OAuthException("openid not found in response: " + response);
        }
        request.addQuerystringParameter("dataType", "json");
        super.signRequest(accessToken, request);
    }
}
