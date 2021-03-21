package ru.vohmin.controller;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vohmin.model.User;
import ru.vohmin.service.RoleRepository;
import ru.vohmin.service.UserService;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AdminController(UserService service, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.service = service;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @GetMapping(value = "/users")
    public String getUsers(ModelMap model) {
        model.addAttribute("authUser", getAuthorizedUser());
        model.addAttribute("users", service.getUsers());
        return "users";
    }

    @GetMapping("/add_page")
    public String redirectToAddUserForm(ModelMap map) {
        map.addAttribute("authUser", getAuthorizedUser());
        map.addAttribute("user", new User());
        map.addAttribute("allRoles", roleRepository.findAll());
        return "add_user";
    }

    @PostMapping("/add_user")
    public String addUser(@ModelAttribute User user, @RequestParam("roles") String[] rolesFromHtml) {
        user.setPassword(encoder.encode(user.getPassword()));
        service.addUser(user, rolesFromHtml);
        return "redirect:/admin/users";
    }

    @PostMapping("/del_user/{id}")
    public String delUser(@PathVariable @Validated @NotNull Long id) {
        service.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/update/{id}")
    public String redirectToMergePage(@PathVariable @Validated @NotNull Long id, ModelMap map) {
        map.addAttribute("user", service.findUser(id));
        map.addAttribute("allRoles", roleRepository.findAll());
        return "update";
    }

    @PostMapping("/update/merge")
    public String updateUser(@ModelAttribute User user) {
        service.updateUser(user);
        return "redirect:/admin/users";
    }

    private User getAuthorizedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
