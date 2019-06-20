package org.hippo.oauth2s.config.oauth2;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-05-12
 * @since JDK1.8
 */
@Component
public class SignInHelper {

  private final ConsumerTokenServices tokenService;

  private final AuthorizationServerTokenServices authorizationServerTokenServices;

  private final DefaultOAuth2RequestFactory oAuth2RequestFactory;

  private final ClientDetailsServiceImpl clientDetailsService;

  @Autowired public SignInHelper(ConsumerTokenServices tokenService, AuthorizationServerTokenServices authorizationServerTokenServices, DefaultOAuth2RequestFactory oAuth2RequestFactory, ClientDetailsServiceImpl clientDetailsService) {
    this.tokenService = tokenService;
    this.authorizationServerTokenServices = authorizationServerTokenServices;
    this.oAuth2RequestFactory = oAuth2RequestFactory;
    this.clientDetailsService = clientDetailsService;
  }

  public OAuth2AccessToken signIn(String clientId, Object user, Collection<? extends GrantedAuthority> authorities, Set<String> scope) {

    AuthorizationRequest request = new AuthorizationRequest(clientId, scope);
    TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(request, "password");
    ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
    OAuth2Request oAuth2Request = oAuth2RequestFactory.createOAuth2Request(clientDetails, tokenRequest);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        user, "", authorities
    );
    OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authenticationToken);
    return authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
  }

  public OAuth2AccessToken signInTwoStep() {
    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    if (oAuth2Authentication.isAuthenticated()) {
      logout();
      return signIn(oAuth2Authentication.getOAuth2Request().getClientId(), oAuth2Authentication.getUserAuthentication().getName(), oAuth2Authentication.getAuthorities(), oAuth2Authentication.getOAuth2Request().getScope());
    } else {
      throw new BadCredentialsException("oauth2 authentication fail.");
    }
  }

  public Boolean logout() {
    OAuth2AuthenticationDetails token = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    return tokenService.revokeToken(token.getTokenValue());
  }
}
