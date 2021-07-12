package org.hippo.jwts.config.shiro;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.hippo.jwts.filter.JWTAuthenticationFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dellll
 */
@Configuration
public class ShiroConfig {


  @Bean
  public ShiroFilterFactoryBean shirFilter(DefaultWebSecurityManager securityManager) {

    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    // 必须设置 SecurityManager
    shiroFilterFactoryBean.setSecurityManager(securityManager);

    //拦截器.
    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
    //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
    // anon:所有url都都可以匿名访问;
    // authc: 需要认证才能进行访问;
    // user:配置记住我或认证通过可以访问；
    // 配置不会被拦截的链接 顺序判断
    filterChainDefinitionMap.put("/swagger/**", "anon");
    filterChainDefinitionMap.put("/v2/api-docs", "anon");
    filterChainDefinitionMap.put("/swagger-ui.html", "anon");
    filterChainDefinitionMap.put("/webjars/**", "anon");
    filterChainDefinitionMap.put("/swagger-resources/**", "anon");
    filterChainDefinitionMap.put("/statics/**", "anon");
    filterChainDefinitionMap.put("/**/login/**", "anon");
    filterChainDefinitionMap.put("/favicon.ico", "anon");
    filterChainDefinitionMap.put("/captcha.jpg", "anon");
//    filterChainDefinitionMap.put("/**", "authc");

    Map<String, Filter> filterMap = new HashMap<>(1);
    filterMap.put("jwt", new JWTAuthenticationFilter());
    shiroFilterFactoryBean.setFilters(filterMap);
    filterChainDefinitionMap.put("/**", "jwt");

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//    shiroFilterFactoryBean.setLoginUrl("/swagger-ui.html");
    // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index.html");
    //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error.html");
    return shiroFilterFactoryBean;
  }


  @Bean
  public DefaultWebSecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    //设置realm.
    securityManager.setRealm(myShiroRealm());
    return securityManager;
  }

  /**
   * 开启shiro aop注解支持.
   * 使用代理方式;所以需要开启代码支持;
   *
   * @return
   */
  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
    return authorizationAttributeSourceAdvisor;
  }

  @Bean
  @ConditionalOnMissingBean
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
    defaultAAP.setProxyTargetClass(true);
    return defaultAAP;
  }


  /**
   * 身份认证realm;
   * (这个需要自己写，账号密码校验；权限等)
   *
   * @return
   */
  @Bean
  public MyShiroRealm myShiroRealm() {
    return new MyShiroRealm();
  }

}