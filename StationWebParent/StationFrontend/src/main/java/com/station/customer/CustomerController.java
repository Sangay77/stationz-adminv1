package com.station.customer;

import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.security.CustomerUserDetails;
import com.station.security.oauth.CustomerOAuth2User;
import com.station.setting.EmailSettingBag;
import com.station.setting.SettingService;
import com.station.utility.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.security.Principal;


@Controller
public class CustomerController {

    private final CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final SettingService settingService;

    public CustomerController(CustomerService customerService, SettingService settingService) {
        this.customerService = customerService;
        this.settingService = settingService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());
        return "customer/register";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model,
                                 HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);
        model.addAttribute("pageTitle", "Registration Succeeded!");
        return "/customer/registration_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {

        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());

        String verifyURL = Utility.getSiteUrl(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
        System.out.println("to address" + toAddress);
        System.out.println("verify url" + verifyURL);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = customerService.verify(code);
        return "customer/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {

        String email = Utility.getEMailOfAuthenticatedToken(request);
        Customer customer = customerService.getCustomerByEmail(email);
        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", "account settings");
        return "customer/account_form";
    }

    @PostMapping("/update_account_details")
    public String updateAccountDetails(Customer customer, RedirectAttributes ra,
                                       HttpServletRequest request) {
        customerService.update(customer);
        ra.addFlashAttribute("message", "Your account details have been updated");
        updateNameForAuthenticatedCustomer(customer, request);
        return "redirect:/account_details";

    }

    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {

        Object principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
            Customer authenticatedCustomer = userDetails.getCustomer();
            authenticatedCustomer.setFirstName(customer.getFirstName());
            authenticatedCustomer.setLastName(customer.getLastName());
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User customerOAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            String fullName = customer.getFirstName() + " " + customer.getLastName();
            customerOAuth2User.setFullName(fullName);
        }
    }

    private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
        CustomerUserDetails userDetails = null;
        if (principal instanceof UsernamePasswordAuthenticationToken) {

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();

        } else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();

        }
        return userDetails;
    }

    @PostMapping("/reset_password")
    public String processRequestForm(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        try {
            customerService.updatePassword(token, password);
            model.addAttribute("message", "You have successfully changed your password");
            return "message";

        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "message";

        }

    }

}
