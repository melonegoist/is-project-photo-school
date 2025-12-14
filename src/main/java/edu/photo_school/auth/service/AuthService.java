package edu.photo_school.auth.service;

import edu.photo_school.auth.dto.AuthResponse;
import edu.photo_school.auth.dto.LoginRequest;
import edu.photo_school.auth.dto.RefreshTokenRequest;
import edu.photo_school.auth.dto.RegisterRequest;
import edu.photo_school.auth.security.CustomUserDetails;
import edu.photo_school.auth.security.CustomUserDetailsService;
import edu.photo_school.auth.security.JwtTokenProvider;
import edu.photo_school.common.exception.BusinessException;
import edu.photo_school.user.dto.CreateUserRequest;
import edu.photo_school.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       UserService userService,
                       CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        CreateUserRequest createUserRequest = new CreateUserRequest(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.roles()
        );

        CustomUserDetails userDetails = new CustomUserDetails(userService.createUserEntity(createUserRequest));
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());
        return AuthResponse.bearer(accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal.getUsername());
        return AuthResponse.bearer(accessToken, refreshToken);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateRefreshToken(request.refreshToken())) {
            throw new BusinessException("Invalid refresh token");
        }
        String username = jwtTokenProvider.extractUsername(request.refreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username);
        return AuthResponse.bearer(accessToken, refreshToken);
    }
}
