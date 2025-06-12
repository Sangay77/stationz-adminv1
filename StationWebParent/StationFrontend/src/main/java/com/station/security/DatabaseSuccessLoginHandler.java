package com.station.security;

import com.station.common.entity.AuthenticationType;
import com.station.common.entity.Customer;
import com.station.customer.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DatabaseSuccessLoginHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final CustomerService customerService;

    public DatabaseSuccessLoginHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        Customer customer = userDetails.getCustomer();
        customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
