package org.hippo.oauth2s.config.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author dujf
 */

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  final
  AuthenticationManager authenticationManager;

  final
  RedisConnectionFactory redisConnectionFactory;

  private final ClientDetailsServiceImpl clientDetailsService;

  private final UserDetailsServiceImpl userDetailsService;

  @Autowired public AuthorizationServerConfiguration(AuthenticationManager authenticationManager, RedisConnectionFactory redisConnectionFactory, ClientDetailsServiceImpl clientDetailsService, UserDetailsServiceImpl userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.redisConnectionFactory = redisConnectionFactory;
    this.clientDetailsService = clientDetailsService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientDetailsService);
//    clients.inMemory()
//        .withClient("abc")
//        .resourceIds("API")
//        .authorizedGrantTypes("password", "refresh_token")
//        .scopes("select")
//        .authorities("client")
//        .secret("123456")
    ;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
//        .tokenStore(new RedisTokenStore(redisConnectionFactory))
        .tokenStore(new InMemoryTokenStore())
        .authenticationManager(authenticationManager)
        //使用自定义token
        .tokenEnhancer(tokenEnhancer())
        //刷新token会用到userDetailsService
        .userDetailsService(userDetailsService)
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
  }

  /**
   * client 验证方式
   *
   * @param oauthServer
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    //配置client secret 验证方式
    oauthServer.passwordEncoder(NoOpPasswordEncoder.getInstance());
    //允许表单认证
    oauthServer.allowFormAuthenticationForClients();
  }

  @Bean
  public DefaultOAuth2RequestFactory oAuth2RequestFactory() {
    return new DefaultOAuth2RequestFactory(clientDetailsService);
  }

  @Bean
  TokenEnhancer tokenEnhancer() {
    return new UserTokenEnhancer();
  }
//  /**
//   * @Bean 方式配置 密码加密
//   *用户名密码验证和客户端密码验证都会使用配置的方式
//   * <p>
//   * }
//   */
//  @Bean
//  PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
}
