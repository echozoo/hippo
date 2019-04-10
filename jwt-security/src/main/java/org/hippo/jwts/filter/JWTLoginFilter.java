//package org.hippo.jwts.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.hippo.jwts.config.jwt.JWtTokenAuthenticationService;
//import org.hippo.jwts.param.UserParams;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
///**
// * @author <a href="http://github.com/athc">dujf</a>
// * @date 2019-04-10
// * @since JDK1.8
// */
//public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
//
//  public JWTLoginFilter(String url, AuthenticationManager authManager) {
//    super(new AntPathRequestMatcher(url));
//    setAuthenticationManager(authManager);
//  }
//
//  @Override
//  public Authentication attemptAuthentication(
//      HttpServletRequest req, HttpServletResponse res)
//      throws AuthenticationException, IOException, ServletException {
//
//    // JSON反序列化成 AccountCredentials
//    UserParams creds = new ObjectMapper().readValue(req.getInputStream(), UserParams.class);
//
//    // 返回一个验证令牌
//    return getAuthenticationManager().authenticate(
//        new UsernamePasswordAuthenticationToken(creds, creds.getPassword())
//    );
//  }
//
//  @Override
//  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
//    JWtTokenAuthenticationService.addAuthentication(res, auth.getName());
//  }
//
//
//  @Override
//  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//
//    response.setContentType("application/json");
//    response.setStatus(HttpServletResponse.SC_OK);
////    response.getOutputStream().println(JSONResult.fillResultString(500, "Internal Server Error!!!", JSONObject.NULL));
//  }
//}
