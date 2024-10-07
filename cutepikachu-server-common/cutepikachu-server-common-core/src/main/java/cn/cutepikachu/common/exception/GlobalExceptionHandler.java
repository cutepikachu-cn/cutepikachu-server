package cn.cutepikachu.common.exception;

import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

/**
 * 全局异常处理器
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 参考：https://github.com/YunaiV/yudao-cloud/blob/master/yudao-framework/yudao-spring-boot-starter-web/src/main/java/cn/iocoder/yudao/framework/web/core/handler/GlobalExceptionHandler.java

    /**
     * Sa-Token 未登录异常
     */
    @ExceptionHandler({NotLoginException.class})
    public ResponseEntity<?> notLoginExceptionHandler(NotLoginException e) {
        log.error("[NotLoginException] ", e);
        return ResponseUtils.error(ResponseCode.UNAUTHORIZED);
    }

    /**
     * Sa-Token 无权限角色异常
     */
    @ExceptionHandler({NotRoleException.class})
    public ResponseEntity<?> notRoleExceptionHandler(NotRoleException e) {
        log.error("[NotRoleException] ", e);
        return ResponseUtils.error(ResponseCode.FORBIDDEN);
    }

    /**
     * Sa-Token 无权限异常
     */
    @ExceptionHandler({NotPermissionException.class})
    public ResponseEntity<?> notPermissionExceptionHandler(NotPermissionException e) {
        log.error("[NotPermissionException] ", e);
        return ResponseUtils.error(ResponseCode.FORBIDDEN);
    }

    /**
     * 参数传递格式不支持异常
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<?> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException e) {
        log.error("[HttpMediaTypeNotSupportedException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST, "参数格式错误");
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("[MissingServletRequestParameterException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST, "请求参数缺失");
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error("[MethodArgumentTypeMismatchException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST, "请求参数类型错误");
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST, "请求参数不正确");
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> bindExceptionHandler(BindException e) {
        log.error("[BindException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST, "请求参数不正确");
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * <p>
     * 例如说，接口上设置了 @RequestBody 实体中 xx 属性类型为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST, "请求参数类型错误");
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("[ConstraintViolationException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST, "请求参数不正确");
    }

    /**
     * 处理本地参数校验时，抛出的 ValidationException 异常
     */
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> validationExceptionHandler(ValidationException e) {
        log.error("[ValidationException] ", e);
        return ResponseUtils.error(ResponseCode.BAD_REQUEST);
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     * <p>
     * 需要设置如下两个配置项：
     * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
     * 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<?> noHandlerFoundExceptionHandler(NoHandlerFoundException e) {
        log.error("[NoHandlerFoundException] ", e);
        return ResponseUtils.error(ResponseCode.NOT_FOUND, "请求地址不存在");
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     */
    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<?> noResourceFoundExceptionHandler(NoResourceFoundException e) {
        log.error("[NoResourceFoundException] ", e);
        return ResponseUtils.error(ResponseCode.NOT_FOUND, "请求地址不存在");
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     * <p>
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("[HttpRequestMethodNotSupportedException] ", e);
        return ResponseUtils.error(ResponseCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 处理 Spring Security 权限不足的异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedExceptionHandler(AccessDeniedException e) {
        log.error("[AccessDeniedException] ", e);
        return ResponseUtils.error(ResponseCode.FORBIDDEN);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<?> businessExceptionHandler(BusinessException e) {
        log.error("[BusinessException] ", e);
        return ResponseUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理系统异常，兜底一切异常
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> defaultExceptionHandler(Exception e) {
        log.error("[Exception] ", e);
        return ResponseUtils.error(ResponseCode.INTERNAL_SERVER_ERROR);
    }

}
