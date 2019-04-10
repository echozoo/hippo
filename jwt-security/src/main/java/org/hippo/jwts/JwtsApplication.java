package org.hippo.jwts;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dujf
 */
@SpringBootApplication
@EnableSwagger2Doc
public class JwtsApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtsApplication.class, args);
  }

}
