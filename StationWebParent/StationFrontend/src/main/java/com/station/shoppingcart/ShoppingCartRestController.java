package com.station.shoppingcart;

import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.customer.CustomerService;
import com.station.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;


    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable(name = "quantity") Integer quantity,
                                   HttpServletRequest request) {


        try {
            Customer authenticatedCustomer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, authenticatedCustomer);
            return updatedQuantity + "item (s) of this product were added to your shopping cart";

        } catch (CustomerNotFoundException e) {
            return "you must login to add this product to cart";
        } catch (ShoppingCartException exception) {
            return exception.getMessage();
        }

    }


    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEMailOfAuthenticatedToken(request);
        if (email == null) {
            throw new CustomerNotFoundException("No Authenticated customer");
        }
        return customerService.getCustomerByEmail(email);
    }


    @PostMapping("/cart/update/{productId}/{quantity}")
    public @ResponseBody Map<String, Object> updateQuantity(@PathVariable(name = "productId") Integer productId,
                                                            @PathVariable(name = "quantity") Integer quantity,
                                                            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            Customer authenticatedCustomer = getAuthenticatedCustomer(request);
            float subTotal = shoppingCartService.updateQuantity(productId, quantity, authenticatedCustomer);
            float estimatedTotal = shoppingCartService.getEstimatedTotal(authenticatedCustomer); // Implement this method

            response.put("updatedSubtotal", subTotal);
            response.put("estimatedTotal", estimatedTotal);

        } catch (CustomerNotFoundException e) {
            response.put("error", "You must login to change quantity of product.");
        }

        return response;
    }

    @DeleteMapping("/cart/remove/{productId}")
    public Map<String, Object> removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Customer authenticatedCustomer = getAuthenticatedCustomer(request);
            shoppingCartService.removeProduct(productId, authenticatedCustomer);
            float estimatedTotal = shoppingCartService.getEstimatedTotal(authenticatedCustomer); // Ensure this is implemented

            response.put("message", "The product has been removed from your shopping cart.");
            response.put("estimatedTotal", estimatedTotal);
        } catch (CustomerNotFoundException e) {
            response.put("message", "You must login to remove product.");
            response.put("estimatedTotal", 0);
        }

        return response;
    }

}
