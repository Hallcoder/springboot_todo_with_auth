package rw.ac.rca.springboot_todo_with_auth.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.SessionDelegatorBaseImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import rw.ac.rca.springboot_todo_with_auth.auth.AuthenticationRequest;
import rw.ac.rca.springboot_todo_with_auth.auth.AuthenticationResponse;
import rw.ac.rca.springboot_todo_with_auth.auth.JwtService;
import rw.ac.rca.springboot_todo_with_auth.auth.RegisterRequest;
import rw.ac.rca.springboot_todo_with_auth.model.Role;
import rw.ac.rca.springboot_todo_with_auth.model.User;
import rw.ac.rca.springboot_todo_with_auth.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .password(request.getPassword())
                .role(Role.STANDARD)
                .email(request.getEmail())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
