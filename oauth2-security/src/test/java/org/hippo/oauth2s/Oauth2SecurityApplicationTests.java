package org.hippo.oauth2s;

import org.hippo.oauth2s.config.sina.SinaProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Oauth2SecurityApplicationTests {

  @Autowired
  private SinaProperties sinaProperties;

  @Test
  public void test() {
    System.out.println(sinaProperties);
  }

}
