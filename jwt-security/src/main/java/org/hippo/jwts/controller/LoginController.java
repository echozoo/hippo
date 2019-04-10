package org.hippo.jwts.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import org.hippo.jwts.config.jwt.JWtTokenAuthenticationService;
import org.hippo.jwts.config.security.UserDetailsServiceImpl;
import org.hippo.jwts.param.UserParams;
import org.hippo.jwts.service.LoginAndRegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dellll
 */
@RestController
@RequestMapping("/v1")
@Api(description = "注册与登录", tags = "注册与登录")
public class LoginController {

  private final AuthenticationManager authenticationManager;

  private final UserDetailsServiceImpl userDetailsService;

  private final JWtTokenAuthenticationService jWtTokenAuthenticationService;

  @Autowired public LoginController(UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager, JWtTokenAuthenticationService jWtTokenAuthenticationService) {
    this.userDetailsService = userDetailsService;
    this.authenticationManager = authenticationManager;
    this.jWtTokenAuthenticationService = jWtTokenAuthenticationService;
  }


  @PostMapping(value = "/login/in")
  public R loginIn(@RequestBody UserParams params) {
    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(params.getAccount(), params.getPassword());
    final Authentication authentication = authenticationManager.authenticate(upToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    final UserDetails userDetails = userDetailsService.loadUserByUsername(params.getAccount());
    final String token = jWtTokenAuthenticationService.addAuthentication(userDetails);
    return R.ok(token);
  }

  @PostMapping(value = "/login/out")
  public R<String> loginOut() {
    return new R<>();
  }

  @PostMapping(value = "/register")
  public int register() {
    return 0;
  }

}
