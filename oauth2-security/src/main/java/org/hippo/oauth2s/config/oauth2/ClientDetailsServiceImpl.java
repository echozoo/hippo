package org.hippo.oauth2s.config.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hippo.common.po.Clients;
import org.hippo.oauth2s.service.ClientsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * @author dujf
 */
@Configuration
public class ClientDetailsServiceImpl implements ClientDetailsService {

  private final ClientsServiceImpl clientsService;

  @Autowired public ClientDetailsServiceImpl(ClientsServiceImpl clientsService) {this.clientsService = clientsService;}

  @Override public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
    Clients clients = clientsService.getOne(new QueryWrapper<Clients>().lambda().eq(Clients::getClientId, s));
    if (null == clients) {
      throw new ClientRegistrationException(new StringBuffer("clientId: ").append(s).append(" not found").toString());
    }
    return new ClientDetails() {
      @Override public String getClientId() {
        return clients.getClientId();
      }

      @Override public Set<String> getResourceIds() {
        return string2Set(clients.getResourceIds());
      }

      @Override public boolean isSecretRequired() {
        return false;
      }

      @Override public String getClientSecret() {
        return clients.getClientSecret();
      }

      @Override public boolean isScoped() {
        return true;
      }

      @Override public Set<String> getScope() {
        return string2Set(clients.getScope());
      }

      @Override public Set<String> getAuthorizedGrantTypes() {
        return string2Set(clients.getAuthorizedGrantTypes());
      }

      @Override public Set<String> getRegisteredRedirectUri() {
        return string2Set(clients.getWebServerRedirectUri());
      }

      @Override public Collection<GrantedAuthority> getAuthorities() {
        return getAuthes(clients.getAuthorities());
      }

      @Override public Integer getAccessTokenValiditySeconds() {
        return Integer.valueOf(clients.getAccessTokenValidity());
      }

      @Override public Integer getRefreshTokenValiditySeconds() {
        return Integer.valueOf(clients.getRefreshTokenValidity());
      }

      @Override public boolean isAutoApprove(String s) {
        return false;
      }

      @Override public Map<String, Object> getAdditionalInformation() {
        return null;
      }
    };
  }

  private Set<String> string2Set(String str) {
    Set<String> strings = new HashSet<>();
    if (!str.isEmpty()) {
      String[] strs = str.split(COMMA);
      for (int i = 0, j = strs.length; i < j; i++) {
        strings.add(strs[i]);
      }
    }
    return strings;
  }

  private List<GrantedAuthority> getAuthes(String str) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    if (!str.isEmpty()) {
      String[] strs = str.split(COMMA);
      for (int i = 0, j = strs.length; i < j; i++) {
        authorities.add(new SimpleGrantedAuthority(strs[i]));
      }
    }
    return authorities;
  }

  private static String COMMA = ",";
}
