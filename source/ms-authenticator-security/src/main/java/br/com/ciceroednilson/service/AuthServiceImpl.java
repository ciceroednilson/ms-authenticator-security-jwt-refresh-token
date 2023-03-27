package br.com.ciceroednilson.service;

import br.com.ciceroednilson.controller.request.LoginRequest;
import br.com.ciceroednilson.controller.request.SignupRequest;
import br.com.ciceroednilson.controller.response.JwtResponse;
import br.com.ciceroednilson.controller.response.MessageResponse;
import br.com.ciceroednilson.exception.InvalidRequestException;
import br.com.ciceroednilson.repository.RoleRepository;
import br.com.ciceroednilson.repository.UserRepository;
import br.com.ciceroednilson.repository.entity.EnumRole;
import br.com.ciceroednilson.repository.entity.RefreshTokenEntity;
import br.com.ciceroednilson.repository.entity.RoleEntity;
import br.com.ciceroednilson.repository.entity.UserEntity;
import br.com.ciceroednilson.service.definition.AuthService;
import br.com.ciceroednilson.service.definition.RefreshTokenService;
import br.com.ciceroednilson.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public JwtResponse authenticateUser(final LoginRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtils.generateJwtToken(authentication);
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final RefreshTokenEntity refreshTokenEntity =  refreshTokenService.create(userDetails.getId());
        final List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return JwtResponse
                .builder()
                .token(jwt)
                .refreshToken(refreshTokenEntity.getToken())
                .type(JwtUtils.TYPE_BEARER)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public MessageResponse registerUser(final SignupRequest request) {
        validateRegisterUser(request);
        final UserEntity userEntity = buildUserEntity(request);
        final Set<String> roles = request.getRole();
        final Set<RoleEntity> rolesEntity = new HashSet<>();
        if (Objects.isNull(roles)) {
            final RoleEntity role = this.roleRepository.findByName(EnumRole.ROLE_USER)
                    .orElseThrow(() -> new InvalidRequestException("Role not found!"));
            rolesEntity.add(role);
        } else {
            roles.forEach( role -> {
                final EnumRole enumRole = EnumRole.findByValue(role)
                        .orElseThrow(() -> new InvalidRequestException("Role not found in enum!"));
                final RoleEntity entity = this.roleRepository.findByName(enumRole)
                        .orElseThrow(() -> new InvalidRequestException("Role not found in repository!"));
                rolesEntity.add(entity);
            });
        }
        userEntity.setRoles(rolesEntity);
        this.userRepository.save(userEntity);
        return new MessageResponse("User registered successfully!");
    }

    private UserEntity buildUserEntity(final SignupRequest request) {
        return UserEntity
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    private void validateRegisterUser(final SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new InvalidRequestException("Username is already token!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email is already in user!");
        }
    }
}
