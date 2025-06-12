package com.station.shoppingcart;

import com.station.common.entity.CartItem;
import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.customer.CustomerService;
import com.station.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);

        float estimatedTotal = 0.0f;
        for (CartItem item : cartItems) {
            estimatedTotal += item.getSubTotal();
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);


        model.addAttribute("cartItems",cartItems);
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEMailOfAuthenticatedToken(request);
        return customerService.getCustomerByEmail(email);
    }
}
