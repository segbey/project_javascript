package habsida.spring.boot_security.demo.service;


import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

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

    public Optional<User> findByIdOptional(Long id) {
        return userRepository.findById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User save(User user) {
        encodePasswordIfNeeded(user);
        user.setRoles(new HashSet<>(user.getRoles()));
        return userRepository.save(user);
    }

    public boolean editUser(Long id, User editedUser) {
        return userRepository.findById(id).map(existing -> {
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
            return true;
        }).orElse(false);
    }

    public boolean deleteIfExists(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void encodePasswordIfNeeded(User user) {
        String password = user.getPassword();
        if (password != null && !password.startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(password));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
    }
}

