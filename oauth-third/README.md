# 短信验证登录

项目地址: https://github.com/athc/hippo/tree/master/oauth-third

##1 拦截器拦截短信验证登录

  继承认证拦截器AbstractAuthenticationProcessingFilter，重写认证方法
  获取到手机号验证码，组装认证认证的Token类，提交认证
 流程大致为：
 
 1 拦截短信认证登录，提交短信认证
 
 2 短信认证逻辑处理，认证成功，提交认证信息
 
 3 生成 token
  
 ```java
@Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
  //短信验证预处理  
  if (!RequestMethod.POST.name().equals(request.getMethod())) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
    String mobile = request.getParameter("mobile").trim();
    String code = request.getParameter("code").trim();
    if (mobile.isEmpty() || code.isEmpty()) {
      throw new VerifyException("mobile or code cant be null");
    }
    
    //组装认证参数
    SmsToken authRequest = new SmsToken(mobile, code, new ArrayList<SimpleGrantedAuthority>());
    setDetails(request, authRequest);
    //提交authenticationManager 认证
   
    return this.getAuthenticationManager().authenticate(authRequest);
  }
```
##  security  配置短信认证 逻辑处理provider
  ```java
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
```
## 类似 用户名密码认证的短信认证 provider
 ```java
 @Override public Authentication authenticate(Authentication authentication) {
  //短信验证码逻辑处理
    SmsToken token = (SmsToken) authentication;
    String mobile = (String) token.getPrincipal();
    String code = (String) token.getCredentials();
    UserDetails user = userDetailsService.loadUserByUsername(mobile);
    logger.info(code);
    //fixme: 验证code
    if (code != code) {
      throw new CredentialsExpiredException("$code expired.");
    }
    //返回认证完成Token
    return new SmsToken(user, null, user.getAuthorities());
  }

//支持自定义Token
  @Override public boolean supports(Class<?> authentication) {
    return SmsToken.class.isAssignableFrom(authentication);
  }
```
 

# 三方认证登录

## 参数注入到类
```java
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
```
## 三方登录请求拦截

```java
 private Filter ssoFilter() {
    CompositeFilter filter = new CompositeFilter();
    List<Filter> filters = new ArrayList<>();
    OAuth2ClientAuthenticationProcessingFilter sinaFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/sina");
    OAuth2RestTemplate sinaTemplate = new OAuth2RestTemplate(sina(), oauth2ClientContext);
    sinaFilter.setRestTemplate(sinaTemplate);
    //自定义userInfo 获取
    SinaUserInfoTokenServices sinaTokenServices = new SinaUserInfoTokenServices(sinaResource().getUserInfoUri(), sina().getClientId());
    sinaTokenServices.setRestTemplate(sinaTemplate);
    sinaFilter.setTokenServices(sinaTokenServices);
    //认证成功处理
    sinaFilter.setAuthenticationSuccessHandler(authSuccessHandler);
    //获取到三方信息 自定义处理 存库等
    sinaTokenServices.setAuthoritiesExtractor(new MyAuthoritiesExtractor());
    //三方登录权限处理
    sinaTokenServices.setPrincipalExtractor(new MyPrincipalExtractor());
    filters.add(sinaFilter);
    filter.setFilters(filters);
    return filter;
  }
```

## 将拦截器放到拦截链中


自定义token：
```java
实现自定义token产生
public class UserTokenEnhancer implements TokenEnhancer {
  @Override public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    DefaultOAuth2AccessToken result = (DefaultOAuth2AccessToken) accessToken;
    //uuid 去掉 	`-`
    result.setValue(result.getValue().replace("-", ""));
    result.setRefreshToken(new DefaultOAuth2RefreshToken(UUID.randomUUID().toString().replace("-", "")));
    //todo： 这里可以自定义token数据结构
    return result;
  }
}
```
从获取的三方信息中 获取有用的信息 

MyPrincipalExtractor implements PrincipalExtractor

三方认证登录获取的权限

MyAuthoritiesExtractor implements AuthoritiesExtractor 

GitHub三方认证登录项目地址：https://github.com/athc/hippo/tree/master/github

参考链接：

https://spring.io/guides/tutorials/spring-boot-oauth2/