package org.example.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.config.exception.base.TokenException;
import org.example.until.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.sql.rowset.serial.SerialException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 * Create by Administrator
 * Data 1:43 2021/12/26 星期日
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*处理请求方式异常*/
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        handle(e);
        return Result.ofFail("请求缺少body或参数类型绑定错误");
    }

    /*处理请求缺少请求体异常*/
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        handle(e);
        return Result.ofFail("请求方式不支持");
    }

    /*处理Content-Type错误异常*/
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result handHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        handle(e);
        return Result.ofFail("Content-Type的类型错误");
    }

    /**
     * 参数校验异常
     *
     * @param e
     * @return
     */
    /*处理参数校验异常*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        handle(e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        return Result.ofFail(fieldError.getDefaultMessage());
    }

    /*处理参数校验异常*/
    @ExceptionHandler(BindException.class)
    public Result handBindException(BindException e) {
        handle(e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        return Result.ofFail(fieldError.getDefaultMessage());
    }

    /*处理参数校验异常*/
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handConstraintViolationException(ConstraintViolationException e) {
        handle(e);
        String message = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(";"));
        return Result.ofFail(message);
    }

    /*处理系统自定义异常*/
    @ExceptionHandler(SystemDefaultException.class)
    public Result handSystemDefaultException(SystemDefaultException e) {
        handle(e);
        return Result.ofFail(e.getMessage());
    }

    /*处理服务异常*/
    @ExceptionHandler(SerialException.class)
    public Result handSerialException(SerialException e) {
        handle(e);
        return Result.ofFail(e.getMessage());
    }

    /*url缺少参数*/
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        handle(e);
        return Result.ofFail(e.getMessage());
    }

    /**
     * @param e
     * @return
     */
    /*RuntimeException*/
    @ExceptionHandler(RuntimeException.class)
    public Result handRuntimeException(RuntimeException e) {
        handle(e);
        return Result.ofFail(e.getMessage());
    }

    /*TokenException*/
    @ExceptionHandler(TokenException.class)
    public Result handTokenException(TokenException e) {
        handle(e);
        System.out.println(" token异常验证111 ,"+e.getCode());
        return Result.ofFailByCode(401, e.getMessage());
    }

    /*处理其他异常*/
    @ExceptionHandler(Exception.class)
    public Result handException(Exception e) {
        handle(e);
        return Result.ofFail("请求处理异常");
    }

    private void handle(Exception e) {
        log.error("全局异常处理{}", e);//记录错误信息
    }


}
