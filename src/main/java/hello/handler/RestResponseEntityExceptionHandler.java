package hello.handler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import hello.error.ApiError;
import hello.exception.ForbiddenException;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity handleResourceException(ForbiddenException e) {
		return ResponseEntity.status(e.getCode()).body(e.getMessage());
	}
	
	/**
	 * ESSE FUNCIONA!
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.info("\n\tmetodo handleNoHandlerFoundException");
		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		log.info("\n"+error);

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Not foooooooooooooooound!!!", error);
		log.info("\n\tFIM metodo handleNoHandlerFoundException");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}
	
	@ExceptionHandler(RequestRejectedException.class)
	protected ResponseEntity handleRequestRejectedException(RequestRejectedException ex) {
		log.info("\n\tmetodo handleRequestRejectedException");
		log.info("\n"+ex.getLocalizedMessage());
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
				"Unexpected error", "");
		log.info("\n\tFIMmetodo handleRequestRejectedException");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
		
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, 
			HttpHeaders headers, 
			HttpStatus status, 
			WebRequest request) {
		log.info("\n\tmetodo handleHttpRequestMethodNotSupported");
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
		log.info("\n"+builder.toString());

		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, 
				"Method not allowed", builder.toString());
		log.info("\n\tFIM metodo handleHttpRequestMethodNotSupported");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}
	
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.info("\n\tmetodo handleHttpMediaTypeNotSupported");
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMessage());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
		log.info("\n"+builder.toString());

		ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
				"Unsupported Media Type", builder.substring(0, builder.length() - 2));
		log.info("\n\tFIM metodo handleHttpMediaTypeNotSupported");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(
			AccessDeniedException ex
			,	HttpHeaders headers
			,	HttpStatus status
			,	WebRequest request
			) {
		log.info("\n\tmetodo handleAccessDeniedException");
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMessage());
		builder.append(" access denied ");
		log.info("\n"+builder.toString());

		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN , 
				"Forbidden", builder.substring(0, builder.length() - 2));
		log.info("\n\tFIM metodo handleAccessDeniedException");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		log.info("\n\tmetodo handleAll");
		ApiError apiError = new ApiError(
				HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error", "error occurred");
		log.info("\n\tFIM metodo handleAll");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex, HttpHeaders headers, 
			HttpStatus status, WebRequest request) {
		log.info("\n\tmetodo handleMissingServletRequestParameter");
		String error = ex.getParameterName() + " parameter is missing";

		ApiError apiError = 
				new ApiError(HttpStatus.BAD_REQUEST, "Bad request", error);
		log.info("\n\tFIMmetodo handleMissingServletRequestParameter");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(
			ConstraintViolationException ex, WebRequest request) {
		log.info("\n\tmetodo handleConstraintViolation");
		List<String> errors = new ArrayList<String>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + 
					violation.getPropertyPath() + ": " + violation.getMessage());
		}

		ApiError apiError = 
				new ApiError(HttpStatus.BAD_REQUEST, "Bad request - v2", errors);
		log.info("\n\tFIM metodo handleConstraintViolation");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, WebRequest request) {
		log.info("\n\tmetodo handleMethodArgumentTypeMismatch");
		String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		ApiError apiError = 
				new ApiError(HttpStatus.BAD_REQUEST, "Bad req - v3", error);
		log.info("\n\tFIM metodo handleMethodArgumentTypeMismatch");
		return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
	}
	
//	public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
//		HttpHeaders headers = new HttpHeaders();
//		if (ex instanceof HttpRequestMethodNotSupportedException) {
//			HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
//			return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, headers, status, request);
			
//		}
//		else if (ex instanceof HttpMediaTypeNotSupportedException) {
//			HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
//			return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, headers, status, request);
			
//			ApiError apiError = 
//					new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupp. media type", "" );
//			return new ResponseEntity<Object>(apiError.toString(), apiError.getStatus());
//		}
//		else if (ex instanceof HttpMediaTypeNotAcceptableException) {
//			HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
//			return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, headers, status, request);
//		}
//		else if (ex instanceof MissingPathVariableException) {
//			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//			return handleMissingPathVariable((MissingPathVariableException) ex, headers, status, request);
//		}
//		else if (ex instanceof MissingServletRequestParameterException) {
//			HttpStatus status = HttpStatus.BAD_REQUEST;
//			return handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, headers, status, request);
//		}
//		else if (ex instanceof ServletRequestBindingException) {
//			HttpStatus status = HttpStatus.BAD_REQUEST;
//			return handleServletRequestBindingException((ServletRequestBindingException) ex, headers, status, request);
//		}
//		else if (ex instanceof ConversionNotSupportedException) {
//			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//			return handleConversionNotSupported((ConversionNotSupportedException) ex, headers, status, request);
//		}
//		else if (ex instanceof TypeMismatchException) {
//			HttpStatus status = HttpStatus.BAD_REQUEST;
//			return handleTypeMismatch((TypeMismatchException) ex, headers, status, request);
//		}
//		else if (ex instanceof HttpMessageNotReadableException) {
//			HttpStatus status = HttpStatus.BAD_REQUEST;
//			return handleHttpMessageNotReadable((HttpMessageNotReadableException) ex, headers, status, request);
//		}
//		else if (ex instanceof HttpMessageNotWritableException) {
//			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//			return handleHttpMessageNotWritable((HttpMessageNotWritableException) ex, headers, status, request);
//		}
//		else if (ex instanceof MethodArgumentNotValidException) {
//			HttpStatus status = HttpStatus.BAD_REQUEST;
//			return handleMethodArgumentNotValid((MethodArgumentNotValidException) ex, headers, status, request);
//		}
//		else if (ex instanceof MissingServletRequestPartException) {
//			HttpStatus status = HttpStatus.BAD_REQUEST;
//			return handleMissingServletRequestPart((MissingServletRequestPartException) ex, headers, status, request);
//		}
//		else if (ex instanceof BindException) {
//			HttpStatus status = HttpStatus.BAD_REQUEST;
//			return handleBindException((BindException) ex, headers, status, request);
//		}
//		else if (ex instanceof NoHandlerFoundException) {
//			HttpStatus status = HttpStatus.NOT_FOUND;
//			return handleNoHandlerFoundException((NoHandlerFoundException) ex, headers, status, request);
//		}
//		else if (ex instanceof AsyncRequestTimeoutException) {
//			HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
//			return handleAsyncRequestTimeoutException(
//					(AsyncRequestTimeoutException) ex, headers, status, request);
//		}
//		else {
//			if (logger.isWarnEnabled()) {
//				logger.warn("Unknown exception type: " + ex.getClass().getName());
//			}
//			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//			return handleExceptionInternal(ex, null, headers, status, request);
//		}
//	}
}