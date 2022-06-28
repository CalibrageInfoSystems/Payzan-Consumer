package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Calibrage11 on 11/8/2017.
 */

public class ChangePasswordResponseModel {
    @SerializedName("Result")
    @Expose
    private Result result;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("AffectedRecords")
    @Expose
    private Integer affectedRecords;
    @SerializedName("EndUserMessage")
    @Expose
    private String endUserMessage;
    @SerializedName("ValidationErrors")
    @Expose
    private List<Object> validationErrors = null;
    @SerializedName("Exception")
    @Expose
    private Object exception;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Integer getAffectedRecords() {
        return affectedRecords;
    }

    public void setAffectedRecords(Integer affectedRecords) {
        this.affectedRecords = affectedRecords;
    }

    public String getEndUserMessage() {
        return endUserMessage;
    }

    public void setEndUserMessage(String endUserMessage) {
        this.endUserMessage = endUserMessage;
    }

    public List<Object> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<Object> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }


    public class Result {

        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("NormalizedUserName")
        @Expose
        private String normalizedUserName;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("NormalizedEmail")
        @Expose
        private String normalizedEmail;
        @SerializedName("EmailConfirmed")
        @Expose
        private Boolean emailConfirmed;
        @SerializedName("PasswordHash")
        @Expose
        private String passwordHash;
        @SerializedName("SecurityStamp")
        @Expose
        private String securityStamp;
        @SerializedName("ConcurrencyStamp")
        @Expose
        private String concurrencyStamp;
        @SerializedName("PhoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("PhoneNumberConfirmed")
        @Expose
        private Boolean phoneNumberConfirmed;
        @SerializedName("TwoFactorEnabled")
        @Expose
        private Boolean twoFactorEnabled;
        @SerializedName("LockoutEnd")
        @Expose
        private Object lockoutEnd;
        @SerializedName("LockoutEnabled")
        @Expose
        private Boolean lockoutEnabled;
        @SerializedName("AccessFailedCount")
        @Expose
        private Integer accessFailedCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNormalizedUserName() {
            return normalizedUserName;
        }

        public void setNormalizedUserName(String normalizedUserName) {
            this.normalizedUserName = normalizedUserName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNormalizedEmail() {
            return normalizedEmail;
        }

        public void setNormalizedEmail(String normalizedEmail) {
            this.normalizedEmail = normalizedEmail;
        }

        public Boolean getEmailConfirmed() {
            return emailConfirmed;
        }

        public void setEmailConfirmed(Boolean emailConfirmed) {
            this.emailConfirmed = emailConfirmed;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }

        public String getSecurityStamp() {
            return securityStamp;
        }

        public void setSecurityStamp(String securityStamp) {
            this.securityStamp = securityStamp;
        }

        public String getConcurrencyStamp() {
            return concurrencyStamp;
        }

        public void setConcurrencyStamp(String concurrencyStamp) {
            this.concurrencyStamp = concurrencyStamp;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Boolean getPhoneNumberConfirmed() {
            return phoneNumberConfirmed;
        }

        public void setPhoneNumberConfirmed(Boolean phoneNumberConfirmed) {
            this.phoneNumberConfirmed = phoneNumberConfirmed;
        }

        public Boolean getTwoFactorEnabled() {
            return twoFactorEnabled;
        }

        public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
            this.twoFactorEnabled = twoFactorEnabled;
        }

        public Object getLockoutEnd() {
            return lockoutEnd;
        }

        public void setLockoutEnd(Object lockoutEnd) {
            this.lockoutEnd = lockoutEnd;
        }

        public Boolean getLockoutEnabled() {
            return lockoutEnabled;
        }

        public void setLockoutEnabled(Boolean lockoutEnabled) {
            this.lockoutEnabled = lockoutEnabled;
        }

        public Integer getAccessFailedCount() {
            return accessFailedCount;
        }

        public void setAccessFailedCount(Integer accessFailedCount) {
            this.accessFailedCount = accessFailedCount;
        }

    }
}
