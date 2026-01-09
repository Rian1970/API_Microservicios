package com.himfg.user.utils;

import com.himfg.user.dto.response.Base.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class BaseResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (!(body instanceof BaseResponse<?> baseResponse))
            return body;

        HttpServletRequest servletRequest =
                ((ServletServerHttpRequest) request).getServletRequest();

        Long start = (Long) servletRequest.getAttribute("startTime");

        if (start != null) {
            long execTime = System.currentTimeMillis() - start;
            baseResponse.setExecutionTimeMs(execTime);
        }

        // Headers desde el Interceptor
        String host = (String) servletRequest.getAttribute("host");
        String userAgent = (String) servletRequest.getAttribute("userAgent");
        String accept = (String) servletRequest.getAttribute("accept");
        String contentType = (String) servletRequest.getAttribute("contentType");

        if (baseResponse.getMessage() != null && baseResponse.getMessage().getHeaders() != null) {
            baseResponse.getMessage().getHeaders().setHost(host);
            baseResponse.getMessage().getHeaders().setUserAgent(userAgent);
            baseResponse.getMessage().getHeaders().setAccept(accept);
            baseResponse.getMessage().getHeaders().setContentType(contentType);
        }

        return baseResponse;
    }
}

