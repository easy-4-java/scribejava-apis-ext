package com.github.scribejava.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.github.scribejava.apis.service.OschinaOAuth20ServiceImpl;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.junit.jupiter.api.Test;

class OschinaApi20Test {

    @Test
    void shouldReturnSingletonInstance() {
        OschinaApi20 instance = OschinaApi20.instance();
        assertNotNull(instance);
    }

    @Test
    void shouldReturnSameInstanceOnMultipleCalls() {
        OschinaApi20 first = OschinaApi20.instance();
        OschinaApi20 second = OschinaApi20.instance();
        assertSame(first, second);
    }

    @Test
    void shouldReturnGetAsAccessTokenVerb() {
        assertEquals(Verb.GET, OschinaApi20.instance().getAccessTokenVerb());
    }

    @Test
    void shouldReturnCorrectAccessTokenEndpoint() {
        assertEquals("http://www.oschina.net/action/openapi/token",
                OschinaApi20.instance().getAccessTokenEndpoint());
    }

    @Test
    void shouldReturnNonNullAccessTokenExtractor() {
        assertNotNull(OschinaApi20.instance().getAccessTokenExtractor());
    }

    @Test
    void shouldCreateOschinaOAuth20ServiceImpl() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        OAuth20Service service = OschinaApi20.instance().createService(config);
        assertNotNull(service);
        assertEquals(OschinaOAuth20ServiceImpl.class, service.getClass());
    }

    @Test
    void shouldHaveCorrectAuthorizeUrl() {
        assertEquals("http://www.oschina.net/action/oauth2/authorize", OschinaApi20.AUTHORIZE_URL);
    }

    @Test
    void shouldHaveCorrectAccessTokenUrl() {
        assertEquals("http://www.oschina.net/action/openapi/token", OschinaApi20.ACCESS_TOKEN_URL);
    }
}
