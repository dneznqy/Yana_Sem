package ru.fa.internetshop.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.fa.internetshop.entity.CustomerOrder;
import ru.fa.internetshop.entity.OrderStatus;
import ru.fa.internetshop.entity.Product;
import ru.fa.internetshop.entity.User;
import ru.fa.internetshop.repository.CustomerOrderRepository;
import ru.fa.internetshop.repository.ProductRepository;
import ru.fa.internetshop.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(CustomerOrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createOrder(String username, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточное количество товара на складе");
        }

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(user);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        product.setQuantity(product.getQuantity() - quantity);

        productRepository.save(product);
        orderRepository.save(order);
    }

    public List<CustomerOrder> findOrdersForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        return orderRepository.findByCustomer(user);
    }

    public List<CustomerOrder> findAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void confirmOrder(Long id) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public List<CustomerOrder> findConfirmedOrders() {
        return orderRepository.findByStatus(OrderStatus.CONFIRMED);
    }
}
