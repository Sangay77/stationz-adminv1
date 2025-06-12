package com.station.admin.order;

import com.station.common.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void testNewAddOrder() {
        Product product = entityManager.find(Product.class, 1);
        Customer customer = entityManager.find(Customer.class, 27);

        Order mainOrder = new Order();
        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();


        mainOrder.setShippingCost(10);
        mainOrder.setProductCost(product.getCost());
        mainOrder.setSubtotal(product.getPrice());
        mainOrder.setTax(0);
        mainOrder.setTotal(product.getPrice() + 10);
        mainOrder.setDeliverDays(5);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setPaymentMethod(PaymentMethod.RMA_PG);
        mainOrder.setOrderStatus(OrderStatus.NEW);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setOrder(mainOrder);
        orderDetail.setQuantity(1);
        orderDetail.setUnitPrice(product.getPrice());
        orderDetail.setProductCost(product.getCost());
        orderDetail.setSubtotal(product.getPrice());
        orderDetail.setShippingCost(10);

        mainOrder.getOrderDetails().add(orderDetail);

        Order savedOrder = orderRepository.save(mainOrder);

        assertNotNull(savedOrder.getId());
    }
}