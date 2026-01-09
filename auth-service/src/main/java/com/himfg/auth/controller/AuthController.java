package com.himfg.auth.controller;

import com.himfg.auth.dto.request.AuthRegister;
import com.himfg.auth.dto.request.AuthLogin;
import com.himfg.auth.dto.request.ChangePassword;
import com.himfg.auth.dto.response.Base.ResponseBuilder;
import com.himfg.auth.dto.response.LoginResponse;
import com.himfg.auth.service.AuthService;
import com.himfg.auth.service.JwtService;
import com.himfg.auth.dto.response.Base.BaseResponse;
import com.himfg.auth.service.dto.LoginServiceResponse;
import com.himfg.auth.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CookieUtils cookieUtils;

    @Autowired
    private JwtService jwtService;

    private final boolean isDev;

    public AuthController(@Value("${ENVIRONMENT}") String environment) {
        this.isDev = "Develop".equalsIgnoreCase(environment);
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody AuthRegister request) {
        return ResponseBuilder.ok("Usuario registrado exitosamente.", authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody AuthLogin request) {
        LoginServiceResponse loginResponse = authService.login(request);

        // Crear cookie solo para el REFRESH TOKEN
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", loginResponse.getRefreshToken())
                .httpOnly(true)         // No accesible desde JS
                .secure(false)           // Solo HTTPS en producción
                .sameSite("Strict")     // Protección CSRF
                .path("/auth/refresh")  // La cookie solo viaja a /auth/refresh
                .maxAge(7 * 24 * 60 * 60) // 7 días o lo que quieras
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("access_token", loginResponse.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        // Retornar accessToken en el body y refreshToken como cookie
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseBuilder.ok("Inicio de sesión exitoso.",
                loginResponse.getAuthLogin(),
                loginResponse.getUserData(),
                headers);
    }

    @PostMapping("/change_password")
    public ResponseEntity<BaseResponse> changePasword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ChangePassword request) {
        Long id_user = jwt.getClaim("userId");
        return ResponseBuilder.ok(authService.changePassword(id_user, request), null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieUtils.extractRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = authService.refreshToken(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseBuilder.ok("Access Token generado.",
                null,
                new LoginResponse(isDev ? newAccessToken : null, null),
                headers);

    }
}
