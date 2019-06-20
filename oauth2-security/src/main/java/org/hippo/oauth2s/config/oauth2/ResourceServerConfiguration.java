package org.hippo.oauth2s.config.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId("API");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        //配置order访问控制，必须认证过后才可以访问
        .antMatchers("/security/user/**").hasAnyAuthority("USER")
        .antMatchers("/security/role/**").hasAnyRole("ADMIN")
        .anyRequest().permitAll()
    ;
  }
}
