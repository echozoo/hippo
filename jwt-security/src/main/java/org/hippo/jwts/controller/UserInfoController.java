package org.hippo.jwts.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.hippo.jwts.exception.ElementExistException;
import org.hippo.jwts.po.UserInfo;
import org.hippo.jwts.service.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dellll
 * * hasRole([role])
 * 当前用户是否拥有指定角色。
 * hasAnyRole([role1,role2])
 * 多个角色是一个以逗号进行分隔的字符串。如果当前用户拥有指定角色中的任意一个则返回true。
 * hasAuthority([auth])
 * 等同于hasRole
 * hasAnyAuthority([auth1,auth2])
 * 等同于hasAnyRole
 * Principle
 * 代表当前用户的principle对象
 * authentication
 * 直接从SecurityContext获取的当前Authentication对象
 * permitAll
 * 总是返回true，表示允许所有的
 * denyAll
 * 总是返回false，表示拒绝所有的
 * isAnonymous()
 * 当前用户是否是一个匿名用户
 * isRememberMe()
 * 表示当前用户是否是通过Remember-Me自动登录的
 * isAuthenticated()
 * 表示当前用户是否已经登录认证成功了。
 * isFullyAuthenticated()
 * 如果当前用户既不是一个匿名用户，同时又不是通过Remember-Me自动登录的，则返回true。
 */
@RestController
@RequestMapping("/v1/user")
@Api(description = "用户信息", tags = "1.0 用户信息")
public class UserInfoController {

  private final UserInfoServiceImpl userInfoService;

  @Autowired public UserInfoController(UserInfoServiceImpl userInfoService) {this.userInfoService = userInfoService;}

  @GetMapping
  @ApiOperation("重置全部用户密码为123456")
  public R<List<UserInfo>> putUsers() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("1")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public R<List<UserInfo>> get1() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("2")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','USER')")
  public R<List<UserInfo>> get2() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("3")
  @PreAuthorize("hasAuthority('USER')")
  public R<List<UserInfo>> get3() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("4")
  @PreAuthorize("hasAuthority('ROLE_VIP')")
  public R<List<UserInfo>> get4() {
    return R.ok(userInfoService.list());
  }


}
