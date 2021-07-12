package io.hippo.third.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hippo.third.ErrorVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-18
 * @since JDK1.8
 */
@Component
public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    response.setContentType("application/json;charset=UTF-8");
    ErrorVO errorVO = new ErrorVO();
    errorVO.setCode("invalid_grant");
    errorVO.setDescription(e.getLocalizedMessage());
    response.getWriter().write(
        new ObjectMapper().writeValueAsString(errorVO)
    );
  }
}