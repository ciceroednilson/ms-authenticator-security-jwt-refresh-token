package br.com.ciceroednilson.service.definition;

import br.com.ciceroednilson.controller.request.LoginRequest;
import br.com.ciceroednilson.controller.request.SignupRequest;
import br.com.ciceroednilson.controller.response.JwtResponse;
import br.com.ciceroednilson.controller.response.MessageResponse;

public interface AuthService {

    JwtResponse authenticateUser(LoginRequest request);
    MessageResponse registerUser(SignupRequest request);
}
