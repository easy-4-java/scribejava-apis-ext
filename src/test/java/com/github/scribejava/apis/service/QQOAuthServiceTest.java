package com.github.scribejava.apis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import com.alibaba.fastjson.JSONObject;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QQOAuthServiceTest {

    @Test
    void shouldCreateInstanceSuccessfully() {
        QQOAuthService service = new QQOAuthService();
        assertNotNull(service);
    }

    @Test
    void shouldReturnLoginUrlStartingWithQQAuthorize() {
        QQOAuthService service = new QQOAuthService();
        String loginUrl = service.getLoginUrl();
        assertNotNull(loginUrl);
        assertTrue(loginUrl.startsWith("https://graph.qq.com/oauth2.0/authorize"),
                "Expected URL starting with QQ authorize endpoint but got: " + loginUrl);
    }

    @Test
    void shouldReturnLoginUrlContainingClientId() {
        QQOAuthService service = new QQOAuthService();
        String loginUrl = service.getLoginUrl();
        assertTrue(loginUrl.contains("client_id="),
                "Expected client_id parameter in URL but got: " + loginUrl);
    }

    @Test
    void shouldReturnNullFromRequestMethod() {
        QQOAuthService service = new QQOAuthService();
        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        com.github.scribejava.core.builder.api.DefaultApi20 mockApi = mock(com.github.scribejava.core.builder.api.DefaultApi20.class);
        when(mockOAuthService.getApi()).thenReturn(mockApi);
        when(mockApi.getAccessTokenVerb()).thenReturn(com.github.scribejava.core.model.Verb.GET);

        Response result = service.request(mockOAuthService, "test_token", "http://example.com");
        assertNull(result);
        verify(mockOAuthService).signRequest(any(OAuth2AccessToken.class), any(OAuthRequest.class));
    }

    @Test
    void shouldGetAccessTokenUsingMockedService() throws Exception {
        QQOAuthService service = spy(new QQOAuthService());
        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        OAuth2AccessToken mockToken = new OAuth2AccessToken("expected_token");
        when(mockOAuthService.getAccessToken("test_code")).thenReturn(mockToken);

        Field field = QQOAuthService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String accessToken = service.getAccessToken("test_code");
        assertEquals("expected_token", accessToken);
    }

    @Test
    void shouldGetOpenIdFromResponse() throws Exception {
        QQOAuthService service = spy(new QQOAuthService());
        Response mockResponse = mock(Response.class);
        when(mockResponse.getBody()).thenReturn(
                "callback( {\"client_id\":\"101292272\",\"openid\":\"4584E3AAABFC5F052971C278790E9FCF\"} );");
        doReturn(mockResponse).when(service).request(any(OAuth20Service.class), anyString(), anyString());

        // Set a mock oauthService field for the internal call
        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        Field field = QQOAuthService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String openId = service.getOpenId("test_access_token");
        assertEquals("4584E3AAABFC5F052971C278790E9FCF", openId);
    }

    @Test
    void shouldGetUserInfoAsJsonObject() throws Exception {
        QQOAuthService service = spy(new QQOAuthService());
        Response mockResponse = mock(Response.class);
        when(mockResponse.getBody()).thenReturn(
                "{\"nickname\":\"TestUser\",\"figureurl\":\"http://example.com/avatar.jpg\"}");
        doReturn(mockResponse).when(service).request(any(OAuth20Service.class), anyString(), anyString());

        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        Field field = QQOAuthService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        JSONObject userInfo = service.getUserInfo("test_access_token", "test_openid");
        assertNotNull(userInfo);
        assertEquals("TestUser", userInfo.getString("nickname"));
    }
}
