package io.hippo.third.config.sms;

import io.hippo.third.config.oauth2.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-17
 * @since JDK1.8
 * 短信验证逻辑
 */
@Component
public class SmsProvider implements AuthenticationProvider {

  private final UserDetailsServiceImpl userDetailsService;

  @Autowired public SmsProvider(UserDetailsServiceImpl userDetailsService) {this.userDetailsService = userDetailsService;}

  @Override public Authentication authenticate(Authentication authentication) {
    SmsToken token = (SmsToken) authentication;
    String mobile = (String) token.getPrincipal();
    String code = (String) token.getCredentials();
    UserDetails user = userDetailsService.loadUserByUsername(mobile);
    logger.info(code);
    //fixme: 验证code
    if (code != code) {
      throw new CredentialsExpiredException("$code expired.");
    }
    //用户信息
    return new SmsToken(user, null, user.getAuthorities());
  }

  @Override public boolean supports(Class<?> authentication) {
    return SmsToken.class.isAssignableFrom(authentication);
  }

  private Logger logger = LoggerFactory.getLogger(SmsProvider.class);
}