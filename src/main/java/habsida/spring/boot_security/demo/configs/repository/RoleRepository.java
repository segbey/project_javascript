package habsida.spring.boot_security.demo.configs.repository;

import habsida.spring.boot_security.demo.configs.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

