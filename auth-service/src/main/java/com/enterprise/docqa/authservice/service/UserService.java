package com.enterprise.docqa.authservice.service;

import com.enterprise.docqa.authservice.model.UserEntity;
import com.enterprise.docqa.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public boolean validateCredentials(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public void createDefaultUsers() {
        if (userRepository.count() == 0) {
            List<UserEntity> defaults = List.of(
                    new UserEntity(null, "admin", passwordEncoder.encode("Admin123!"), "Admin", "admin@enterprise.ai"),
                    new UserEntity(null, "dataengineer", passwordEncoder.encode("DataEng123!"), "Data Engineer", "deng@enterprise.ai"),
                    new UserEntity(null, "compliance", passwordEncoder.encode("Compliance123!"), "Compliance Officer", "compliance@enterprise.ai")
            );
            userRepository.saveAll(defaults);
        }
    }

    @PostConstruct
    public void init() {
        createDefaultUsers();
    }
}
