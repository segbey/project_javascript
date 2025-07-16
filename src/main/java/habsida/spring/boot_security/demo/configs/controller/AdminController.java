package habsida.spring.boot_security.demo.configs.controller;


import habsida.spring.boot_security.demo.configs.model.User;
import habsida.spring.boot_security.demo.configs.service.UserService;
import habsida.spring.boot_security.demo.configs.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleServiceImpl roleService;


    @GetMapping
    public String adminPage(@RequestParam(required = false) String view,
                            @RequestParam(required = false) Long editId,
                            @RequestParam(required = false) Long deleteId,
                            @RequestParam(required = false, name = "create") Boolean showCreateForm,
                            Model model, Principal principal) {
        model.addAttribute("authUser", userService.findByEmail(principal.getName()));
        model.addAttribute("viewUser", "user".equals(view));
        model.addAttribute("showNewForm", Boolean.TRUE.equals(showCreateForm));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", roleService.findAll());

        if (editId != null) model.addAttribute("editUser", userService.findById(editId));
        if (deleteId != null) model.addAttribute("deleteUser", userService.findById(deleteId));
        if (!model.containsAttribute("user")) model.addAttribute("user", new User());

        return "admin";
    }


    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin?view=admin";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("editUser") User user) {
        userService.editUser(user);
        return "redirect:/admin?view=admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("deleteUser") User user) {
        userService.deleteById(user.getId());
        return "redirect:/admin?view=admin";
    }



}

