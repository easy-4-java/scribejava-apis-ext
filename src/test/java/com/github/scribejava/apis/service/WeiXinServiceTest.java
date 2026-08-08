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
class WeiXinServiceTest {

    @Test
    void shouldCreateInstanceSuccessfully() {
        WeiXinService service = new WeiXinService();
        assertNotNull(service);
    }

    @Test
    void shouldReturnLoginUrl() {
        WeiXinService service = new WeiXinService();
        String loginUrl = service.getLoginUrl();
        assertNotNull(loginUrl);
        assertTrue(loginUrl.length() > 0, "Expected non-empty login URL");
    }

    @Test
    void shouldReturnLoginUrlContainingOAuthParams() {
        WeiXinService service = new WeiXinService();
        String loginUrl = service.getLoginUrl();
        assertTrue(loginUrl.contains("response_type=") || loginUrl.contains("client_id="),
                "Expected OAuth parameters in URL but got: " + loginUrl);
    }

    @Test
    void shouldReturnNullFromRequestMethod() {
        WeiXinService service = new WeiXinService();
        OAuth20Service mockOAuthService = mock(OAuth20Service.class);

        Response result = service.request(mockOAuthService, "test_token", "http://example.com");
        assertNull(result);
        verify(mockOAuthService).signRequest(any(OAuth2AccessToken.class), any(OAuthRequest.class));
    }

    @Test
    void shouldGetAccessTokenUsingMockedService() throws Exception {
        WeiXinService service = spy(new WeiXinService());
        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        OAuth2AccessToken mockToken = new OAuth2AccessToken("weixin_token");
        when(mockOAuthService.getAccessToken("test_code")).thenReturn(mockToken);

        Field field = WeiXinService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String accessToken = service.getAccessToken("test_code");
        assertEquals("weixin_token", accessToken);
    }

    @Test
    void shouldGetUserIdFromResponse() throws Exception {
        WeiXinService service = spy(new WeiXinService());
        Response mockResponse = mock(Response.class);
        when(mockResponse.getBody()).thenReturn("{\"uid\":\"wx_uid_789\"}");
        doReturn(mockResponse).when(service).request(any(OAuth20Service.class), anyString(), anyString());

        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        Field field = WeiXinService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String uid = service.getUserid("test_access_token");
        assertEquals("wx_uid_789", uid);
    }

    @Test
    void shouldGetUserInfoFromResponse() throws Exception {
        WeiXinService service = spy(new WeiXinService());
        Response mockResponse = mock(Response.class);
        when(mockResponse.getBody()).thenReturn("{\"screen_name\":\"WXUser\",\"profile_image_url\":\"http://example.com/wx.jpg\"}");
        doReturn(mockResponse).when(service).request(any(OAuth20Service.class), anyString(), anyString());

        OAuth20Service mockOAuthService = mock(OAuth20Service.class);
        Field field = WeiXinService.class.getDeclaredField("oauthService");
        field.setAccessible(true);
        field.set(service, mockOAuthService);

        String userInfo = service.getUserInfo("test_access_token", "wx_uid_789");
        assertNotNull(userInfo);
        assertTrue(userInfo.contains("WXUser"));
    }
}
