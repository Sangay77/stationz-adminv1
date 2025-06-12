package com.station.admin.customer;

import com.station.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Customer c WHERE " +
            "CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', c.city, ' ', c.state) " +
            "LIKE %?1%")
    Page<Customer> findAll(String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    void updateEnabledStatus(Integer id,boolean enabled);


    @Query("SELECT c FROM Customer c WHERE c.email=?1")
    Customer findByEmail(String email);

    Long countById(Integer id);


}
