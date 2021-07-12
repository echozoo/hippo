package org.hippo.jwts.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.hippo.jwts.config.shiro.MyShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-04-10
 * @since JDK1.8
 */
@Component
public class JWtTokenAuthenticationService {

  private static final long EXPIRATIONTIME = 432_000_000;     // 5天
  private static final String SECRET = "P@ssw02d";            // JWT密码
  static final String TOKEN_PREFIX = "bearer";        // Token前缀
  static final String HEADER_STRING = "Authorization";// 存放Token的Header Key


  private final MyShiroRealm myShiroRealm;

  @Autowired public JWtTokenAuthenticationService(MyShiroRealm myShiroRealm) {this.myShiroRealm = myShiroRealm;}

  // JWT生成方法
  public String addAuthentication(String userName) {
//    AuthorizationInfo authenticationInfo = myShiroRealm.doGetAuthorizationInfo(subject.getPrincipals());
//    String authorities = authenticationInfo.getStringPermissions().toString();
    // 生成JWT
    String jwtToken = Jwts.builder()
        // 保存权限（角色）
        .claim("username", userName)
//        .claim("authorities", authorities)
        // 用户名写入标题
        .setSubject(userName)
        // 有效期设置
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        // 签名设置
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
    return jwtToken;
  }

  // JWT验证方法
  public AuthenticationToken getAuthentication(HttpServletRequest request) {
    // 从Header中拿到token
    String token = request.getHeader(HEADER_STRING);

    if (token != null) {
      try {
// 解析 Token
        Claims claims = Jwts.parser()
            // 验签
            .setSigningKey(SECRET)
            // 去掉 Bearer
            .parseClaimsJws(token.substring(TOKEN_PREFIX.length()).trim())
            .getBody();
        // 拿用户名
        String user = claims.getSubject();
        // 返回验证令牌
        return user != null ? new UsernamePasswordToken(user, "") : null;
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  /**
   * 获得token中的信息无需secret解密也能获得
   *
   * @return token中包含的用户名
   */
  public String getUsername(HttpServletRequest request) {
    // 从Header中拿到token
    String token = request.getHeader(HEADER_STRING);
    try {
      Claims claims = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token.substring(TOKEN_PREFIX.length()).trim())
          .getBody();
      return claims.getSubject();
    } catch (Exception e) {
      return null;
    }
  }
}
