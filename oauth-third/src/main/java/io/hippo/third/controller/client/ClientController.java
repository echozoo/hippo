package io.hippo.third.controller.client;

import com.baomidou.mybatisplus.extension.api.R;
import io.hippo.third.service.ClientsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dujf
 */
@RestController
@RequestMapping("/security/client")
@Api(description = "客户端", tags = "客户端")
public class ClientController {

  @Autowired
  private ClientsServiceImpl clientsService;

  @PutMapping("/all")
  @ApiOperation("重置全部客户端密码为123456")
  public R<Void> putUsers() {
    clientsService.updateClients();
    return R.ok(null);
  }
}
