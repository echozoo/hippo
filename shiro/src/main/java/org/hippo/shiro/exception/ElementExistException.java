package org.hippo.shiro.exception;

public class ElementExistException extends Exception {

  private String message;

  @Override public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ElementExistException(String message) {
    this.message = message;
  }
}
