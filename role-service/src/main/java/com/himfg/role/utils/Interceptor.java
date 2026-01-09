package com.himfg.role.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("host", request.getHeader("Host"));
        request.setAttribute("userAgent", request.getHeader("User-Agent"));
        request.setAttribute("accept", request.getHeader("Accept"));
        request.setAttribute("contentType", request.getHeader("Content-Type"));

        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
}

