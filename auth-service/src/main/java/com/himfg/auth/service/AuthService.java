package com.himfg.auth.service;

import com.himfg.auth.client.user.UserClient;
import com.himfg.auth.client.user.dto.CreateUser;
import com.himfg.auth.client.user.dto.GetUser;
import com.himfg.auth.dto.request.AuthRegister;
import com.himfg.auth.dto.request.AuthLogin;
import com.himfg.auth.dto.request.ChangePassword;
import com.himfg.auth.entity.User;
import com.himfg.auth.exceptions.DuplicateEntryException;
import com.himfg.auth.exceptions.ForbiddenException;
import com.himfg.auth.exceptions.NoDataFoundException;
import com.himfg.auth.repository.UserRepository;
import com.himfg.auth.service.dto.LoginServiceResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ModelMapper modelMapper;

    private final boolean isDev;
    public AuthService(@Value("${ENVIRONMENT}") String environment) {
        this.isDev = "Develop".equalsIgnoreCase(environment);
    }


    public GetUser register(AuthRegister request) {
        try {

            User user = userRepository.save(User.builder()
                            .username(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .build());

            CreateUser createUser = modelMapper.map(request, CreateUser.class);

            createUser.setUserId(user.getUserId());

            return userClient.createUser(createUser);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException("Email already exists.", e);
        }
    }

    public LoginServiceResponse login(AuthLogin request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getEmail()).orElseThrow(() -> new NoDataFoundException("User not found"));

            GetUser getUser = userClient.updateLastLogin(user.getUserId());

            if(!getUser.getIsActive()){
                throw new ForbiddenException();
            }

            String accessToken = jwtService.generateToken(getUser);
            String refreshToken = jwtService.generateRefreshToken(getUser);

            getUser.setAccessToken(isDev ? accessToken : null);

            return new LoginServiceResponse(accessToken, refreshToken, getUser, new AuthLogin(user.getUsername(), user.getPassword()));

    }

    public String refreshToken(String refreshToken ){
            String username = jwtService.extractUsername(refreshToken);

            User user = userRepository.findByUsername(username).orElseThrow(() -> new NoDataFoundException("User not found"));

            GetUser getUser = userClient.getUserById(user.getUserId());

            assert getUser != null;

            return jwtService.generateToken(getUser);

    }

    public String changePassword(Long id_user, ChangePassword request) {

            User user = userRepository.findById(id_user).orElseThrow(() -> new NoDataFoundException("El usuario no existe"));

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);

            return "Cambio de contraseña exitoso.";
    }
}
