package com.github.scribejava.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OAuth2ConstantsTest {

    @Test
    void shouldHaveCorrectIdConstant() {
        assertEquals("id", OAuth2Constants.ID);
    }

    @Test
    void shouldHaveCorrectOpenIdConstant() {
        assertEquals("openid", OAuth2Constants.OPENID);
    }

    @Test
    void shouldHaveCorrectOAuthConsumerKeyConstant() {
        assertEquals("oauth_consumer_key", OAuth2Constants.OAUTH_CONSUMER_KEY);
    }

    @Test
    void shouldHaveCorrectGrantTypeConstant() {
        assertEquals("grant_type", OAuth2Constants.GRANT_TYPE);
    }

    @Test
    void shouldHaveCorrectResponseTypeConstant() {
        assertEquals("response_type", OAuth2Constants.RESPONSE_TYPE);
    }
}
