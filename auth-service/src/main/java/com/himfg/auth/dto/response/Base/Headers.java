package com.himfg.auth.dto.response.Base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Headers {
    private String host;
    private String userAgent;
    private String accept;
    private String contentType;
}
