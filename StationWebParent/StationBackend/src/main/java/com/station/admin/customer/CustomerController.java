package com.station.admin.customer;

import com.station.admin.FileUploadUtil;
import com.station.common.entity.Customer;
import com.station.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String listFirstPage(Model model) {
        return listByPage(model, 1, "firstName", "asc", null);
    }

    private String listByPage(Model model,
                              @PathVariable(name = "pageNumber") int currentPage,
                              @Param("sortField") String sortField,
                              @Param("sortDir") String sortDir,
                              @Param("keyword") String keyword
    ) {

        Page<Customer> page = customerService.listByPage(currentPage, sortField, sortDir, keyword);

        model.addAttribute("listCustomers", page.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(
            @PathVariable("id") Integer id,
            @PathVariable("status") boolean enabled,
            RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = String.format("Customer ID %d has been %s", id, status);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }

    @GetMapping("/customers/details/{id}")
    public String viewCustomerDetail(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) throws CustomerNotFoundException {
        try {
            Customer customer = customerService.get(id);
            model.addAttribute("customer", customer);
            return "customers/customer_detail_modal";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }

    }

    @GetMapping("/customers/edit/{id}")
    public String updateCustomer(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) throws CustomerNotFoundException {
        try {
            Customer customer = customerService.get(id);
            model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
            model.addAttribute("customer", customer);
            return "customers/customer_form";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }

    }


    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            customerService.delete(id);
            String customerImagesDir = "customer-images/" + id;
            FileUploadUtil.cleanDir(customerImagesDir);
            redirectAttributes.addFlashAttribute("message", "The Customer ID " + id + "has been deleted successfully");

        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/customers";
    }


    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, RedirectAttributes ra) {
        customerService.save(customer);
        ra.addFlashAttribute("message", "Customer saved successfully.");
        return "redirect:/customers";
    }
}
