package org.hippo.oauth2s.config.oauth2;

import java.util.UUID;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-20
 * @since JDK1.8
 */
public class UserTokenEnhancer implements TokenEnhancer {
  @Override public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    DefaultOAuth2AccessToken result = (DefaultOAuth2AccessToken) accessToken;
    result.setValue(result.getValue().replace("-", ""));
    result.setRefreshToken(new DefaultOAuth2RefreshToken(UUID.randomUUID().toString().replace("-", "")));
    return result;
  }
}
