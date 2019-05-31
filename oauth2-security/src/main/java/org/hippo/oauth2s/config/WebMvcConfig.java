package org.hippo.oauth2s.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dellll
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }


}
