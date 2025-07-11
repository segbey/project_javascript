package habsida.spring.boot_security.demo.configs.repository;

import java.util.Optional;
import habsida.spring.boot_security.demo.configs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

