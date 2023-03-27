package br.com.ciceroednilson.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenRefreshResponse {

    private String accessToken;
    private String type;
    private String refreshToken;;
}
