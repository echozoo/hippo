package org.hippo.jwts.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.hippo.common.po.UserInfo;
import org.hippo.jwts.config.shiro.MyShiroRealm;
import org.hippo.jwts.service.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dellll
 * //属于user角色
 * RequiresRoles("user") 必须同时属于user和admin角色
 * RequiresRoles({"user","admin"}) RequiresPermissions
 * 属于user或者admin之一;修改logical为OR 即可
 * RequiresRoles(value={"user","admin"},logical=Logical.OR)
 * <p>
 * RequiresPermissions
 * RequiresAuthentication
 * RequiresUser
 * RequiresGuest
 */
@RestController
@RequestMapping("/v1/user")
@Api(description = "用户信息", tags = "1.0 用户信息")
public class UserInfoController {

  private final UserInfoServiceImpl userInfoService;

  private final MyShiroRealm myShiroRealm;


  @Autowired public UserInfoController(UserInfoServiceImpl userInfoService, MyShiroRealm myShiroRealm) {
    this.userInfoService = userInfoService;
    this.myShiroRealm = myShiroRealm;
  }

  @GetMapping
  @ApiOperation("重置全部用户密码为123456")
  public R<List<UserInfo>> putUsers() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("1")
  @RequiresAuthentication
  public R<List<UserInfo>> get1() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("2")
  @RequiresRoles("USER")
  public R<List<UserInfo>> get2() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("3")
  @RequiresPermissions(value = { "userInfo:del" }, logical = Logical.OR)
  public R<List<UserInfo>> get3() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("4")
  @RequiresPermissions("aaac:view")
  public R<List<UserInfo>> get4() {
    return R.ok(userInfoService.list());
  }

  @GetMapping("5")
  public R<AuthorizationInfo> get5() {
    AuthorizationInfo authorizationInfo = myShiroRealm.doGetAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    return R.ok(authorizationInfo);
  }


}
