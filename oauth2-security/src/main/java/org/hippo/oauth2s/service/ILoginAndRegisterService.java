package org.hippo.oauth2s.service;

import javax.servlet.http.HttpServletRequest;
import org.hippo.oauth2s.exception.ElementExistException;
import org.hippo.oauth2s.params.UserParams;
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
