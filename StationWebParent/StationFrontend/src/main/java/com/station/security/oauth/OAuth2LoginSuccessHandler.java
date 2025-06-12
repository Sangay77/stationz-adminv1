package com.station.security.oauth;

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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    private final CustomerService customerService;

    public OAuth2LoginSuccessHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User customerOAuth2User = (CustomerOAuth2User) authentication.getPrincipal();
        String name = customerOAuth2User.getFullName();
        String email = customerOAuth2User.getEmail();
        String countryCode = request.getLocale().getCountry();


        Customer customer = customerService.getCustomerByEmail(email);

        if (customer == null) {
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode);
        } else {
            customerOAuth2User.setFullName(customer.getFullName());
            customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
