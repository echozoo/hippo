package org.hippo.oauth2s.config;

import org.hippo.oauth2s.config.sina.SinaConfig;
import org.hippo.oauth2s.config.sina.SinaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-05-31
 * @since JDK1.8
 */

@Configuration
public class AutoConfig {

  @Bean
  SinaConfig sinaConfig(SinaProperties properties) {
    SinaConfig config = new SinaConfig();
    SinaConfig.accessTokenUrl = null != properties.getAccessTokenUrl() ? properties.getAccessTokenUrl() : SinaConfig.accessTokenUrl;
    SinaConfig.appKey = null != properties.getAppKey() ? properties.getAppKey() : SinaConfig.appKey;
    SinaConfig.appName = null != properties.getAppName() ? properties.getAppName() : SinaConfig.appName;
    SinaConfig.appSecret = null != properties.getAppSecret() ? properties.getAppSecret() : SinaConfig.appSecret;
    SinaConfig.authorizeUrl = null != properties.getAuthorizeUrl() ? properties.getAuthorizeUrl() : SinaConfig.authorizeUrl;
    SinaConfig.baseUrl = null != properties.getBaseUrl() ? properties.getBaseUrl() : SinaConfig.baseUrl;
    SinaConfig.redirectUrl = null != properties.getRedirectUrl() ? properties.getRedirectUrl() : SinaConfig.redirectUrl;
    SinaConfig.rmUrl = null != properties.getRmUrl() ? properties.getRmUrl() : SinaConfig.rmUrl;
    SinaConfig.userInfoUrl = null != properties.getUserInfoUrl() ? properties.getUserInfoUrl() : SinaConfig.userInfoUrl;
    return config;
  }

}
