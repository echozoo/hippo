package org.hippo.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by sungang on 2017/3/31.
 */
public class EncryptionHelper {
  /**
   * 加密密码
   */
  public static String encryptPasswordByBCrypt(String password) {
    return new BCryptPasswordEncoder().encode(password);
  }


  public static void main(String[] args) {
    String encrypt_pwd = EncryptionHelper.encryptPasswordByBCrypt("111111");
    System.out.println(encrypt_pwd);
  }
}
