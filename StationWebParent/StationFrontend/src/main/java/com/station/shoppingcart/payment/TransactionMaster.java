package com.station.shoppingcart.payment;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;


@Entity
@Component
public class TransactionMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accountNumber;
    private String bfs_msgType;
    private String bfs_benfBankCode;
    private String bfs_benfId;
    private String bfs_benfTxnTime;
    private String bfs_orderNo;
    @Transient
    private String bfs_txnCurrency;
    private String bfs_txnAmount;
    @Transient
    private String bfs_paymentDesc;
    @Transient
    private String bfs_checkSum;
    @Transient
    private String bfs_remitterEmail;
    @Transient
    private String bfs_version;
    private String bfs_remitterBankId;
    @Column(name = "BFS_TXN_ID")
    private String bfs_bfsTxnId;
    private String bfs_remitterOtp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBfs_msgType() {
        return bfs_msgType;
    }

    public void setBfs_msgType(String bfs_msgType) {
        this.bfs_msgType = bfs_msgType;
    }

    public String getBfs_benfBankCode() {
        return bfs_benfBankCode;
    }

    public void setBfs_benfBankCode(String bfs_benfBankCode) {
        this.bfs_benfBankCode = bfs_benfBankCode;
    }

    public String getBfs_benfId() {
        return bfs_benfId;
    }

    public void setBfs_benfId(String bfs_benfId) {
        this.bfs_benfId = bfs_benfId;
    }

    public String getBfs_benfTxnTime() {
        return bfs_benfTxnTime;
    }

    public void setBfs_benfTxnTime(String bfs_benfTxnTime) {
        this.bfs_benfTxnTime = bfs_benfTxnTime;
    }

    public String getBfs_orderNo() {
        return bfs_orderNo;
    }

    public void setBfs_orderNo(String bfs_orderNo) {
        this.bfs_orderNo = bfs_orderNo;
    }

    public String getBfs_txnCurrency() {
        return bfs_txnCurrency;
    }

    public void setBfs_txnCurrency(String bfs_txnCurrency) {
        this.bfs_txnCurrency = bfs_txnCurrency;
    }

    public String getBfs_txnAmount() {
        return bfs_txnAmount;
    }

    public void setBfs_txnAmount(String bfs_txnAmount) {
        this.bfs_txnAmount = bfs_txnAmount;
    }

    public String getBfs_paymentDesc() {
        return bfs_paymentDesc;
    }

    public void setBfs_paymentDesc(String bfs_paymentDesc) {
        this.bfs_paymentDesc = bfs_paymentDesc;
    }

    public String getBfs_checkSum() {
        return bfs_checkSum;
    }

    public void setBfs_checkSum(String bfs_checkSum) {
        this.bfs_checkSum = bfs_checkSum;
    }

    public String getBfs_remitterEmail() {
        return bfs_remitterEmail;
    }

    public void setBfs_remitterEmail(String bfs_remitterEmail) {
        this.bfs_remitterEmail = bfs_remitterEmail;
    }

    public String getBfs_version() {
        return bfs_version;
    }

    public void setBfs_version(String bfs_version) {
        this.bfs_version = bfs_version;
    }

    public String getBfs_remitterBankId() {
        return bfs_remitterBankId;
    }

    public void setBfs_remitterBankId(String bfs_remitterBankId) {
        this.bfs_remitterBankId = bfs_remitterBankId;
    }

    public String getBfs_bfsTxnId() {
        return bfs_bfsTxnId;
    }

    public void setBfs_bfsTxnId(String bfs_bfsTxnId) {
        this.bfs_bfsTxnId = bfs_bfsTxnId;
    }

    public String getBfs_remitterOtp() {
        return bfs_remitterOtp;
    }

    public void setBfs_remitterOtp(String bfs_remitterOtp) {
        this.bfs_remitterOtp = bfs_remitterOtp;
    }

    @Override
    public String toString() {
        return "TransactionMaster{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", bfs_msgType='" + bfs_msgType + '\'' +
                ", bfs_benfBankCode='" + bfs_benfBankCode + '\'' +
                ", bfs_benfId='" + bfs_benfId + '\'' +
                ", bfs_benfTxnTime='" + bfs_benfTxnTime + '\'' +
                ", bfs_orderNo='" + bfs_orderNo + '\'' +
                ", bfs_txnCurrency='" + bfs_txnCurrency + '\'' +
                ", bfs_txnAmount='" + bfs_txnAmount + '\'' +
                ", bfs_paymentDesc='" + bfs_paymentDesc + '\'' +
                ", bfs_checkSum='" + bfs_checkSum + '\'' +
                ", bfs_remitterEmail='" + bfs_remitterEmail + '\'' +
                ", bfs_version='" + bfs_version + '\'' +
                '}';
    }
}