package io.hippo.third.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hippo.third.config.oauth2.SignInHelper;
import java.util.Collections;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-18
 * @since JDK1.8
 */
@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final SignInHelper signInHelper;

  @Autowired public AuthSuccessHandler(SignInHelper signInHelper) {this.signInHelper = signInHelper;}

  @Override public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
    OAuth2AccessToken token = signInHelper.signIn("role", authentication.getPrincipal(), authentication.getAuthorities(), new HashSet(Collections.singleton("read")));
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpStatus.OK.value());
    response.getWriter().write(new ObjectMapper().writeValueAsString(token));
  }
}