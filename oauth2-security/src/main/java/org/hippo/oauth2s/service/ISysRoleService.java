package org.hippo.oauth2s.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.hippo.common.po.SysRole;
import org.hippo.oauth2s.params.RoleParams;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
public interface ISysRoleService extends IService<SysRole> {

  void addRole(List<RoleParams> roleParams);

}
