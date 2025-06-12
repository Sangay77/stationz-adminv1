package com.station.customer;

import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import com.station.setting.EmailSettingBag;
import com.station.setting.SettingService;
import com.station.utility.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    private final CustomerService customerService;
    private final SettingService settingService;

    public ForgotPasswordController(CustomerService customerService, SettingService settingService) {
        this.customerService = customerService;
        this.settingService = settingService;
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) throws MessagingException {
        String email = request.getParameter("email");
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteUrl(request) + "/reset_password?token=" + token;
            try {
                sendEmail(link, email);
                model.addAttribute("message", "We have have send reset password link to your email, Please check");
            } catch (UnsupportedEncodingException e) {
                model.addAttribute("error", e.getMessage());
            }
        } catch (CustomerNotFoundException | MessagingException e) {
            model.addAttribute("error", "Could not send email");
        }
        return "login";
    }

    private void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = email;
        String subject = "Here is the link to reset your password";

        String content = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <p>Dear User,</p>
                    <p>We received a request to reset your password. Click the button below:</p>
                    <a href="%s" style="display: inline-block; background-color: #007bff; color: white; padding: 10px 20px;
                    text-decoration: none; border-radius: 5px; margin-top: 10px;">Reset Password</a>
                    <p>If you did not request this, please ignore this email.</p>
                    <p>Thanks,<br>The Support Team</p>
                </body>
                </html>
                """, link);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);  // true to indicate HTML content
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model) {
        Customer customer = customerService.getByResetPasswordToken(token);
        if (customer!=null){
            model.addAttribute("token",token);
        }else {
            model.addAttribute("message","Invalid Token");
            return "message";
        }
        return "customer/reset_password_form";
    }


}
