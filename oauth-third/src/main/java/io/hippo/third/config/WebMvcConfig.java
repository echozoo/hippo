package io.hippo.third.config;

import io.hippo.third.handler.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dellll
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private LoginHandlerInterceptor loginHandlerInterceptor;

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 添加拦截器
    registry
        .addInterceptor(loginHandlerInterceptor)
        .addPathPatterns("/**")
    ;
  }
}
