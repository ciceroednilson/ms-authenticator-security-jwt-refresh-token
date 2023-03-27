package br.com.ciceroednilson.service.definition;

import br.com.ciceroednilson.controller.request.TokenRefreshRequest;
import br.com.ciceroednilson.controller.response.TokenRefreshResponse;
import br.com.ciceroednilson.repository.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    Optional<RefreshTokenEntity> findByToken(String token);
    RefreshTokenEntity create(Long userId);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity entity);

    int deleteByUserId(long userId);
}
