package ru.vohmin.service;


import ru.vohmin.model.User;

import java.util.List;

public interface UserService {
    boolean addUser(User user, String[] rolesFromHtml);
    boolean deleteUser(Long id);
    User findUser(Long id);
    void updateUser(User user);
    List<User> getUsers();
}
