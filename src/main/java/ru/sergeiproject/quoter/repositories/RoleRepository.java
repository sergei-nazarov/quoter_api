package ru.sergeiproject.quoter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sergeiproject.quoter.data.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
