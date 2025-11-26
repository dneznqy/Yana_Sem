package ru.fa.internetshop.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fa.internetshop.dto.RegistrationForm;
import ru.fa.internetshop.entity.Role;
import ru.fa.internetshop.entity.User;
import ru.fa.internetshop.repository.RoleRepository;
import ru.fa.internetshop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerNewUser(RegistrationForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким e-mail уже существует");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароль и подтверждение пароля не совпадают");
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setActive(true);

        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new IllegalStateException("Роль CUSTOMER не найдена"));
        user.getRoles().add(role);

        userRepository.save(user);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
