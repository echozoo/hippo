package org.hippo.jwts.config.security;

import org.hippo.jwts.filter.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserAuthenticationProvider userAuthenticationProvider;


  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public JWTAuthenticationFilter jwtAuthenticationFilter() {
    return new JWTAuthenticationFilter();
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
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/v1/login/**").permitAll()
        .antMatchers("/v1/**").authenticated()
        .anyRequest().permitAll()
        .and()
        // 添加一个过滤器验证其他请求的Token是否合法
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * 添加 UserDetailsService， 实现自定义登录校验,密码加密校验
   */
  @Autowired
  @Override
  protected void configure(AuthenticationManagerBuilder builder) throws Exception {
    builder.authenticationProvider(userAuthenticationProvider)
    //这里配置了userDetailsService会去验证密码，一般在provider中去验证
//        .userDetailsService(userDetailsService)
//        .passwordEncoder(passwordEncoder())
    ;
  }

  /**
   * 密码加密
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
