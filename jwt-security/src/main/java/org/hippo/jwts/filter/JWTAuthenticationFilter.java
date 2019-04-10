package org.hippo.jwts.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.hippo.jwts.config.jwt.JWtTokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-04-10
 * @since JDK1.8
 * 需要认证的接口认证
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

  @Autowired
  private JWtTokenAuthenticationService jWtTokenAuthenticationService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    Authentication authentication = jWtTokenAuthenticationService.getAuthentication((HttpServletRequest) request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }
}
