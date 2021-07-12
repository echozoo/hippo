package io.hippo.third.config;

import io.hippo.third.config.oauth2.UserAuthenticationProvider;
import io.hippo.third.config.oauth2.UserDetailsServiceImpl;
import io.hippo.third.config.sina.SinaUserInfoTokenServices;
import io.hippo.third.config.sms.SmsFilter;
import io.hippo.third.config.sms.SmsProvider;
import io.hippo.third.handler.AuthFailHandler;
import io.hippo.third.handler.AuthSuccessHandler;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

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

  private final SmsProvider smsProvider;

  private final SmsFilter smsFilter;

  private final AuthFailHandler authFailHandler;

  private final AuthSuccessHandler authSuccessHandler;

  @Autowired public @Lazy WebSecurityConfig(UserAuthenticationProvider userAuthenticationProvider, UserDetailsServiceImpl userDetailsService, SmsProvider smsProvider, SmsFilter smsFilter, AuthFailHandler authFailHandler, AuthSuccessHandler authSuccessHandler) {
    this.userAuthenticationProvider = userAuthenticationProvider;
    this.userDetailsService = userDetailsService;
    this.smsProvider = smsProvider;
    this.smsFilter = smsFilter;
    this.authFailHandler = authFailHandler;
    this.authSuccessHandler = authSuccessHandler;
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
    http
        .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(smsFilter, BasicAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/oauth/**").permitAll()
        .antMatchers("/login/**").permitAll()
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
    auth
        .authenticationProvider(smsProvider)
        .userDetailsService(userDetailsService)
    ;
  }

  /**
   * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
   */
  @Bean("authenticationManager")
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    AuthenticationManager manager = super.authenticationManagerBean();
    return manager;
  }

  /**
   * 短信验证filter注册到Spring容器
   */
  @Bean
  SmsFilter smsFilter() throws Exception {
    SmsFilter filter = new SmsFilter();
    filter.setAuthenticationFailureHandler(authFailHandler);
    filter.setAuthenticationSuccessHandler(authSuccessHandler);
    filter.setAuthenticationManager(authenticationManagerBean());
    return filter;
  }

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   *
   * @return
   */

  @Autowired
  OAuth2ClientContext oauth2ClientContext;

  @Bean
  public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
    FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }

  @Bean
  public FilterRegistrationBean someFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(ssoFilter());
    registration.setOrder(-100);
    return registration;
  }

  @Bean
  @ConfigurationProperties("sina.client")
  public AuthorizationCodeResourceDetails sina() {
    return new AuthorizationCodeResourceDetails();
  }

  @Bean
  @Qualifier("sinaResource")
  @Primary
  @ConfigurationProperties("sina.resource")
  public ResourceServerProperties sinaResource() {
    return new ResourceServerProperties();
  }

  private Filter ssoFilter() {
    CompositeFilter filter = new CompositeFilter();
    List<Filter> filters = new ArrayList<>();
    OAuth2ClientAuthenticationProcessingFilter sinaFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/sina");
    OAuth2RestTemplate sinaTemplate = new OAuth2RestTemplate(sina(), oauth2ClientContext);
    sinaFilter.setRestTemplate(sinaTemplate);
    SinaUserInfoTokenServices sinaTokenServices = new SinaUserInfoTokenServices(sinaResource().getUserInfoUri(), sina().getClientId());
    sinaTokenServices.setRestTemplate(sinaTemplate);
    sinaFilter.setTokenServices(sinaTokenServices);
    sinaFilter.setAuthenticationSuccessHandler(authSuccessHandler);
    //存库
    sinaTokenServices.setAuthoritiesExtractor(new MyAuthoritiesExtractor());
    sinaTokenServices.setPrincipalExtractor(new MyPrincipalExtractor());
    filters.add(sinaFilter);
    filter.setFilters(filters);
    return filter;
  }

}
