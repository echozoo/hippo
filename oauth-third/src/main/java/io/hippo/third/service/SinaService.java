package io.hippo.third.service;

import io.hippo.third.config.sina.SinaToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-05-30
 * @since JDK1.8
 */
interface SinaService {

  String getSinaCodeUrl();

  SinaToken getSinaToken(String code);

  OAuth2AccessToken getSinaUserInfo(SinaToken token);

}
