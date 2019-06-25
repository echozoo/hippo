package io.hippo.github;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-23
 * @since JDK1.8
 */
@Configuration
@Order(1)
@EnableAuthorizationServer
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .antMatcher("/**")
        .authorizeRequests()
        .antMatchers("/me", "/login/**", "/webjars/**", "/error**").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
    ;
  }

  @Bean
  @ConfigurationProperties("github.client")
  public AuthorizationCodeResourceDetails github() {
    return new AuthorizationCodeResourceDetails();
  }

  @Bean
  @ConfigurationProperties("github.resource")
  public ResourceServerProperties githubResource() {
    return new ResourceServerProperties();
  }


  @Bean
  @ConfigurationProperties("sina.client")
  public AuthorizationCodeResourceDetails sina() {
    return new AuthorizationCodeResourceDetails();
  }

  @Bean
  @ConfigurationProperties("sina.resource")
  public ResourceServerProperties sinaResource() {
    return new ResourceServerProperties();
  }

  @Autowired
  OAuth2ClientContext oauth2ClientContext;

  @Autowired
  MyPrincipalExtractor myPrincipalExtractor;

  @Autowired
  MyAuthoritiesExtractor myAuthoritiesExtractor;

  private Filter ssoFilter() throws Exception {
    CompositeFilter filter = new CompositeFilter();
    List<Filter> filters = new ArrayList<>();

    OAuth2ClientAuthenticationProcessingFilter githubFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
    OAuth2RestTemplate template = new OAuth2RestTemplate(github(), oauth2ClientContext);
    githubFilter.setRestTemplate(template);
    //    githubFilter.setAuthenticationSuccessHandler(new AuthSuccessHandler());
    UserInfoTokenServices tokenServices = new UserInfoTokenServices(githubResource().getUserInfoUri(), github().getClientId());
    tokenServices.setRestTemplate(template);
    githubFilter.setTokenServices(tokenServices);
    //存库
    tokenServices.setPrincipalExtractor(myPrincipalExtractor);
    //自定义权限
    tokenServices.setAuthoritiesExtractor(myAuthoritiesExtractor);

    OAuth2ClientAuthenticationProcessingFilter sinaFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/sina");
    OAuth2RestTemplate sinaTemplate = new OAuth2RestTemplate(sina(), oauth2ClientContext);
    sinaFilter.setRestTemplate(sinaTemplate);
    sinaFilter.setAuthenticationSuccessHandler(new AuthSuccessHandler());
    SinaUserInfoTokenServices sinaTokenServices = new SinaUserInfoTokenServices(sinaResource().getUserInfoUri(), sina().getClientId());
    sinaTokenServices.setRestTemplate(sinaTemplate);
    sinaFilter.setTokenServices(sinaTokenServices);
    sinaFilter.setAuthenticationManager(authenticationManager());
    //存库
    sinaTokenServices.setPrincipalExtractor(myPrincipalExtractor);
    sinaTokenServices.setAuthoritiesExtractor(myAuthoritiesExtractor);
    filters.add(githubFilter);
    filters.add(sinaFilter);
    filter.setFilters(filters);
    return filter;
  }

  @Bean
  public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
    FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }

  @Bean("authenticationManager")
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    AuthenticationManager manager = super.authenticationManagerBean();
    return manager;
  }
}
