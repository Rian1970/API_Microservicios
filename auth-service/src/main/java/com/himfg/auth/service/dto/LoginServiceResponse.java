package com.himfg.auth.service.dto;

import com.himfg.auth.client.user.dto.GetUser;
import com.himfg.auth.dto.request.AuthLogin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginServiceResponse {
    private String accessToken;
    private String refreshToken;
    private GetUser userData;
    private AuthLogin authLogin;
}
