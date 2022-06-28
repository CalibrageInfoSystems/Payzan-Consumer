package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserAddressModel {

    @SerializedName("ListResult")
    @Expose
    private List<ListResult> listResult = null;
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
    private List<ValidationError> validationErrors = null;
    @SerializedName("Exception")
    @Expose
    private Exception exception;

    public List<ListResult> getListResult() {
        return listResult;
    }

    public void setListResult(List<ListResult> listResult) {
        this.listResult = listResult;
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

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public class ListResult {

        @SerializedName("CountryName")
        @Expose
        private String countryName;
        @SerializedName("ProvinceName")
        @Expose
        private String provinceName;
        @SerializedName("DistrictName")
        @Expose
        private String districtName;
        @SerializedName("MandalName")
        @Expose
        private String mandalName;
        @SerializedName("VillageName")
        @Expose
        private String villageName;
        @SerializedName("PostCode")
        @Expose
        private Integer postCode;
        @SerializedName("ProvinceId")
        @Expose
        private Integer provinceId;
        @SerializedName("DistrictId")
        @Expose
        private Integer districtId;
        @SerializedName("MandalId")
        @Expose
        private Integer mandalId;
        @SerializedName("CountryId")
        @Expose
        private Integer countryId;
        @SerializedName("UpdatedBy")
        @Expose
        private UpdatedBy updatedBy;
        @SerializedName("Updated")
        @Expose
        private Updated updated;
        @SerializedName("AspNetUserId")
        @Expose
        private String aspNetUserId;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("AddressLine1")
        @Expose
        private String addressLine1;
        @SerializedName("AddressLine2")
        @Expose
        private String addressLine2;
        @SerializedName("Landmark")
        @Expose
        private String landmark;
        @SerializedName("MobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("VillageId")
        @Expose
        private Integer villageId;
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

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getMandalName() {
            return mandalName;
        }

        public void setMandalName(String mandalName) {
            this.mandalName = mandalName;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public Integer getPostCode() {
            return postCode;
        }

        public void setPostCode(Integer postCode) {
            this.postCode = postCode;
        }

        public Integer getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Integer provinceId) {
            this.provinceId = provinceId;
        }

        public Integer getDistrictId() {
            return districtId;
        }

        public void setDistrictId(Integer districtId) {
            this.districtId = districtId;
        }

        public Integer getMandalId() {
            return mandalId;
        }

        public void setMandalId(Integer mandalId) {
            this.mandalId = mandalId;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public UpdatedBy getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(UpdatedBy updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Updated getUpdated() {
            return updated;
        }

        public void setUpdated(Updated updated) {
            this.updated = updated;
        }

        public String getAspNetUserId() {
            return aspNetUserId;
        }

        public void setAspNetUserId(String aspNetUserId) {
            this.aspNetUserId = aspNetUserId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public Integer getVillageId() {
            return villageId;
        }

        public void setVillageId(Integer villageId) {
            this.villageId = villageId;
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

    public class ValidationError {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Description")
        @Expose
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public class Updated {

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
        private String lockoutEnd;
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

        public String getLockoutEnd() {
            return lockoutEnd;
        }

        public void setLockoutEnd(String lockoutEnd) {
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

    public class UpdatedBy {

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
        private String lockoutEnd;
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

        public String getLockoutEnd() {
            return lockoutEnd;
        }

        public void setLockoutEnd(String lockoutEnd) {
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

