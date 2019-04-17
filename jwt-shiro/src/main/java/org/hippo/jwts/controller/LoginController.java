package org.hippo.jwts.controller;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.hippo.jwts.param.UserParams;
import org.hippo.jwts.service.LoginAndRegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    String token = loginAndRegisterService.login(params);
    if (null == token) {
      R.failed("error");
    }
    return R.ok(token);
  }

  @PostMapping(value = "/login/out")
  public R<Boolean> loginOut() {
    SecurityUtils.getSubject().logout();
    return R.ok(true);
  }

  @PostMapping(value = "/register")
  public int register() {
    return 0;
  }

}
