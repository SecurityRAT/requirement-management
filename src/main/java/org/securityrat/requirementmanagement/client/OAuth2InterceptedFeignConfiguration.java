package org.securityrat.requirementmanagement.client;

import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

import org.securityrat.requirementmanagement.security.oauth2.AuthorizationHeaderUtil;

public class OAuth2InterceptedFeignConfiguration {

    @Bean(name = "oauth2RequestInterceptor")
    public RequestInterceptor getOAuth2RequestInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil) {
        return new TokenRelayRequestInterceptor(authorizationHeaderUtil);
    }
}
