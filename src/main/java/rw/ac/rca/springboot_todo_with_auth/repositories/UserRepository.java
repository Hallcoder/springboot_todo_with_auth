package rw.ac.rca.springboot_todo_with_auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.ac.rca.springboot_todo_with_auth.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Integer, User> {
    Optional<User> findByEmail(String email);
}
