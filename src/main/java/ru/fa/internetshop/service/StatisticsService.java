package ru.fa.internetshop.service;

import org.springframework.stereotype.Service;
import ru.fa.internetshop.entity.CustomerOrder;
import ru.fa.internetshop.repository.CustomerOrderRepository;
import ru.fa.internetshop.repository.ProductRepository;
import ru.fa.internetshop.repository.UserRepository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderRepository orderRepository;

    public StatisticsService(UserRepository userRepository,
                             ProductRepository productRepository,
                             CustomerOrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public long getProductCount() {
        return productRepository.count();
    }

    public long getOrderCount() {
        return orderRepository.count();
    }

    public Optional<Duration> getAverageOrderConfirmationDuration() {
        List<CustomerOrder> confirmed = orderRepository.findByStatus(ru.fa.internetshop.entity.OrderStatus.CONFIRMED);
        if (confirmed.isEmpty()) {
            return Optional.empty();
        }
        long totalSeconds = 0;
        int count = 0;
        for (CustomerOrder order : confirmed) {
            if (order.getConfirmedAt() != null) {
                totalSeconds += Duration.between(order.getCreatedAt(), order.getConfirmedAt()).getSeconds();
                count++;
            }
        }
        if (count == 0) {
            return Optional.empty();
        }
        return Optional.of(Duration.ofSeconds(totalSeconds / count));
    }
}
