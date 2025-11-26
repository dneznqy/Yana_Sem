package ru.fa.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fa.internetshop.entity.CustomerOrder;
import ru.fa.internetshop.entity.OrderStatus;
import ru.fa.internetshop.entity.User;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByCustomer(User customer);

    List<CustomerOrder> findByStatus(OrderStatus status);
}
