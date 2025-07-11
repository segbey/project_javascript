package habsida.spring.boot_security.demo.configs.controller;


import habsida.spring.boot_security.demo.configs.model.User;
import habsida.spring.boot_security.demo.configs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getCurrentUser(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/error";
        }
        model.addAttribute("user", user);
        return "user";
    }
}
