package io.hippo.third.config.sms;

import java.util.Collection;
import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-17
 * @since JDK1.8
 * 短信验证token
 */
@Data
class SmsToken extends AbstractAuthenticationToken {

  private Object principal;

  private Object credentials;

  private Collection<GrantedAuthority> authorities;

  public SmsToken(Object mobile, Object code, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    principal = mobile;
    credentials = code;
  }

  @Override public Object getCredentials() {
    return credentials;
  }

  @Override public Object getPrincipal() {
    return principal;
  }
}
