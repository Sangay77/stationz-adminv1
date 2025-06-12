package com.station.admin.user.controller;

import com.station.admin.FileUploadUtil;
import com.station.admin.user.UserNotFoundException;
import com.station.admin.user.UserService;
import com.station.common.entity.Role;
import com.station.common.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        List<User> users = userService.users();
        return listByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                             @RequestParam(value = "keyword", required = false) String keyword) {

        Page<User> pageUser = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listUsers = pageUser.getContent();

        long startCount = ((long) (pageNum - 1)) * UserService.USER_PER_PAGE + 1;
        long endCount = startCount + UserService.USER_PER_PAGE - 1;
        if (endCount > pageUser.getTotalElements()) {
            endCount = pageUser.getTotalElements();
        }

        String reverseSortDir = sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageUser.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageUser.getTotalElements());
        model.addAttribute("userList", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "users/users";
    }


    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        List<Role> roleList = userService.roles();
        model.addAttribute("listRole", roleList);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Add New Team");
        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        boolean isNewImage = !multipartFile.isEmpty();

        if (isNewImage) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName); // Set initial file name
        } else if (user.getPhotos() == null || user.getPhotos().isEmpty()) {
            user.setPhotos(null);
        }
        User savedUser = userService.saveUser(user);

        if (isNewImage) {
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, savedUser.getPhotos(), multipartFile);
        }

        redirectAttributes.addFlashAttribute("message", "User added successfully");
        return getRedirectUrlToAffectedUser(savedUser);
    }


    private static String getRedirectUrlToAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }


    @GetMapping("/users/edit/{id}")
    public String updateUser(@PathVariable(name = "id") Integer id,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            User userById = userService.getUserById(id);
            List<Role> roles = userService.roles();
            model.addAttribute("pageTitle", "Update your Team with email: " + userById.getEmail());
            model.addAttribute("user", userById);
            model.addAttribute("listRole", roles);
            return "users/user_form";

        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + "has been deleted successfully");

        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(
            @PathVariable("id") Integer id,
            @PathVariable("status") boolean enabled,
            RedirectAttributes redirectAttributes) {

        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid user ID");
            }
            userService.updateUserEnabledStatus(id, enabled);

            String status = enabled ? "enabled" : "disabled";
            String message = String.format("User ID %d has been %s", id, status);
            redirectAttributes.addFlashAttribute("message", message);

        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "User not found with ID: " + id);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error updating user status: " + ex.getMessage());
        }

        return "redirect:/users";
    }
}
