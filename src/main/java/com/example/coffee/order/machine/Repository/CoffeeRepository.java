package com.example.coffee.order.machine.Repository;

import com.example.coffee.order.machine.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, String> {

    @Modifying
    @Query(value = "UPDATE Coffee SET COFFEESTOCK = COFFEESTOCK - :quantity WHERE COFFEENAME = :name", nativeQuery = true)
    void updateCoffeeStock(@Param("quantity") Integer quantity, @Param("name") String name);

}
