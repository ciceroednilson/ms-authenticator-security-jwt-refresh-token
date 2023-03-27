package br.com.ciceroednilson.service;

import br.com.ciceroednilson.repository.UserRepository;
import br.com.ciceroednilson.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found!", username)));
        return UserDetailsImpl.build(entity);
    }
}
