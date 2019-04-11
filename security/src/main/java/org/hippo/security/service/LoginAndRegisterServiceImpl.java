package org.hippo.security.service;

import org.hippo.security.config.security.UserDetailsServiceImpl;
import org.hippo.security.param.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author dujf
 */
@Service
public class LoginAndRegisterServiceImpl implements ILoginAndRegisterService {

  private final AuthenticationManager authenticationManager;

  private final UserDetailsServiceImpl userDetailsService;


  @Autowired public LoginAndRegisterServiceImpl(UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager) {
    this.userDetailsService = userDetailsService;
    this.authenticationManager = authenticationManager;
  }

  @Override public Boolean login(UserParams params) {
    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(params.getAccount(), params.getPassword());
    final Authentication authentication = authenticationManager.authenticate(upToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    final UserDetails userDetails = userDetailsService.loadUserByUsername(params.getAccount());
    return true;
  }
}
