package io.hippo.third.service;

import com.baomidou.mybatisplus.core.conditions.query.EmptyWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.hippo.third.params.UserParams;
import org.hippo.common.mapper.ClientsMapper;
import org.hippo.common.po.Clients;
import org.hippo.common.util.EncryptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
@Service
public class ClientsServiceImpl extends ServiceImpl<ClientsMapper, Clients> implements IClientsService {

  @Autowired
  private ClientsMapper clientsMapper;

  @Override public void updateClients() {
    clientsMapper.selectList(new EmptyWrapper<>()).forEach(it -> {
      UserParams userParams = new UserParams();
      userParams.setAccount(it.getClientId());
      clientsMapper.updateById(paramToPo(it));
    });
  }

  private Clients paramToPo(Clients clients) {
    clients.setClientSecret(EncryptionHelper.encryptPasswordByBCrypt("123456"));
    return clients;
  }
}
