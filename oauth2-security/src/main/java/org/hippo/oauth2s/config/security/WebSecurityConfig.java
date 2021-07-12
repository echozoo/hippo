package org.hippo.oauth2s.config.security;

import org.hippo.oauth2s.config.oauth2.UserAuthenticationProvider;
import org.hippo.oauth2s.config.oauth2.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author dellll
 * 开启security注解
 * import javax.annotation.security.RolesAllowed;
 * @EnableGlobalMethodSecurity 可以配置多个参数:
 * prePostEnabled :决定Spring Security的前注解是否可用 [@PreAuthorize,@PostAuthorize,..]
 * secureEnabled : 决定是否Spring Security的保障注解 [@Secured] 是否可用
 * jsr250Enabled ：决定 JSR-250 annotations 注解[@RolesAllowed..] 是否可用
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  private final UserDetailsServiceImpl userDetailsService;

  private final UserAuthenticationProvider userAuthenticationProvider;

  @Autowired @Lazy public WebSecurityConfig(UserAuthenticationProvider userAuthenticationProvider, UserDetailsServiceImpl userDetailsService) {
    this.userAuthenticationProvider = userAuthenticationProvider;
    this.userDetailsService = userDetailsService;
  }

  /**
   * security 拦截路径
   * http.authorizeRequests()方法有多个子节点，每个macher按照他们的声明顺序执行
   * 路径配置顺序有要求 ，匹配就返回
   *
   * @param http
   * @throws Exception
   */
  @Override protected void configure(HttpSecurity http) throws Exception {
    http.requestMatchers()
        .anyRequest()
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**").permitAll()
    ;
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //密码认证
    auth
        .authenticationProvider(userAuthenticationProvider)
        .userDetailsService(userDetailsService)
        //密码验证方式
        .passwordEncoder(new BCryptPasswordEncoder())
    ;
    //sms认证
//    auth
//        .authenticationProvider()
//    ;
  }

  /**
   * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    AuthenticationManager manager = super.authenticationManagerBean();
    return manager;
  }
}
