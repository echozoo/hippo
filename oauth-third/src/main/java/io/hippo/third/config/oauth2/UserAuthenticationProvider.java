package io.hippo.third.config.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;


/**
 * @author dellll
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

  private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);

  private final UserDetailsServiceImpl userDetailsService;

  @Autowired public UserAuthenticationProvider(UserDetailsServiceImpl userDetailsService) {this.userDetailsService = userDetailsService;}

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    logger.info("-------------------UserAuthenticationProvider------------");
    UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
    // 验证密码
    if (!BCrypt.checkpw(authentication.getCredentials().toString(), userDetails.getPassword())) {
      throw new BadCredentialsException("error password");
    }
    // 验证账号状态-账号删除
    if (!userDetails.isEnabled()) {
      throw new DisabledException(new StringBuffer(authentication.getName()).append(" Account Deleted ").toString());
    }
    // 验证账号状态-账号可不可用
    if (!userDetails.isAccountNonExpired()) {
      throw new AccountExpiredException(new StringBuffer(authentication.getName()).append(" Account UnEnabled").toString());
    }
    // 验证账号状态-账号锁定
    if (!userDetails.isAccountNonLocked()) {
      throw new LockedException(new StringBuffer(authentication.getName()).append(" Account locked ").toString());
    }
    return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities());
  }


  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}