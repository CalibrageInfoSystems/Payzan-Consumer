package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Calibrage10 on 12/26/2017.
 */

public class UserProfileInfoResponseModel {
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
    private String exception;

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

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public class Result {
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("RoleIds")
        @Expose
        private String roleIds;
        @SerializedName("RoleNames")
        @Expose
        private String roleNames;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("GenderType")
        @Expose
        private String genderType;
        @SerializedName("EducationType")
        @Expose
        private String educationType;
        @SerializedName("VillageName")
        @Expose
        private String villageName;
        @SerializedName("MandalId")
        @Expose
        private Integer mandalId;
        @SerializedName("MandalName")
        @Expose
        private String mandalName;
        @SerializedName("DistrictId")
        @Expose
        private Integer districtId;
        @SerializedName("DistrictName")
        @Expose
        private String districtName;
        @SerializedName("ProvinceId")
        @Expose
        private Integer provinceId;
        @SerializedName("ProvinceName")
        @Expose
        private String provinceName;
        @SerializedName("CountryId")
        @Expose
        private String countryId;
        @SerializedName("CountryName")
        @Expose
        private String countryName;
        @SerializedName("ParentAspNetUserName")
        @Expose
        private String parentAspNetUserName;
        @SerializedName("ParentAspNetUserFirstName")
        @Expose
        private String parentAspNetUserFirstName;
        @SerializedName("ParentAspNetUserMidlleName")
        @Expose
        private String parentAspNetUserMidlleName;
        @SerializedName("ParentAspNetUserLastName")
        @Expose
        private String parentAspNetUserLastName;
        @SerializedName("PostCode")
        @Expose
        private Integer postCode;
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
        private String educationTypeId;
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

        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(String roleIds) {
            this.roleIds = roleIds;
        }

        public String getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGenderType() {
            return genderType;
        }

        public void setGenderType(String genderType) {
            this.genderType = genderType;
        }

        public String getEducationType() {
            return educationType;
        }

        public void setEducationType(String educationType) {
            this.educationType = educationType;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public Integer getMandalId() {
            return mandalId;
        }

        public void setMandalId(Integer mandalId) {
            this.mandalId = mandalId;
        }

        public String getMandalName() {
            return mandalName;
        }

        public void setMandalName(String mandalName) {
            this.mandalName = mandalName;
        }

        public Integer getDistrictId() {
            return districtId;
        }

        public void setDistrictId(Integer districtId) {
            this.districtId = districtId;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public Integer getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Integer provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getParentAspNetUserName() {
            return parentAspNetUserName;
        }

        public void setParentAspNetUserName(String parentAspNetUserName) {
            this.parentAspNetUserName = parentAspNetUserName;
        }

        public String getParentAspNetUserFirstName() {
            return parentAspNetUserFirstName;
        }

        public void setParentAspNetUserFirstName(String parentAspNetUserFirstName) {
            this.parentAspNetUserFirstName = parentAspNetUserFirstName;
        }

        public String getParentAspNetUserMidlleName() {
            return parentAspNetUserMidlleName;
        }

        public void setParentAspNetUserMidlleName(String parentAspNetUserMidlleName) {
            this.parentAspNetUserMidlleName = parentAspNetUserMidlleName;
        }

        public String getParentAspNetUserLastName() {
            return parentAspNetUserLastName;
        }

        public void setParentAspNetUserLastName(String parentAspNetUserLastName) {
            this.parentAspNetUserLastName = parentAspNetUserLastName;
        }

        public Integer getPostCode() {
            return postCode;
        }

        public void setPostCode(Integer postCode) {
            this.postCode = postCode;
        }

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

        public String getEducationTypeId() {
            return educationTypeId;
        }

        public void setEducationTypeId(String educationTypeId) {
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
