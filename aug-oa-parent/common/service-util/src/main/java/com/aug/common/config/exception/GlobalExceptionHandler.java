package com.aug.common.config.exception;

import com.aug.common.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author querkecor
 * @date 2023/4/24
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception e) {
        return Result.fail().message(e.getMessage());
    }

    @ExceptionHandler(AugException.class)
    public Result<Object> augExceptionHandler(AugException e) {
        return Result.fail().message(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> accessException(AccessDeniedException e) {
        return Result.fail().message(e.getMessage());
    }

}
