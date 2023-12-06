package ru.sergeiproject.quoter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sergeiproject.quoter.security.CustomUserPrincipal;
import ru.sergeiproject.quoter.data.Role;
import ru.sergeiproject.quoter.data.User;
import ru.sergeiproject.quoter.data.UserRegistrationDto;
import ru.sergeiproject.quoter.repositories.RoleRepository;
import ru.sergeiproject.quoter.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Configuration
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User saveUser(UserRegistrationDto userDto) {

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .created(LocalDateTime.now())
                .roles(List.of(role))
                .build();

        try {
            User newUser = userRepository.save(user);
            return newUser;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Username or email already registered");
        }
    }

    public Optional<User> getUserInfo(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public CustomUserPrincipal loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserPrincipal(user.get());
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

}
