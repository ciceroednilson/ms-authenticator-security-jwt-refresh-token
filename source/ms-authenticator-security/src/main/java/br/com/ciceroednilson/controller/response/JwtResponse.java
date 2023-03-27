package br.com.ciceroednilson.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
