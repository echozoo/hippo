package org.hippo.jwts;

import io.jsonwebtoken.Jwts;
import org.hippo.jwts.config.jwt.JWtTokenAuthenticationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtsApplicationTests {

  @Autowired
  private JWtTokenAuthenticationService jWtTokenAuthenticationService;

  @Test
  public void addAuthentication() {
    String token = jWtTokenAuthenticationService.addAuthentication("lisi");
    System.out.println(token);
  }

  @Test
  public void getUserName() {
    String username = jWtTokenAuthenticationService.getUsername("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaXNpIiwiZXhwIjoxNTU1NzI5MDUxfQ.GM3vtLwljg_fN3EpNoF3sBezSOudLu7Hj8VzE29ze0USaZD8ez0wxYz1U8M1ElAwV5EEgDPYEQjJObRMfGNqVw");
    Assert.assertEquals("success", "lisi", username);
    System.out.println(username);
  }

  @Test
  public void ts() {
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaXNpIiwiZXhwIjoxNTU1NzI5MDUxfQ.GM3vtLwljg_fN3EpNoF3sBezSOudLu7Hj8VzE29ze0USaZD8ez0wxYz1U8M1ElAwV5EEgDPYEQjJObRMfGNqVw";
    String username = jWtTokenAuthenticationService.getUsername(token);
    String a = Jwts.parser().parse(token).getHeader().get("authorities").toString();
    Assert.assertEquals("success", "lisi", username);
    System.out.println(username);
  }


}
