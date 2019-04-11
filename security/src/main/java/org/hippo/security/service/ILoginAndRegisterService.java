package org.hippo.security.service;


import org.hippo.security.param.UserParams;

/**
 * @author dujf
 */
public interface ILoginAndRegisterService {

  Boolean login(UserParams params);
}
