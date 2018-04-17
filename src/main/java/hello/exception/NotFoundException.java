package hello.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

 private static final long serialVersionUID = -20180416223255L;
 
 String errorMessage;
 
 String errorCode;

 public NotFoundException(String errorMessage, String errorCode) {
  super();
  this.errorMessage = errorMessage;
  this.errorCode = errorCode;
 }

 public String getErrorMessage() {
  return errorMessage;
 }

 public void setErrorMessage(String errorMessage) {
  this.errorMessage = errorMessage;
 }

 public String getErrorCode() {
  return errorCode;
 }

 public void setErrorCode(String errorCode) {
  this.errorCode = errorCode;
 }
 
}
