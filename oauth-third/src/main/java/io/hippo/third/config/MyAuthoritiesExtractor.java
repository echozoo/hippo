package io.hippo.third.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-06-23
 * @since JDK1.8
 * 自定义权限
 */
@Component
public class MyAuthoritiesExtractor implements AuthoritiesExtractor {
  @Override public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
    List<GrantedAuthority> list = new ArrayList<>();
    list.add(new SimpleGrantedAuthority("ROLE_USER"));
    return list;
  }
}
