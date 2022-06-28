package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Calibrage11 on 10/14/2017.
 */

public class PostWalletModel {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("WalletId")
    @Expose
    private String walletId;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("TransactionTypeId")
    @Expose
    private Integer transactionTypeId;
    @SerializedName("ReasonTypeId")
    @Expose
    private Integer reasonTypeId;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("NameOnCard")
    @Expose
    private String nameOnCard;
    @SerializedName("CardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("CardExpiry")
    @Expose
    private String cardExpiry;
    @SerializedName("CVV")
    @Expose
    private Integer cVV;
    @SerializedName("Currency")
    @Expose
    private String currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public Integer getReasonTypeId() {
        return reasonTypeId;
    }

    public void setReasonTypeId(Integer reasonTypeId) {
        this.reasonTypeId = reasonTypeId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public Integer getCVV() {
        return cVV;
    }

    public void setCVV(Integer cVV) {
        this.cVV = cVV;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
