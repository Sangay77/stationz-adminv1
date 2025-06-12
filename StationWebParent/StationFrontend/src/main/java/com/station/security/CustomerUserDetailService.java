package com.station.security;

import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerUserDetailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            throw new UsernameNotFoundException("No Customer found with email: " + email);
        }
        return new CustomerUserDetails(customer);
    }
}
