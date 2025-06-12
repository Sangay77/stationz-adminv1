package com.station.shoppingcart.payment;

import lombok.Data;

@Data
public class DebitRequestDTO {

    private String bfs_remitterOtp;
    private String bfs_bfsTxnId;

}
