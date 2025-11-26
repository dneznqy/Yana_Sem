package ru.fa.internetshop.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.fa.internetshop.entity.Category;
import ru.fa.internetshop.entity.Product;
import ru.fa.internetshop.entity.Role;
import ru.fa.internetshop.entity.User;
import ru.fa.internetshop.repository.CategoryRepository;
import ru.fa.internetshop.repository.ProductRepository;
import ru.fa.internetshop.repository.RoleRepository;
import ru.fa.internetshop.repository.UserRepository;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      CategoryRepository categoryRepository,
                                      ProductRepository productRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                Role role = new Role("ADMIN");
                return roleRepository.save(role);
            });
            Role customerRole = roleRepository.findByName("CUSTOMER").orElseGet(() -> {
                Role role = new Role("CUSTOMER");
                return roleRepository.save(role);
            });

            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setFullName("Администратор системы");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.getRoles().add(adminRole);
                admin.getRoles().add(customerRole);
                userRepository.save(admin);
            }

            if (categoryRepository.count() == 0) {
                Category electronics = new Category();
                electronics.setName("Электроника");
                electronics.setDescription("Смартфоны, ноутбуки и другая техника");
                categoryRepository.save(electronics);

                Category books = new Category();
                books.setName("Книги");
                books.setDescription("Художественная и учебная литература");
                categoryRepository.save(books);

                if (productRepository.count() == 0) {
                    Product phone = new Product();
                    phone.setName("Смартфон X100");
                    phone.setDescription("Современный смартфон с большим экраном");
                    phone.setCategory(electronics);
                    phone.setPrice(new BigDecimal("39990.00"));
                    phone.setQuantity(10);
                    productRepository.save(phone);

                    Product book = new Product();
                    book.setName("Java для начинающих");
                    book.setDescription("Учебник по языку программирования Java");
                    book.setCategory(books);
                    book.setPrice(new BigDecimal("990.00"));
                    book.setQuantity(50);
                    productRepository.save(book);
                }
            }
        };
    }
}
