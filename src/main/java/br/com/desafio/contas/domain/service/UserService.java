package br.com.desafio.contas.domain.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        // Este é um usuario FAKE não está sendo puxado do banco de dados
        if ("user".equalsIgnoreCase(username)) {
        	// Senha "12345" encriptada com BCrypt
            String encodedPassword = new BCryptPasswordEncoder().encode("12345");
            return new User("user", encodedPassword, Collections.emptyList()); // Password encriptada
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}