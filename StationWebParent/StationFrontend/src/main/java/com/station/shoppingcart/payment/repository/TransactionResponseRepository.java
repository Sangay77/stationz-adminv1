package com.station.shoppingcart.payment.repository;

import com.station.common.entity.TransactionResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionResponseRepository extends JpaRepository<TransactionResponse, Long> {
}

