package ru.fa.internetshop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fa.internetshop.entity.CustomerOrder;
import ru.fa.internetshop.service.OrderService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/my")
    public String myOrders(Model model, Principal principal) {
        List<CustomerOrder> orders = orderService.findOrdersForUser(principal.getName());
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam("productId") Long productId,
                              @RequestParam("quantity") Integer quantity,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.createOrder(principal.getName(), productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно создан");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOrders(Model model) {
        List<CustomerOrder> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "orders/admin-list";
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public String confirm(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        orderService.confirmOrder(id);
        redirectAttributes.addFlashAttribute("successMessage", "Заказ подтвержден");
        return "redirect:/orders/admin";
    }
}
