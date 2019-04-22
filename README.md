# spring security

## spring boot 整合 security
	
	org.springframework.security.core.userdetails.User
	org.springframework.security.core.userdetails.UserDetails	
  
1 . 实现UserDetailsService的loadUserByUsername方法,作用是从数据库获取用户信息
  ```java
//给自定义认证方式添加加密方式，在userDetailsService将密码交给security去验证，在认证管理中配置密码验证方式
  @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new User(userInfo.getAccount(), userInfo.getPassword(), roles);
  }
```
2 . 实现AuthenticationProvider的authenticate方法根据UserDetails实现类获取用户信息进行用户密码，状态等相关验证

3 . 告诉security认证方式
  ```java
	/**
   * 添加自定义登录到认证security管理
   * 
   */
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //用户认证逻辑
    auth.authenticationProvider(userAuthenticationProvider)
    		//获取用户信息
        .userDetailsService(userDetailsService)
        //密码加密方式
        .passwordEncoder(passwordEncoder());
  }
  ```
4 . 访问资源控制,http.authorizeRequests()方法有多个子节点，每个macher按照他们的声明顺序执行,路径配置顺序有要求 ，**匹配就返回**.
  hasAnyAuthority("USER")需要有USER权限才能访问；
  hasAnyRole("ADMIN")会自动给ADMIN加上**ROLE_**前缀，需要有*ROLE_ADMIN*角色才能访问。
  ```java
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
            .antMatchers("/security/login/**").permitAll()
            .antMatchers("/security/user/**").hasAnyAuthority("USER")
            .antMatchers("/security/role/**").hasAnyRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .rememberMe()
            .key("my-secret")
            .rememberMeCookieName("my-cookie-name")
            .tokenValiditySeconds(24 * 60 * 60)
            .and()
            .formLogin()
            .and()
            .logout()
            .and()
            .httpBasic()
        ;
        // 在 UsernamePasswordAuthenticationFilter 前添加自定义过滤器 BeforeLoginFilter 
        http.addFilterBefore(new BeforeLoginFilter(), UsernamePasswordAuthenticationFilter.class);
      }
  ```
## 整合oauth2
		
oauth2-security区分了客户端和用户。

5 . 实现ClientDetailsService的loadClientByClientId方法，实现客户端认证
	
6 . 配置认证server（@EnableAuthorizationServer）通过继承AuthorizationServerConfigurerAdapter配置认证oauth2自定义客户端和用户认证
```java
//client认证
@Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientDetailsService);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
    		//token存储位置
        .tokenStore(new InMemoryTokenStore())
        //将web security配置的authenticationManager
        .authenticationManager(authenticationManager)
        //刷新token会用到userDetailsService
        .userDetailsService(userDetailsService)
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
  	//允许验证token 接口访问,单点登录会访问这个接口验证token是否有效
    oauthServer.checkTokenAccess("permitAll()");
    //加密方式
    oauthServer.passwordEncoder(passwordEncoder());
    //允许表单认证
    oauthServer.allowFormAuthenticationForClients();
  }
```
7 . 修改security的资源控制,不拦截oauth2资源
```java
@Override protected void configure(HttpSecurity http) throws Exception {
      http
          .authorizeRequests()
          .antMatchers("/oauth/*").permitAll()
          .and().httpBasic()
      ;
    }
    
   /**
      * 在这security中，把AuthenticationManager交给Spring，
      * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
      */
     @Bean
     @Override
     public AuthenticationManager authenticationManagerBean() throws Exception {
       AuthenticationManager manager = super.authenticationManagerBean();
       return manager;
     }
```  
8 . 配置资源server（@EnableResourceServer） 继承ResourceServerConfigurerAdapter配置oauth2资源控制
```java
@Configuration
@EnableResourceServer
class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    //资源id和loadClientByClientId查询到的相匹配
    resources.resourceId("API");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        //必须认证过后才可以访问
        .antMatchers("/security/user/**").hasAnyAuthority("USER")
        .antMatchers("/security/role/**").hasAnyRole("ADMIN")
        .anyRequest().permitAll()
    ;
  }
}
```
```java
/**
 * oauth2 几种获取token方式 client 可用basic 方式传递
 * refresh token: http://localhost:8013/oauth/token?grant_type=refresh_token&refresh_token=3680e51e-fbf4-417a-85d9-6a8205c14c0a&client_id=user&client_secret=123456
 * client: http://localhost:8013/oauth/token?client_id=user&client_secret=123456&scope=read&grant_type=client_credentials
 * password: http://localhost:8013/oauth/token?username=zhangsan&password=123456&grant_type=password&scope=read&client_id=user&client_secret=1234567
 * authorization code: http://localhost:8013/oauth/authorize?response_type=code&client_id=code&redirect_uri=http://localhost:8013/security/login&scope=all
 */
```