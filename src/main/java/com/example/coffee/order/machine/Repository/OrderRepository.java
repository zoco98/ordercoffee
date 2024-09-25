package com.example.coffee.order.machine.Repository;

import com.example.coffee.order.machine.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
