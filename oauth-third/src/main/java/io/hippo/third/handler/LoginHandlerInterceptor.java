package io.hippo.third.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-22
 * @since JDK1.8
 */
@Component
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

  @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    logger.info("------------------HandlerInterceptor---------------");
    return true;
  }

  private Logger logger = LoggerFactory.getLogger(LoginHandlerInterceptor.class);
}
