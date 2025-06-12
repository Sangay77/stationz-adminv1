package com.station.shoppingcart;

import com.station.common.entity.CartItem;
import com.station.common.entity.Customer;
import com.station.common.entity.Product;
import com.station.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ShoppingCartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {

        Integer updatedQuantity = quantity;
        Product product = new Product(productId);

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

        if (cartItem != null) {
            updatedQuantity = cartItem.getQuantity() + quantity;
            if (updatedQuantity > 5) {
                throw new ShoppingCartException("Could not add more " + quantity + "item(s) because there are already " + cartItem.getQuantity());
            }
        } else {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }
        cartItem.setQuantity(updatedQuantity);
        cartItemRepository.save(cartItem);
        return updatedQuantity;
    }

    public List<CartItem> listCartItems(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
        cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
        Product product = productRepository.findById(productId).get();
        return calculateSubtotal(product, quantity);

    }

    public static float calculateSubtotal(Product product, int quantity) {
        float price = product.getPrice();
        float discountPercent = product.getDiscountPercent();
        float discountedPrice = price * (1 - discountPercent / 100.0f);
        return discountedPrice * quantity;
    }

    public float getEstimatedTotal(Customer customer) {
        // Calculate total of all cart items for this customer
        return cartItemRepository.findByCustomer(customer)
                .stream()
                .map(CartItem::getSubTotal) // e.g. price * quantity
                .reduce(0.0f, Float::sum);
    }

    public void removeProduct(Integer productId, Customer customer) {
        cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
    }
}
