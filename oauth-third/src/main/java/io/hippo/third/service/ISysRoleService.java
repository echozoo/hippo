package io.hippo.third.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.hippo.third.params.RoleParams;
import java.util.List;
import org.hippo.common.po.SysRole;

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
