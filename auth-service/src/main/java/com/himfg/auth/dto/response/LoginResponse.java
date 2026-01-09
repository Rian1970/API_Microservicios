package com.himfg.auth.dto.response;

import com.himfg.auth.client.user.dto.GetUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private GetUser userData;
}
