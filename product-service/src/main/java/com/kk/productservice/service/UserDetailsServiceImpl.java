package com.kk.productservice.service;

import com.kk.productservice.model.User;
import com.kk.productservice.model.UserDetailsImpl;
import com.kk.productservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class is used by spring security to get the UserDetails
 * <p>
 * **TODO**
 * UserDetailsServiceImpl should implement the UserDetailsService Interface of Spring Security
 * Use the Tests in the UserDetailsServiceImplTests class to complete this class
 * using TestDrivenDevelopment technique
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final String USER_NOT_FOUND = "User not Found : ";
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));
    }
}
