package com.github.scribejava.apis.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.scribejava.apis.QQApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.junit.jupiter.api.Test;

class QQOAuth20ServiceImplTest {

    @Test
    void shouldCreateInstanceWithValidConfig() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        QQOAuth20ServiceImpl service = new QQOAuth20ServiceImpl(QQApi20.instance(), config);
        assertNotNull(service);
    }

    @Test
    void shouldExtractOpenIdAndAddToRequest() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        QQOAuth20ServiceImpl service = new QQOAuth20ServiceImpl(QQApi20.instance(), config);
        String rawResponse = "{\"openid\": \"test_openid_123\"}";
        OAuth2AccessToken token = new OAuth2AccessToken("test_token", rawResponse);
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api", null);

        service.signRequest(token, request);

        String completeUrl = request.getCompleteUrl();
        assertTrue(completeUrl.contains("openid=test_openid_123"),
                "Expected openid=test_openid_123 in URL but got: " + completeUrl);
    }

    @Test
    void shouldAddDataTypeJsonParameter() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        QQOAuth20ServiceImpl service = new QQOAuth20ServiceImpl(QQApi20.instance(), config);
        String rawResponse = "{\"openid\": \"test_openid\"}";
        OAuth2AccessToken token = new OAuth2AccessToken("test_token", rawResponse);
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api", null);

        service.signRequest(token, request);

        String completeUrl = request.getCompleteUrl();
        assertTrue(completeUrl.contains("dataType=json"),
                "Expected dataType=json in URL but got: " + completeUrl);
    }

    @Test
    void shouldThrowOAuthExceptionWhenOpenIdMissing() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        QQOAuth20ServiceImpl service = new QQOAuth20ServiceImpl(QQApi20.instance(), config);
        OAuth2AccessToken token = new OAuth2AccessToken("test_token", "no openid here");
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api", null);

        assertThrows(OAuthException.class, () -> service.signRequest(token, request));
    }

    @Test
    void shouldHandleOpenIdWithSpacesInJson() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        QQOAuth20ServiceImpl service = new QQOAuth20ServiceImpl(QQApi20.instance(), config);
        String rawResponse = "{\"openid\":   \"spaced_openid\"}";
        OAuth2AccessToken token = new OAuth2AccessToken("test_token", rawResponse);
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api", null);

        service.signRequest(token, request);

        String completeUrl = request.getCompleteUrl();
        assertTrue(completeUrl.contains("openid=spaced_openid"),
                "Expected openid=spaced_openid in URL but got: " + completeUrl);
    }
}
