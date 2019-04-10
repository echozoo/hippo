package org.hippo.jwts.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
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

  // JWT生成方法
  public String addAuthentication(UserDetails userDetails) {
    String authorities = userDetails.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    // 生成JWT
    String jwtToken = Jwts.builder()
        // 保存权限（角色）
        .claim("authorities", authorities)
        // 用户名写入标题
        .setSubject(userDetails.getUsername())
        // 有效期设置
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        // 签名设置
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
    return jwtToken;
  }

  // JWT验证方法
  public Authentication getAuthentication(HttpServletRequest request) {
    // 从Header中拿到token
    String token = request.getHeader(HEADER_STRING);
    if (token != null) {
      String aa = token.substring(TOKEN_PREFIX.length()).trim();
      // 解析 Token
      Claims claims = Jwts.parser()
          // 验签
          .setSigningKey(SECRET)
          // 去掉 Bearer
          .parseClaimsJws(aa)
          .getBody();
      // 拿用户名
      String user = claims.getSubject();
      // 得到 权限（角色）
      List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
      // 返回验证令牌
      return user != null ? new UsernamePasswordAuthenticationToken(user, null, authorities) : null;
    }
    return null;
  }
}
