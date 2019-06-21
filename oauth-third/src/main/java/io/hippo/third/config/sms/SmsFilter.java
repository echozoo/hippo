package io.hippo.third.config.sms;

import com.google.common.base.VerifyException;
import io.hippo.third.config.oauth2.ClientDetailsServiceImpl;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-20
 * @since JDK1.8
 */

public class SmsFilter extends AbstractAuthenticationProcessingFilter {

  public SmsFilter() {
    super((RequestMatcher) (new AntPathRequestMatcher("/login/sms")));
  }

  @Autowired
  public ClientDetailsServiceImpl clientDetailsServiceImpl;

  @Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    if (!RequestMethod.POST.name().equals(request.getMethod())) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
    String mobile = request.getParameter("mobile").trim();
    String code = request.getParameter("code").trim();
    if (mobile.isEmpty() || code.isEmpty()) {
      throw new VerifyException("mobile or code cant be null");
    }
    SmsToken authRequest = new SmsToken(mobile, code, new ArrayList<SimpleGrantedAuthority>());

    setDetails(request, authRequest);
    return this.getAuthenticationManager().authenticate(authRequest);
  }

  private final void setDetails(HttpServletRequest request, SmsToken authRequest) {
    authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
  }

}
