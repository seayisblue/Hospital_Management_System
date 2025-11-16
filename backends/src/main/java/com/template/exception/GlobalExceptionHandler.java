package com.template.exception;

import com.template.common.BusinessException;
import com.template.common.Result;
import com.template.common.ResultCode;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author template
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        System.err.println("业务异常：" + e.getMessage());
        return Result.fail(e.getResultCode().getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（@Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        System.err.println("参数校验异常：" + message);
        return Result.fail(ResultCode.PARAM_IS_INVALID.getCode(), message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        System.err.println("参数绑定异常：" + message);
        return Result.fail(ResultCode.PARAM_TYPE_BIND_ERROR.getCode(), message);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        System.err.println("空指针异常：" + e.getMessage());
        e.printStackTrace();
        return Result.fail(ResultCode.SYSTEM_INNER_ERROR.getCode(), "系统内部错误：空指针异常");
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        System.err.println("非法参数异常：" + e.getMessage());
        return Result.fail(ResultCode.PARAM_IS_INVALID.getCode(), e.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        System.err.println("运行时异常：" + e.getMessage());
        e.printStackTrace();
        return Result.fail(ResultCode.SYSTEM_INNER_ERROR.getCode(), "系统运行时错误");
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        System.err.println("系统异常：" + e.getMessage());
        e.printStackTrace();
        return Result.fail(ResultCode.SYSTEM_INNER_ERROR.getCode(), "系统内部错误");
    }
}

