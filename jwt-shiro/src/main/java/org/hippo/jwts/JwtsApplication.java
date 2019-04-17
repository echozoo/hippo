package org.hippo.jwts;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "org.hippo")
@EnableSwagger2Doc
public class JwtsApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtsApplication.class, args);
  }

}
