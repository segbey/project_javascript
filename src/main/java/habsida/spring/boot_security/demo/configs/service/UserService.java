package habsida.spring.boot_security.demo.configs.service;


import habsida.spring.boot_security.demo.configs.model.Role;
import habsida.spring.boot_security.demo.configs.model.User;
import habsida.spring.boot_security.demo.configs.repository.RoleRepository;
import habsida.spring.boot_security.demo.configs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    public User save(User user) {
        Set<Role> roles = new HashSet<>(user.getRoles());
        user.setRoles(roles);

        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void editUser(Long Id, User editedUser) {
        User existing = userRepository.findById(Id).orElseThrow();

        existing.setFirstName(editedUser.getFirstName());
        existing.setLastName(editedUser.getLastName());
        existing.setAge(editedUser.getAge());
        existing.setEmail(editedUser.getEmail());

        if (editedUser.getPassword() != null && !editedUser.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(editedUser.getPassword()));
        }

        Set<Role> roles = new HashSet<>(editedUser.getRoles());
        existing.setRoles(roles);

        userRepository.save(existing);
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found" + email));
    }
}

