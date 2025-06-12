package com.station.customer;


import com.station.common.entity.AuthenticationType;
import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.utility.helper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.random.RandomGenerator;

@Service
@Transactional
public class CustomerService {


    private final PasswordEncoder passwordEncoder;

    private final CustomerRepository customerRepository;

    public CustomerService(PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    public boolean isEmailUnique(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    }

    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        String randomCode = helper.generateRandomString(64);
        customer.setVerificationCode(randomCode);

        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verificationCode) {
        Customer customer = customerRepository.findByVerificationCode(verificationCode);
        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            customerRepository.enable(customer.getId());
            return true;
        }

    }

    public void updateAuthenticationType(Customer customer, AuthenticationType authenticationType) {
        if (!customer.getAuthenticationType().equals(authenticationType)) {
            customerRepository.updateAuthenticationType(customer.getId(), authenticationType);
        }
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFirstName(name);
        setName(name, customer);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");
        customer.setCity("");
        customer.setAddress("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customerRepository.save(customer);
    }

    private void setName(String name, Customer customer) {

        String[] nameArray = name.split("");
        if (nameArray.length < 2) {

            customer.setFirstName(name);
            customer.setLastName("");

        } else {

            String firstName = nameArray[0];
            customer.setFirstName(firstName);
            String lastName = name.replace(firstName, "");
            customer.setLastName(lastName);
        }
    }

    public void update(Customer customerInForm) {

        Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();

        if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (!customerInForm.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
                customerInForm.setPassword(encodedPassword);
            } else {
                customerInForm.setPassword(customerInDB.getPassword());
            }
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

    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByEmail(email);

        if (customer != null) {
            String token = generateSecureToken(24);
            customer.setResetPasswordToken(token);
            customerRepository.save(customer);
            return token;
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email: " + email);
        }
    }

    public static String generateSecureToken(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Customer getByResetPasswordToken(String token) {
        return customerRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByResetPasswordToken(token);
        if (customer == null) {
            throw new CustomerNotFoundException("No customer found: invalid token");
        }
        customer.setPassword(newPassword);
        customer.setResetPasswordToken(null);
        encodePassword(customer);
        customerRepository.save(customer);
    }

}
