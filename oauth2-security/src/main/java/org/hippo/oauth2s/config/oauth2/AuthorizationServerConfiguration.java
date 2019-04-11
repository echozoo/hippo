package org.hippo.oauth2s.config.oauth2;

import org.hippo.oauth2s.config.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @author dujf
 */

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  RedisConnectionFactory redisConnectionFactory;

  @Autowired
  private ClientDetailsServiceImpl clientDetailsService;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientDetailsService);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
//          .tokenStore(new RedisTokenStore(redisConnectionFactory))
        .authenticationManager(authenticationManager)
        //刷新token会用到userDetailsService
        .userDetailsService(userDetailsService)
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    //允许表单认证
    oauthServer.passwordEncoder(passwordEncoder());
    oauthServer.allowFormAuthenticationForClients();
  }

  /**
   * 密码加密
   * <p>
   * }
   */

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
