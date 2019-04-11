package org.hippo.security.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import org.hippo.security.config.security.UserDetailsServiceImpl;
import org.hippo.security.param.UserParams;
import org.hippo.security.service.LoginAndRegisterServiceImpl;
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

  private final LoginAndRegisterServiceImpl loginAndRegisterService;

  @Autowired public LoginController(LoginAndRegisterServiceImpl loginAndRegisterService) {
    this.loginAndRegisterService = loginAndRegisterService;
  }


  @PostMapping(value = "/login/in")
  public R loginIn(@RequestBody UserParams params) {
    return R.ok(loginAndRegisterService.login(params));
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
