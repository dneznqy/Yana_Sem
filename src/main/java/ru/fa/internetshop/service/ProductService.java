package ru.fa.internetshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fa.internetshop.entity.Product;
import ru.fa.internetshop.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> searchProducts(String search,
                                        Long categoryId,
                                        int page,
                                        int size,
                                        String sortField,
                                        String sortDir) {

        String field = (sortField == null || sortField.isBlank()) ? "name" : sortField;
        Sort sort = Sort.by(field);
        if ("desc".equalsIgnoreCase(sortDir)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        boolean hasSearch = search != null && !search.isBlank();
        boolean hasCategory = categoryId != null;

        if (hasSearch && hasCategory) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory_Id(search, categoryId, pageable);
        } else if (hasSearch) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (hasCategory) {
            return productRepository.findByCategory_Id(categoryId, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
