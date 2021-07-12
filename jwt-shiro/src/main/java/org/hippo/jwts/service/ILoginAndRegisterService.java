package org.hippo.jwts.service;


import org.hippo.jwts.param.UserParams;

/**
 * @author dujf
 */
public interface ILoginAndRegisterService {

  String login(UserParams params);
}
