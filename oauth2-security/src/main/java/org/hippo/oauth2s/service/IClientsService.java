package org.hippo.oauth2s.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hippo.common.po.Clients;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
public interface IClientsService extends IService<Clients> {

  void updateClients();
}
