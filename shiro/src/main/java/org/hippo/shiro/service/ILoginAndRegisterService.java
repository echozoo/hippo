package org.hippo.shiro.service;


import org.hippo.shiro.param.UserParams;

/**
 * @author dujf
 */
public interface ILoginAndRegisterService {

  Boolean login(UserParams params);
}
