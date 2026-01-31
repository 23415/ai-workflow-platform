package com.platform.authservice.authcontroller;

import com.platform.authservice.dto.LoginRequestDto;
import com.platform.authservice.dto.RegisterRequestDto;
import com.platform.authservice.entities.User;
import com.platform.authservice.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class authController {

    private final AuthService authService;

    public authController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String,String> register(@RequestBody RegisterRequestDto req){
        String token = authService.register(req.email(),req.password(),req.role());
        return Map.of("Token",token);
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequestDto req){
        String token = authService.login(req.email(),req.password());
        return Map.of("Token",token);
    }

    @GetMapping("/me")
    public User me(Authentication auth){
        return authService.getCurrentUser(auth.getName());
    }
}
