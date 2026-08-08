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

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeiBoServiceTest {

    @Test
    void shouldCreateInstanceSuccessfully() {
        WeiBoService service = new WeiBoService();
        assertNotNull(service);
    }

    @Test
    void shouldReturnLoginUrlStartingWithWeiboAuthorize() {
        WeiBoService service = new WeiBoService();
        String loginUrl = service.getLoginUrl();
        assertNotNull(loginUrl);
        assertTrue(loginUrl.contains("weibo.com"),
                "Expected weibo.com in URL but got: " + loginUrl);
    }

    @Test
    void shouldReturnLoginUrlContainingResponseType() {
        WeiBoService service = new WeiBoService();
        String loginUrl = service.getLoginUrl();
        assertTrue(loginUrl.contains("response_type=") || loginUrl.contains("client_id="),
                "Expected OAuth parameters in URL but got: " + loginUrl);
    }

    @Test
    void shouldReturnNullFromRequestMethod() {
        WeiBoService service = new WeiBoService();
        OAuth20Service mockOAuthService = mock(OAuth20Service.class);

        Response result = service.request(mockOAuthService, "test_token", "http://example.com", Verb.GET);
        assertNull(result);
        verify(mockOAuthService).signRequest(any(OAuth2AccessToken.class), any(OAuthRequest.class));
    }

    @Test
    void shouldGetAccessTokenUsingMockedService() throws Exception {
        WeiBoService service = spy(new WeiBoService());
        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        OAuth2AccessToken mockToken = new OAuth2AccessToken("weibo_token");
        when(mockOAuthService.getAccessToken("test_code")).thenReturn(mockToken);

        Field field = WeiBoService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String accessToken = service.getAccessToken("test_code");
        assertEquals("weibo_token", accessToken);
    }

    @Test
    void shouldGetUserIdFromResponse() throws Exception {
        WeiBoService service = spy(new WeiBoService());
        Response mockResponse = mock(Response.class);
        when(mockResponse.getBody()).thenReturn("{\"uid\":\"1234567890\"}");
        doReturn(mockResponse).when(service).request(any(OAuth20Service.class), anyString(), anyString(), any(Verb.class));

        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        Field field = WeiBoService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String uid = service.getUserid("test_access_token");
        assertEquals("1234567890", uid);
    }

    @Test
    void shouldGetUserInfoFromResponse() throws Exception {
        WeiBoService service = spy(new WeiBoService());
        Response mockResponse = mock(Response.class);
        when(mockResponse.getBody()).thenReturn("{\"screen_name\":\"TestUser\",\"profile_image_url\":\"http://example.com/pic.jpg\"}");
        doReturn(mockResponse).when(service).request(any(OAuth20Service.class), anyString(), anyString(), any(Verb.class));

        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        Field field = WeiBoService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String userInfo = service.getUserInfo("test_access_token", "1234567890");
        assertNotNull(userInfo);
        assertTrue(userInfo.contains("TestUser"));
    }

    @Test
    void shouldShareStatusToWeibo() throws Exception {
        WeiBoService service = spy(new WeiBoService());
        Response mockResponse = mock(Response.class);
        when(mockResponse.getBody()).thenReturn("{\"id\":\"987654321\"}");
        doReturn(mockResponse).when(service).request(any(OAuth20Service.class), anyString(), anyString(), any(Verb.class));

        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        Field field = WeiBoService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String result = service.UserShare("test_access_token", "Hello Weibo!");
        assertNotNull(result);
        assertTrue(result.contains("987654321"));
    }
}
