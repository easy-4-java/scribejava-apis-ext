package com.github.scribejava.apis.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.scribejava.apis.SinaWeiboApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

import org.junit.jupiter.api.Test;

class SinaOAuth20ServiceImplTest {

    @Test
    void shouldCreateInstanceWithValidConfig() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        SinaOAuth20ServiceImpl service = new SinaOAuth20ServiceImpl(SinaWeiboApi20.instance(), config);
        assertNotNull(service);
    }

    @Test
    void shouldAddDataTypeJsonParameterOnSignRequest() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        SinaOAuth20ServiceImpl service = new SinaOAuth20ServiceImpl(SinaWeiboApi20.instance(), config);
        OAuth2AccessToken token = new OAuth2AccessToken("test_token");
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api", null);

        service.signRequest(token, request);

        String completeUrl = request.getCompleteUrl();
        assertTrue(completeUrl.contains("dataType=json"),
                "Expected dataType=json in URL but got: " + completeUrl);
    }

    @Test
    void shouldDelegateToParentSignRequest() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        SinaOAuth20ServiceImpl service = new SinaOAuth20ServiceImpl(SinaWeiboApi20.instance(), config);
        OAuth2AccessToken token = new OAuth2AccessToken("test_token");
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api", null);

        service.signRequest(token, request);

        String completeUrl = request.getCompleteUrl();
        assertTrue(completeUrl.contains("access_token=test_token") || completeUrl.contains("test_token"),
                "Expected access_token in URL but got: " + completeUrl);
    }
}
