package com.platform.authservice.service;

import com.platform.authservice.entities.Roles;
import com.platform.authservice.entities.User;
import com.platform.authservice.jwt.JwtUtil;
import com.platform.authservice.repository.RoleRepository;
import com.platform.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(String email, String password, String role){

        if(userRepository.findByEmail(email).isPresent()) throw new RuntimeException("User already exsists");

        Roles userRole = roleRepository.findByName(role)
                .orElseThrow(() ->  new RuntimeException("Role does not exsits"));

        User newUser = new User();
        newUser.setEmail(email);
        newUser.getRoles().add(userRole);
        newUser.setPasswordHash(passwordEncoder.encode(password));

        userRepository.save(newUser);

        return jwtUtil.generateToken(newUser);
    }

    public String login(String email, String password){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if(!passwordEncoder.matches(password,user.getPasswordHash())){
            throw new RuntimeException("Invalid Credentials");
        }

        return jwtUtil.generateToken(user);
    }

    public User getCurrentUser(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }
}
