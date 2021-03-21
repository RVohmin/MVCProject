package ru.vohmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vohmin.dao.UserDao;
import ru.vohmin.model.Role;
import ru.vohmin.model.User;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@EnableJpaRepositories("ru.vohmin")
public class UserServiceImp implements UserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public boolean addUser(User user, String[] rolesFromHtml) {
        Set<Role> roleSet = user.getRoles();
        for (String roleId : rolesFromHtml) {
            roleSet.add(roleRepository.findById(Long.valueOf(roleId)).get());
        }
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public User findUser(Long id) {
        return userDao.findUser(id);
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

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
