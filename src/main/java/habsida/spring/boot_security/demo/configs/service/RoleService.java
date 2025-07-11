package habsida.spring.boot_security.demo.configs.service;

import habsida.spring.boot_security.demo.configs.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Role save(Role role);
    void deleteById(Long id);
}
