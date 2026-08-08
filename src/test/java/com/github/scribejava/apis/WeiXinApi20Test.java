package com.github.scribejava.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.github.scribejava.apis.service.WeiXinOAuth20ServiceImpl;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.junit.jupiter.api.Test;

class WeiXinApi20Test {

    @Test
    void shouldReturnSingletonInstance() {
        WeiXinApi20 instance = WeiXinApi20.instance();
        assertNotNull(instance);
    }

    @Test
    void shouldReturnSameInstanceOnMultipleCalls() {
        WeiXinApi20 first = WeiXinApi20.instance();
        WeiXinApi20 second = WeiXinApi20.instance();
        assertSame(first, second);
    }

    @Test
    void shouldReturnGetAsAccessTokenVerb() {
        assertEquals(Verb.GET, WeiXinApi20.instance().getAccessTokenVerb());
    }

    @Test
    void shouldReturnCorrectAccessTokenEndpoint() {
        assertEquals("https://api.weixin.qq.com/sns/oauth2/access_token",
                WeiXinApi20.instance().getAccessTokenEndpoint());
    }

    @Test
    void shouldReturnNonNullAccessTokenExtractor() {
        assertNotNull(WeiXinApi20.instance().getAccessTokenExtractor());
    }

    @Test
    void shouldCreateWeiXinOAuth20ServiceImpl() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        OAuth20Service service = WeiXinApi20.instance().createService(config);
        assertNotNull(service);
        assertEquals(WeiXinOAuth20ServiceImpl.class, service.getClass());
    }

    @Test
    void shouldHaveCorrectAuthorizeUrl() {
        assertEquals("https://open.weixin.qq.com/connect/qrconnect", WeiXinApi20.AUTHORIZE_URL);
    }

    @Test
    void shouldHaveCorrectAccessTokenUrl() {
        assertEquals("https://api.weixin.qq.com/sns/oauth2/access_token", WeiXinApi20.ACCESS_TOKEN_URL);
    }
}
