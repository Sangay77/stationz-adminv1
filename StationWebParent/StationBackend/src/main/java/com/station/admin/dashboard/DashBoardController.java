package com.station.admin.dashboard;

import com.station.admin.customer.CustomerRepository;
import com.station.admin.customer.CustomerService;
import com.station.admin.product.ProductService;
import com.station.admin.user.UserService;
import com.station.common.entity.Customer;
import com.station.common.entity.Product;
import com.station.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import org.springframework.ui.Model;


@Controller
public class DashBoardController {

    @Autowired private UserService userService;
    @Autowired private DashBoardService dashBoardService;

    @GetMapping("/")
    public String adminDashBoard(Model model) {
        List<User> users = userService.users();

        List<Map<String, Object>> comments = new ArrayList<>();

        Map<String, Object> comment1 = new HashMap<>();
        comment1.put("customerName", "Pema Wangmo");
        comment1.put("message", "Great product quality!");
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        comment1.put("date", date);
        comment1.put("customerPhoto", "/images/img_1.png");

        Map<String, Object> comment2 = new HashMap<>();
        comment2.put("customerName", "Sonam Dorji");
        comment2.put("message", "Fast delivery and good packaging.");
        LocalDateTime now2 = LocalDateTime.now();
        Date date2 = Date.from(now2.atZone(ZoneId.systemDefault()).toInstant());
        comment2.put("date", date2);
        comment2.put("customerPhoto", "/images/img_2.png");

        Map<String, Object> comment3 = new HashMap<>();
        comment3.put("customerName", "Karma Choden");
        comment3.put("message", "Customer service was very helpful!");
        LocalDateTime now1 = LocalDateTime.now();
        Date date1 = Date.from(now1.atZone(ZoneId.systemDefault()).toInstant());
        comment3.put("date", date1);
        comment3.put("customerPhoto", "/images/img_3.png");

        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);

        model.addAttribute("comments", comments);


        int user_count = users.size();
        List<Product> products = dashBoardService.getProducts();
        int product_count = products.size();

        List<Customer> customers = dashBoardService.getCustomers();
        int customers_count = customers.size();

        int category_count = dashBoardService.getCategories().size();


        model.addAttribute("totalUsers",user_count);
        model.addAttribute("totalProducts",product_count);
        model.addAttribute("totalCustomers",customers_count);
        model.addAttribute("totalCategories",category_count);

        return "dashboard/index";
    }

}
