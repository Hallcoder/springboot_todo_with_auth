package rw.ac.rca.springboot_todo_with_auth.auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;
}
