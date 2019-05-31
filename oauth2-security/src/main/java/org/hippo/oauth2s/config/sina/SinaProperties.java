package org.hippo.oauth2s.config.sina;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-05-29
 * @since JDK1.8
 */


@Data
@Component
@ConfigurationProperties(prefix = "sina")
@PropertySource(value = { "classpath:sina.properties" }, encoding = "utf-8")
public class SinaProperties {

  private String appName;
  private String appKey;
  private String appSecret;
  private String baseUrl;
  private String accessTokenUrl;
  private String authorizeUrl;
  private String rmUrl;
  private String redirectUrl;
  private String userInfoUrl;

}
