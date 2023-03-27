package br.com.ciceroednilson.service;

import br.com.ciceroednilson.repository.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    @JsonIgnore
    private String password;

    public static UserDetailsImpl build(final UserEntity userEntity) {
        final List<GrantedAuthority> authorities =
                userEntity
                        .getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList());
        return UserDetailsImpl
                .builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(authorities).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
