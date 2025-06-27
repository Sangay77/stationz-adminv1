package com.station.global;

import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.customer.CustomerService;
import com.station.shoppingcart.ShoppingCartService;
import com.station.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;

    @ModelAttribute("cartCount")
    public int getCartItemCount(HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);
            return shoppingCartService.getTotalQuantity(customer);
        } catch (Exception e) {
            return 0;
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEMailOfAuthenticatedToken(request);
        return customerService.getCustomerByEmail(email);
    }
}
