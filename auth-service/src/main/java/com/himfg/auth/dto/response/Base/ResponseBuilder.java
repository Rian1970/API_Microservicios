package com.himfg.auth.dto.response.Base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static <T> ResponseEntity<BaseResponse> ok(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(
                        200,
                        new Message<>(
                                null,
                                new Response<>(message, data)
                        )
                ));
    }

    public static <T> ResponseEntity<BaseResponse> ok(String message, T request, T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(
                        200,
                        new Message<>(
                                request,
                                new Response<>(message, data)
                        )
                ));
    }

    public static <T> ResponseEntity<BaseResponse> ok(String message, T request, T data, HttpHeaders headers) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(new BaseResponse<>(
                        200,
                        new Message<>(
                                request,
                                new Response<>(message, data)
                        )
                ));
    }
}