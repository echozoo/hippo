package org.hippo.shiro.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.hippo.shiro.param.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dujf
 */
@Service
public class LoginAndRegisterServiceImpl implements ILoginAndRegisterService {


  @Autowired
  private SecurityManager securityManager;

  @Override public Boolean login(UserParams params) {
    //将SecurityManager创建到生成环境中
    SecurityUtils.setSecurityManager(securityManager);
    //认证提交认证token
    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(params.getAccount(), params.getPassword());
    Subject subject = SecurityUtils.getSubject();
    //执行认证提交认证
    try {
      subject.login(usernamePasswordToken);
    } catch (AuthenticationException ex) {
      ex.printStackTrace();
    }
    return true;
  }
}
