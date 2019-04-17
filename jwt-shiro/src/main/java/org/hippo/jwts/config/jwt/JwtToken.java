package org.hippo.jwts.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-04-13
 * @since JDK1.8
 */
public class JwtToken implements AuthenticationToken {

  private String token;

  public JwtToken(String token) {
    this.token = token;
  }

  @Override public Object getPrincipal() {
    return token;
  }

  @Override public Object getCredentials() {
    return token;
  }
}
