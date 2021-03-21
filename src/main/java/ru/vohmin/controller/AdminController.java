package ru.vohmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.vohmin.model.Role;
import ru.vohmin.model.User;
import ru.vohmin.service.RoleRepository;
import ru.vohmin.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
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
        List<User> users = service.getUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/add_page")
    public String redirectToAddUserForm(ModelMap map) {
        map.addAttribute("authUser", getAuthorizedUser());
        User user = new User();
        map.addAttribute("user", user);
        List<Role> allRoles = roleRepository.findAll();
        map.addAttribute("allRoles", allRoles);
        return "add_user";
    }

    @PostMapping("/add_user")
    public String addUser(@ModelAttribute User user, @RequestParam("roles") String[] rolesFromHtml) {
        Set<Role> roleSet = user.getRoles();
        for (String roleId : rolesFromHtml) {
            roleSet.add(roleRepository.findById(Long.valueOf(roleId)).get()); // создадим Set с одним значением
        }
        user.setPassword(encoder.encode(user.getPassword()));
        service.addUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/del_user/{id}")
    public String delUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/update/{id}")
    public String redirectToMergePage(@PathVariable Long id, ModelMap map) {
        User user = service.findUser(id);
        map.addAttribute("user", user);
        List<Role> allRoles = roleRepository.findAll();
        map.addAttribute("allRoles", allRoles);
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
