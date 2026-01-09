package com.himfg.permission.dto.response.Base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private int statusCode;
    private Long executionTimeMs;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Message<T> message;

    public BaseResponse(int statusCode, Message<T> message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
