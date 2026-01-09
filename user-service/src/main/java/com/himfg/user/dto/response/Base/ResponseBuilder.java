package com.himfg.user.dto.response.Base;

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

    public static <T, R> ResponseEntity<BaseResponse> ok(String message, T request, R data) {
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
}