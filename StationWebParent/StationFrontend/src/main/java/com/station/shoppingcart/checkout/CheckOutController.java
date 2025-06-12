package com.station.shoppingcart.checkout;

import com.station.common.entity.CartItem;
import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.customer.CustomerService;
import com.station.shoppingcart.ShoppingCartService;
import com.station.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CheckOutController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);

        float estimatedTotal = 0.0f;
        for (CartItem item : cartItems) {
            estimatedTotal += item.getSubTotal();
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);
        model.addAttribute("checkoutForm", new CheckOutForm());

        return "checkout/checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEMailOfAuthenticatedToken(request);
        return customerService.getCustomerByEmail(email);
    }


    @GetMapping("/payment-result")
    public String showPaymentResult() {
        return "checkout/payment-result";
    }

}
