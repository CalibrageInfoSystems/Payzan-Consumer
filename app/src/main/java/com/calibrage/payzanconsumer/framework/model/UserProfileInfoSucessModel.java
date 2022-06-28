package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Calibrage10 on 12/27/2017.
 */

public class UserProfileInfoSucessModel {
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
    private List<ValidationError> validationErrors = null;
    @SerializedName("Exception")
    @Expose
    private Exception exception;

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

    public class Result {

        @SerializedName("AspNetUserId")
        @Expose
        private String aspNetUserId;
        @SerializedName("TitleTypeId")
        @Expose
        private Integer titleTypeId;
        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("MiddleName")
        @Expose
        private String middleName;
        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("Phone")
        @Expose
        private String phone;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("GenderTypeId")
        @Expose
        private Integer genderTypeId;
        @SerializedName("DOB")
        @Expose
        private String dOB;
        @SerializedName("Address1")
        @Expose
        private String address1;
        @SerializedName("Address2")
        @Expose
        private String address2;
        @SerializedName("Landmark")
        @Expose
        private String landmark;
        @SerializedName("VillageId")
        @Expose
        private Integer villageId;
        @SerializedName("ParentAspNetUserId")
        @Expose
        private String parentAspNetUserId;
        @SerializedName("EducationTypeId")
        @Expose
        private Integer educationTypeId;
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

        public String getAspNetUserId() {
            return aspNetUserId;
        }

        public void setAspNetUserId(String aspNetUserId) {
            this.aspNetUserId = aspNetUserId;
        }

        public Integer getTitleTypeId() {
            return titleTypeId;
        }

        public void setTitleTypeId(Integer titleTypeId) {
            this.titleTypeId = titleTypeId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getGenderTypeId() {
            return genderTypeId;
        }

        public void setGenderTypeId(Integer genderTypeId) {
            this.genderTypeId = genderTypeId;
        }

        public String getDOB() {
            return dOB;
        }

        public void setDOB(String dOB) {
            this.dOB = dOB;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public Integer getVillageId() {
            return villageId;
        }

        public void setVillageId(Integer villageId) {
            this.villageId = villageId;
        }

        public String getParentAspNetUserId() {
            return parentAspNetUserId;
        }

        public void setParentAspNetUserId(String parentAspNetUserId) {
            this.parentAspNetUserId = parentAspNetUserId;
        }

        public Integer getEducationTypeId() {
            return educationTypeId;
        }

        public void setEducationTypeId(Integer educationTypeId) {
            this.educationTypeId = educationTypeId;
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
}