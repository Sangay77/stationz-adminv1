package com.station.shoppingcart;

import com.station.common.entity.CartItem;
import com.station.common.entity.Customer;
import com.station.common.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCustomer(Customer customer);

    CartItem findByCustomerAndProduct(Customer customer, Product product);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity=?1 WHERE c.customer.id=?2 AND c.product.id=?3")
    void updateQuantity(Integer quantity, Integer customerId, Integer productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.customer.id=?1 AND c.product.id=?2")
    void deleteByCustomerAndProduct(Integer customerId, Integer productId);

    void deleteByCustomer(Customer customer);
}
