package io.hippo.github;

import java.security.Principal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dujf
 */
@SpringBootApplication
@EnableOAuth2Client
@RestController
public class GithubApplication {

  public static void main(String[] args) {
    SpringApplication.run(GithubApplication.class, args);
  }

  //  @Bean
//  SinaFilter sinaFilter() {
//    return new SinaFilter();
//  }
  @GetMapping("me")
  public Principal userINfo(Principal principal) {
    return principal;
  }

}
