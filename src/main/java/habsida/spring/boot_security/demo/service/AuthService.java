package habsida.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<Map<String, String>> authenticateUser(Map<String, String> loginData,
                                                                HttpServletRequest request) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            SecurityContextHolder.getContext().setAuthentication(auth);
            request.getSession(true);

            Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
            String role = roles.contains("ADMIN") ? "ADMIN" : "USER";

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "role", role
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", "error",
                            "message", "Invalid email or password"
                    ));
        }
    }

    public ResponseEntity<Map<String, String>> logoutUser(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("status", "logged_out"));
    }

    public ResponseEntity<Object> getCurrentAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "No user authenticated"));
        }
        return ResponseEntity.ok(auth.getPrincipal());
    }
}
