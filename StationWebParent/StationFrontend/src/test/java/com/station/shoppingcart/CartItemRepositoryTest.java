package com.station.shoppingcart;

import com.station.common.entity.CartItem;
import com.station.common.entity.Customer;
import com.station.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveItem() {

        Integer customerId = 27;
        Integer productId = 2;
        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem cartItem = new CartItem();
        cartItem.setCustomer(customer);
        cartItem.setProduct(product);
        cartItem.setQuantity(3);

        CartItem item = cartItemRepository.save(cartItem);
        assertThat(item.getId()).isGreaterThan(0);
    }

    @Test
    public void updateQuantity(){
        Integer customerId = 27;
        Integer productId = 2;
        Integer quantity=10;

        cartItemRepository.updateQuantity(quantity,customerId,productId);
    }

    @Test
    public void deleteProduct(){
        Integer customerId = 27;
        Integer productId = 2;

        cartItemRepository.deleteByCustomerAndProduct(customerId,productId);
    }

}