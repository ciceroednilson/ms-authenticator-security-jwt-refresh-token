package br.com.ciceroednilson.controller;

import br.com.ciceroednilson.controller.request.LoginRequest;
import br.com.ciceroednilson.controller.request.SignupRequest;
import br.com.ciceroednilson.controller.request.TokenRefreshRequest;
import br.com.ciceroednilson.controller.response.JwtResponse;
import br.com.ciceroednilson.controller.response.MessageResponse;
import br.com.ciceroednilson.controller.response.TokenRefreshResponse;
import br.com.ciceroednilson.service.definition.AuthService;
import br.com.ciceroednilson.service.definition.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest request) {
        try {
            final JwtResponse response = authService.authenticateUser(request);
            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            log.error("Error to authenticate the user", ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest request) {
        try {
            final MessageResponse response = authService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            log.error("Error to register the user", ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        try {
            final TokenRefreshResponse response = refreshTokenService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (final Exception ex) {
            log.error("Error to refresh token", ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
