package com.himfg.user.dto.response.Base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message<T> {
    private Headers headers;
    private T request;
    private Response<T> response;

    public Message(T request, Response<T> response) {
        this.headers = new Headers();
        this.request = request;
        this.response = response;
    }
}
