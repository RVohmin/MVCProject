package ru.vohmin.dao;


import ru.vohmin.model.User;

import java.util.List;

public interface UserDao {
   void add(User user);
   List<User> getUsers();
   void remove(Long id);
   User findUser(Long id);
   User findUserByUsername(String username);
   void updateUser(User user);
}
