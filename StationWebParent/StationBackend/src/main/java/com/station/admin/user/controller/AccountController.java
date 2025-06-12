package com.station.admin.user.controller;


import com.station.admin.FileUploadUtil;
import com.station.admin.security.CustomUserDetails;
import com.station.admin.user.UserService;
import com.station.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails() {
        return "users/account_form";
    }


    @PostMapping("/account/update")
    public String saveDetails(User user, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal CustomUserDetails loggedInUser,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);

            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            String photoPath = FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            savedUser.setPhotos(photoPath);
            userService.updateAccount(savedUser);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }
        loggedInUser.setFirstName(user.getFirstName());
        loggedInUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "User updated successfully");
        return "redirect:/account";
    }


}
