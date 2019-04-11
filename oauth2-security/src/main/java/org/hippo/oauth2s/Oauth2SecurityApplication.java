package org.hippo.oauth2s;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dujf
 */
@SpringBootApplication
@EnableSwagger2Doc
public class Oauth2SecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(Oauth2SecurityApplication.class, args);
  }

}
