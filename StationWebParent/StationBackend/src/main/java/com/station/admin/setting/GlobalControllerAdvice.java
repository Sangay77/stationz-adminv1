package com.station.admin.setting;

import com.station.admin.security.CustomUserDetails;
import com.station.admin.user.UserService;
import com.station.common.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addLoggedInUser(Model model, @AuthenticationPrincipal CustomUserDetails loggedUser) {
        if (loggedUser != null) {
            String email = loggedUser.getUsername();
            User user = userService.getByEmail(email);
            model.addAttribute("user", user);
        }
    }
}
