package ru.fa.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fa.internetshop.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
