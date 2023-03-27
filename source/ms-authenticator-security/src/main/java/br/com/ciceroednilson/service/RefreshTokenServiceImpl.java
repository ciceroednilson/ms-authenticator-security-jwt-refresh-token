package br.com.ciceroednilson.service;

import br.com.ciceroednilson.controller.request.TokenRefreshRequest;
import br.com.ciceroednilson.controller.response.TokenRefreshResponse;
import br.com.ciceroednilson.exception.TokenRefreshException;
import br.com.ciceroednilson.repository.RefreshTokenRepository;
import br.com.ciceroednilson.repository.UserRepository;
import br.com.ciceroednilson.repository.entity.RefreshTokenEntity;
import br.com.ciceroednilson.repository.entity.UserEntity;
import br.com.ciceroednilson.service.definition.RefreshTokenService;
import br.com.ciceroednilson.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.expiration-refresh-token}")
    private int jwtExpirationRefreshToken;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public TokenRefreshResponse refreshToken(final TokenRefreshRequest request) {
        final String refreshToken = request.getRefreshToken();
        return this.findByToken(refreshToken)
                .map(this::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(user -> {
                    final String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return this.buildTokenRefreshResponse(token, refreshToken);
                }).orElseThrow(
                        () -> new TokenRefreshException("Refresh token there is not in data base")
                );
    }

    private TokenRefreshResponse buildTokenRefreshResponse(final String token, final String refreshToken) {
        return TokenRefreshResponse
                .builder()
                .accessToken(token)
                .type(JwtUtils.TYPE_BEARER)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(final String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity create(final Long userId) {
        final RefreshTokenEntity entity = this.buildRefreshTokenEntity(userId);
        return this.refreshTokenRepository.save(entity);
    }

    private RefreshTokenEntity buildRefreshTokenEntity(final Long userId) {
        final Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (!userEntity.isPresent()) {
            throw new TokenRefreshException("User not found!");
        }
        return RefreshTokenEntity
                .builder()
                .user(userEntity.get())
                .expiryDate(Instant.now().plusMillis(jwtExpirationRefreshToken))
                .token(UUID.randomUUID().toString())
                .build();
    }

    @Override
    public RefreshTokenEntity verifyExpiration(final RefreshTokenEntity entity) {
        if (entity.getExpiryDate().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(entity);
            throw new TokenRefreshException(String.format("Refresh token(%s) was expired!", entity.getToken()));
        }
        return entity;
    }

    @Override
    public int deleteByUserId(final long userId) {
        final Optional<UserEntity> userEntity = this.userRepository.findById(userId);
        return this.refreshTokenRepository.deleteByUser(userEntity.get());
    }
}
