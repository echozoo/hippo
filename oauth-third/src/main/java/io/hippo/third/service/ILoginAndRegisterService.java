package io.hippo.third.service;

import io.hippo.third.exception.ElementExistException;
import io.hippo.third.params.UserParams;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author dujf
 */
public interface ILoginAndRegisterService {

  /**
   * 登录
   */
  OAuth2AccessToken login(HttpServletRequest request, UserParams userParams) throws HttpRequestMethodNotSupportedException;

  /**
   * 注册
   */
  int register(UserParams userParams) throws ElementExistException;
}
