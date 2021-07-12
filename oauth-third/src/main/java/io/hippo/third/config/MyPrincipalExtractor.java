package io.hippo.third.config;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-23
 * @since JDK1.8
 * 存库
 */
@Component
public class MyPrincipalExtractor implements PrincipalExtractor {

  private static final String[] PRINCIPAL_KEYS = new String[]{ "user", "username", "userid", "user_id", "login", "name", "id" };


  @Override public Object extractPrincipal(Map<String, Object> map) {
    logger.info(map.toString());

    String[] var2 = PRINCIPAL_KEYS;
    int var3 = var2.length;
    for (int var4 = 0; var4 < var3; ++var4) {
      String key = var2[var4];
      if (map.containsKey(key)) {
        return map.get(key);
      }
    }
    return null;
  }

  private Logger logger = LoggerFactory.getLogger(MyPrincipalExtractor.class);
}
