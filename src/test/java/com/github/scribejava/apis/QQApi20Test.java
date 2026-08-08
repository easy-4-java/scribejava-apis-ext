package com.github.scribejava.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.github.scribejava.apis.service.QQOAuth20ServiceImpl;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.junit.jupiter.api.Test;

class QQApi20Test {

    @Test
    void shouldReturnSingletonInstance() {
        QQApi20 instance = QQApi20.instance();
        assertNotNull(instance);
    }

    @Test
    void shouldReturnSameInstanceOnMultipleCalls() {
        QQApi20 first = QQApi20.instance();
        QQApi20 second = QQApi20.instance();
        assertSame(first, second);
    }

    @Test
    void shouldReturnGetAsAccessTokenVerb() {
        assertEquals(Verb.GET, QQApi20.instance().getAccessTokenVerb());
    }

    @Test
    void shouldReturnCorrectAccessTokenEndpoint() {
        assertEquals("https://graph.qq.com/oauth2.0/token",
                QQApi20.instance().getAccessTokenEndpoint());
    }

    @Test
    void shouldReturnNonNullAccessTokenExtractor() {
        assertNotNull(QQApi20.instance().getAccessTokenExtractor());
    }

    @Test
    void shouldCreateQQOAuth20ServiceImpl() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        OAuth20Service service = QQApi20.instance().createService(config);
        assertNotNull(service);
        assertEquals(QQOAuth20ServiceImpl.class, service.getClass());
    }

    @Test
    void shouldHaveCorrectAuthorizeUrl() {
        assertEquals("https://graph.qq.com/oauth2.0/authorize", QQApi20.AUTHORIZE_URL);
    }

    @Test
    void shouldHaveCorrectAccessTokenUrl() {
        assertEquals("https://graph.qq.com/oauth2.0/token", QQApi20.ACCESS_TOKEN_URL);
    }
}
