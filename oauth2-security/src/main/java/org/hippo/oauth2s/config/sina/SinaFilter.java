package org.hippo.oauth2s.config.sina;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hippo.oauth2s.service.SinaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-03
 * @since JDK1.8
 */
@Component
public class SinaFilter extends OncePerRequestFilter {

  private final SinaServiceImpl sinaService;

  @Autowired public SinaFilter(SinaServiceImpl sinaService) {this.sinaService = sinaService;}

  @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //过滤路径
    OrRequestMatcher filterPath = new OrRequestMatcher(
        new AntPathRequestMatcher("/sina/login/in", "GET")
    );
    if (filterPath.matches(request)) {
      String code = request.getParameter("code");
      DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) sinaService.getSinaUserInfo(sinaService.getSinaToken(code));
      System.out.println(token.toString());
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
