package rw.ac.rca.springboot_todo_with_auth.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticationResponse {
    private String token;
}
