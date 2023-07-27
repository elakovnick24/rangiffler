package com.elakov.rangiffler.service;

import com.elakov.rangiffler.domain.RangifflerUserPrincipal;
import com.elakov.rangiffler.data.UserEntity;
import com.elakov.rangiffler.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RangifflerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public RangifflerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new RangifflerUserPrincipal(user);
    }
}
