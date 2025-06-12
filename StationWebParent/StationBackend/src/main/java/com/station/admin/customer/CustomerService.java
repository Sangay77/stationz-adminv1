package com.station.admin.customer;

import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class CustomerService {

    public static final int CUSTOMERS_PER_PAGE = 5;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Page<Customer> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, CUSTOMERS_PER_PAGE, sort);

        if (keyword != null) {
            return customerRepository.findAll(keyword, pageable);
        }
        return customerRepository.findAll(pageable);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Could not find any customer with ID " + id));
    }

    public boolean isEmailUnique(Integer id, String email) {
        Customer existingCustomer = customerRepository.findByEmail(email);
        if (existingCustomer == null) return true;
        return existingCustomer.getId().equals(id);
    }


    public void save(Customer customerInForm) {

        Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();

        if (!customerInForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        } else {
            customerInForm.setPassword(customerInDB.getPassword());
        }
        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        customerRepository.save(customerInForm);
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Customer ID must not be null.");
        }

        Long count = customerRepository.countById(id);
        if (count == null || count == 0) {
            throw new CustomerNotFoundException("Customer does not exist with ID: " + id);
        }

        customerRepository.deleteById(id);
    }

}
