package ru.vohmin.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vohmin.model.User;

@Repository
@Qualifier(value="userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
