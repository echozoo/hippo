package org.hippo.jwts.config.jwt;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-04-15
 * @since JDK1.8
 */
class JWtTokenAuthenticationServiceTest {

  @Autowired
  private JWtTokenAuthenticationService jWtTokenAuthenticationService;

  @Test
  void addAuthentication() {
    String token = jWtTokenAuthenticationService.addAuthentication("lisi");
    System.out.println(token);
  }

  @Test
  void getUserName() {
    String username = jWtTokenAuthenticationService.getUsername("");
    Assert.assertEquals("success", "lisi", username);
    System.out.println(username);
  }
}