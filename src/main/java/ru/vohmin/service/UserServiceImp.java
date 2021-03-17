package ru.vohmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vohmin.dao.UserDao;
import ru.vohmin.model.User;

import java.util.List;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public void addUser(User user) {
        userDao.add(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        try {
            userDao.remove(id);
        } catch (Exception e) {
            log.error("Incorrect ID or exception in DataBase");
        }
    }

    @Transactional
    @Override
    public User findUser(Long id) {
        try {
            return userDao.findUser(id);
        } catch (Exception e) {
            log.error("Incorrect ID or exception in DataBase");
            return null;
        }
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsers() {
        return userDao.getUsers();
    }
}
