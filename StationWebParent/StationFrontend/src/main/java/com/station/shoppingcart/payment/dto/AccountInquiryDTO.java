package com.station.shoppingcart.payment.dto;

import lombok.Data;

@Data
public class AccountInquiryDTO {

    private String bfs_remitterAccNo;
    private String bfs_remitterBankId;
    private String bfs_bfsTxnId;
}
