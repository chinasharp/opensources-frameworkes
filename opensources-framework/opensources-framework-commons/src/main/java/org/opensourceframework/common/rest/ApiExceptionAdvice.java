package org.opensourceframework.common.rest;

import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.base.exception.ExceptionCode;
import org.opensourceframework.base.exception.SystemException;
import org.opensourceframework.base.exception.ValidateException;
import org.opensourceframework.base.rest.ApiResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.Iterator;
import java.util.List;

/**
 * 异常处理包装类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ControllerAdvice(annotations = {RestController.class , DubboService.class})
public class ApiExceptionAdvice extends BaseResource{
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionAdvice.class);

	public ApiExceptionAdvice() {
	}

	@ExceptionHandler({ResourceException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiResult handleResourceException(ResourceException ex) {
		LOGGER.error(ex.getMessage(), ex);
		return this.makeResult(ex.getCode(), ex.getMessage(), null);
	}

	@ExceptionHandler({ValidateException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResult handleValidateException(ValidateException ex) {
		LOGGER.error("数据验证失败：{}", ex.getMessage());
		return this.makeResult(ex.getCode(), ex.getMessage(), ex.getErrors());
	}

	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResult handleBindExceptionException(BindException ex) {
		return this.extractMsg(ex.getBindingResult());
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResult handleMethodArgumentException(MethodArgumentNotValidException ex) {
		return this.extractMsg(ex.getBindingResult());
	}

	@ExceptionHandler({ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResult handleValidateException(ConstraintViolationException ex) {
		ValidateException vex = new ValidateException(ex);
		LOGGER.debug("数据验证失败：{}", vex.getMessage());
		return this.makeResult(vex.getCode(), vex.getMessage(), vex.getErrors());
	}

	@ExceptionHandler({HttpMessageConversionException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResult handleHttpMessageConversionException(HttpMessageConversionException ex) {
		LOGGER.error(ex.getMessage(), ex);
		String msg = ex.getMessage();
		return this.makeResult(ExceptionCode.INVALID_PARAM.getCode(), msg, null);
	}

	@ExceptionHandler({TypeMismatchException.class, ServletRequestBindingException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResult handleTypeMismatchException(Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
		String msg;
		if (ex instanceof TypeMismatchException) {
			TypeMismatchException ex2 = (TypeMismatchException)ex;
			msg = String.format("类型不匹配{%s},required：{%s}", ex2.getValue(), ex2.getRequiredType().getName());
		} else {
			msg = ex.getMessage();
		}

		return this.makeResult(ExceptionCode.INVALID_PARAM, msg);
	}

	@ExceptionHandler({ApiSignatureException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResult handleApiSignatureException(ApiSignatureException ex) {
		LOGGER.error(ex.getMessage(), ex);
		return this.makeResult(CommonCanstant.ERROR_CODE, ex.getMessage(), null);
	}

	@ExceptionHandler({BizException.class})
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ApiResult<Object> handleBizException(BizException ex) {
		LOGGER.error(ex.getMessage(), ex);
		return this.makeResult(ex.getCode(), ex.getMessage() == null ? "业务处理异常" : ex.getMessage(), null);
	}

	@ExceptionHandler({SystemException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiResult handleDeelonException(SystemException ex) {
		LOGGER.error(ex.getMessage(), ex);
		return this.makeResult(ex.getCode(), ex.getMessage(), null);
	}

	@ExceptionHandler({AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ApiResult handleccessDeniedException(AccessDeniedException ex) {
		LOGGER.error(ex.getMessage(), ex);
		return StringUtils.isEmpty(ex.getMessage()) ? this.fail() : this.fail(ex.getMessage());
	}

	@ExceptionHandler({Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiResult handleAllException(Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
		return StringUtils.isEmpty(ex.getMessage()) ? this.fail() : this.fail(ex.getMessage());
	}

	private ApiResult extractMsg(BindingResult bindingResult) {
		ValidateException vex = new ValidateException();
		if (bindingResult.hasFieldErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			Iterator iterator = errors.iterator();

			while(iterator.hasNext()) {
				FieldError err = (FieldError)iterator.next();
				vex.addError(err.getField(), err.getDefaultMessage());
			}
		}

		LOGGER.debug("数据验证失败：{}", vex.getMessage());
		return this.makeResult(vex.getCode(), vex.getMessage(), vex.getErrors());
	}
}
