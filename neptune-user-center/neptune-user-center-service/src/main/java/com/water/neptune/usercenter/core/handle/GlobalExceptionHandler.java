package com.water.neptune.usercenter.core.handle;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangmiaojie
 * 全局异常处理器
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseBody
    public String handleException(InvalidTokenException invalidTokenException) {
        // 打印异常堆栈信息
        System.out.println(invalidTokenException.getHttpErrorCode() + " : " + invalidTokenException.getHttpErrorCode());
        return "error";
    }

}
