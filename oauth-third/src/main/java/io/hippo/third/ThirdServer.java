package io.hippo.third;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @author dujf
 */
@EnableOAuth2Client
@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = "io.hippo")
public class ThirdServer {

  public static void main(String[] args) {
    SpringApplication.run(ThirdServer.class, args);
  }

}
