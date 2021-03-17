package ru.vohmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vohmin.model.User;
import ru.vohmin.service.UserService;

import java.util.List;

@Controller
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/")
    public String printWelcome() {
        return "index";
    }

    @GetMapping(value = "/users")
    public String getUsers(ModelMap model) {
        List<User> users = service.getUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/add_page")
    public String addUserForm() {
        return "add_user";
    }

    @PostMapping("/add_user")
    public String addUser(@ModelAttribute User user) {
        service.addUser(user);
        return "redirect:/users";
    }

    @PostMapping("/del_user/{id}")
    public String delUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/update/{id}")
    public String redirectToMergePage(@PathVariable Long id, ModelMap map) {
        User user = service.findUser(id);
        map.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/update/merge")
    public String updateUser(@ModelAttribute User user) {
        service.updateUser(user);
        return "redirect:/users";
    }
//    @PostMapping("/update")
//    public String updateUser(@ModelAttribute User temp) {
//
//        return "redirect:/users";
//    }
}
