package org.hippo.jwts.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.hippo.jwts.config.jwt.JWtTokenAuthenticationService;
import org.hippo.jwts.service.SysUserRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-04-17
 * @since JDK1.8
 */
@RestController
@RequestMapping("/v1/user/role-permission")
@Api(description = "用户角色权限", tags = "1.0 用户角色权限")
public class SysUserRoleController {

  private final SysUserRoleServiceImpl sysUserRoleService;

  private final JWtTokenAuthenticationService jWtTokenAuthenticationService;

  @Autowired
  public SysUserRoleController(SysUserRoleServiceImpl sysUserRoleService, JWtTokenAuthenticationService jWtTokenAuthenticationService) {
    this.sysUserRoleService = sysUserRoleService;
    this.jWtTokenAuthenticationService = jWtTokenAuthenticationService;
  }

  @GetMapping("r")
  @ApiOperation("重置全部用户密码为123456")
  public R userRoles(HttpServletRequest request) {
    return R.ok(sysUserRoleService.userRoles(jWtTokenAuthenticationService.getUsername(request)));
  }

  @GetMapping("p")
  @RequiresAuthentication
  public R userPermissions(HttpServletRequest request) {
    return R.ok(sysUserRoleService.userPermissions(jWtTokenAuthenticationService.getUsername(request)));
  }


}
