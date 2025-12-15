package edu.photo_school.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenValidityMinutes;
    private long refreshTokenValidityMinutes;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getAccessTokenValidityMinutes() {
        return accessTokenValidityMinutes;
    }

    public void setAccessTokenValidityMinutes(long accessTokenValidityMinutes) {
        this.accessTokenValidityMinutes = accessTokenValidityMinutes;
    }

    public long getRefreshTokenValidityMinutes() {
        return refreshTokenValidityMinutes;
    }

    public void setRefreshTokenValidityMinutes(long refreshTokenValidityMinutes) {
        this.refreshTokenValidityMinutes = refreshTokenValidityMinutes;
    }
}
