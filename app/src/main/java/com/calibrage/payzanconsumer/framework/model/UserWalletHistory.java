package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Calibrage11 on 10/31/2017.
 */


    public class UserWalletHistory {

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
        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("IsActive")
        @Expose
        private Boolean isActive;
        @SerializedName("CreatedBy")
        @Expose
        private String createdBy;
        @SerializedName("ModifiedBy")
        @Expose
        private String modifiedBy;
        @SerializedName("Created")
        @Expose
        private String created;
        @SerializedName("Modified")
        @Expose
        private String modified;

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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

    }

