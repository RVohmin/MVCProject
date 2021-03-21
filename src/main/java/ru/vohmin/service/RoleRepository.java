package ru.vohmin.service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vohmin.model.Role;

@Repository
@Qualifier("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {
}