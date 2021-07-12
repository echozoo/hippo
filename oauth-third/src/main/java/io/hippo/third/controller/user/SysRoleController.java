package io.hippo.third.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.hippo.third.params.RoleParams;
import io.hippo.third.service.SysRoleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.hippo.common.po.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2018/12/19
 * @since JDK1.8
 */
@RequestMapping("security/role")
@RestController
@Api(description = "2角色信息", tags = "2角色信息")
public class SysRoleController {

  @Autowired
  private SysRoleServiceImpl sysRoleService;

  @GetMapping()
  public IPage<SysRole> roleList() {
    return sysRoleService.page(new Page<>(0, 10));
  }

  @PostMapping("interim")
  @ApiOperation("临时给ROOT权限")
  public void addInterimRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
    //新加的ROOT权限
    updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_ROOT"));
    Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(newAuth);
  }

  @PostMapping()
  @ApiOperation("添加角色")
  public R<Object> addRole(@RequestBody List<RoleParams> roleParams) {
    sysRoleService.addRole(roleParams);
    return R.ok(null);
  }
}
