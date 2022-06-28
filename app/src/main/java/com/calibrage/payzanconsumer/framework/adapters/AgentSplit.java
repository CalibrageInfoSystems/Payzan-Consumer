package com.calibrage.payzanconsumer.framework.adapters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Calibrage19 on 17-11-2017.
 */

public class AgentSplit {
    @SerializedName("ListResult")
    @Expose
    private List<AgentSplitResult> listResult = null;
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

    public List<AgentSplitResult> getListResult() {
        return listResult;
    }

    public void setListResult(List<AgentSplitResult> listResult) {
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

    public class AgentSplitResult {

        @SerializedName("ServiceTypeName")
        @Expose
        private String serviceTypeName;
        @SerializedName("ServiceProviderName")
        @Expose
        private String serviceProviderName;
        @SerializedName("AgentCategoryName")
        @Expose
        private String agentCategoryName;
        @SerializedName("AgentCategoryTypeId")
        @Expose
        private Integer agentCategoryTypeId;
        @SerializedName("ServiceTypeId")
        @Expose
        private Integer serviceTypeId;
        @SerializedName("ServiceProviderId")
        @Expose
        private Integer serviceProviderId;
        @SerializedName("PercentageSplit")
        @Expose
        private Integer percentageSplit;
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

        public String getServiceTypeName() {
            return serviceTypeName;
        }

        public void setServiceTypeName(String serviceTypeName) {
            this.serviceTypeName = serviceTypeName;
        }

        public String getServiceProviderName() {
            return serviceProviderName;
        }

        public void setServiceProviderName(String serviceProviderName) {
            this.serviceProviderName = serviceProviderName;
        }

        public String getAgentCategoryName() {
            return agentCategoryName;
        }

        public void setAgentCategoryName(String agentCategoryName) {
            this.agentCategoryName = agentCategoryName;
        }

        public Integer getAgentCategoryTypeId() {
            return agentCategoryTypeId;
        }

        public void setAgentCategoryTypeId(Integer agentCategoryTypeId) {
            this.agentCategoryTypeId = agentCategoryTypeId;
        }

        public Integer getServiceTypeId() {
            return serviceTypeId;
        }

        public void setServiceTypeId(Integer serviceTypeId) {
            this.serviceTypeId = serviceTypeId;
        }

        public Integer getServiceProviderId() {
            return serviceProviderId;
        }

        public void setServiceProviderId(Integer serviceProviderId) {
            this.serviceProviderId = serviceProviderId;
        }

        public Integer getPercentageSplit() {
            return percentageSplit;
        }

        public void setPercentageSplit(Integer percentageSplit) {
            this.percentageSplit = percentageSplit;
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







