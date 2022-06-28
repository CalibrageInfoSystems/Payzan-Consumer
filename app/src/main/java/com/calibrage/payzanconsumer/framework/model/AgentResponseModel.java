package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AgentResponseModel {

    @SerializedName("Result")
    @Expose
    private Object result;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("AffectedRecords")
    @Expose
    private Integer affectedRecords;
    @SerializedName("EndUserMessage")
    @Expose
    private String endUserMessage;

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    @SerializedName("ValidationErrors")
    @Expose
    private List<ValidationError> validationErrors = null;
    @SerializedName("Exception")
    @Expose
    private Exception exception;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
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
}


class Exception {

   @SerializedName("ClassName")
   @Expose
   private String className;
   @SerializedName("Message")
   @Expose
   private String message;
   @SerializedName("Data")
   @Expose
   private Object data;
   @SerializedName("InnerException")
   @Expose
   private InnerException innerException;
   @SerializedName("HelpURL")
   @Expose
   private Object helpURL;
   @SerializedName("StackTraceString")
   @Expose
   private String stackTraceString;
   @SerializedName("RemoteStackTraceString")
   @Expose
   private Object remoteStackTraceString;
   @SerializedName("RemoteStackIndex")
   @Expose
   private Integer remoteStackIndex;
   @SerializedName("ExceptionMethod")
   @Expose
   private Object exceptionMethod;
   @SerializedName("HResult")
   @Expose
   private Integer hResult;
   @SerializedName("Source")
   @Expose
   private String source;
   @SerializedName("WatsonBuckets")
   @Expose
   private Object watsonBuckets;

   public String getClassName() {
       return className;
   }

   public void setClassName(String className) {
       this.className = className;
   }

   public String getMessage() {
       return message;
   }

   public void setMessage(String message) {
       this.message = message;
   }

   public Object getData() {
       return data;
   }

   public void setData(Object data) {
       this.data = data;
   }

   public InnerException getInnerException() {
       return innerException;
   }

   public void setInnerException(InnerException innerException) {
       this.innerException = innerException;
   }

   public Object getHelpURL() {
       return helpURL;
   }

   public void setHelpURL(Object helpURL) {
       this.helpURL = helpURL;
   }

   public String getStackTraceString() {
       return stackTraceString;
   }

   public void setStackTraceString(String stackTraceString) {
       this.stackTraceString = stackTraceString;
   }

   public Object getRemoteStackTraceString() {
       return remoteStackTraceString;
   }

   public void setRemoteStackTraceString(Object remoteStackTraceString) {
       this.remoteStackTraceString = remoteStackTraceString;
   }

   public Integer getRemoteStackIndex() {
       return remoteStackIndex;
   }

   public void setRemoteStackIndex(Integer remoteStackIndex) {
       this.remoteStackIndex = remoteStackIndex;
   }

   public Object getExceptionMethod() {
       return exceptionMethod;
   }

   public void setExceptionMethod(Object exceptionMethod) {
       this.exceptionMethod = exceptionMethod;
   }

   public Integer getHResult() {
       return hResult;
   }

   public void setHResult(Integer hResult) {
       this.hResult = hResult;
   }

   public String getSource() {
       return source;
   }

   public void setSource(String source) {
       this.source = source;
   }

   public Object getWatsonBuckets() {
       return watsonBuckets;
   }

   public void setWatsonBuckets(Object watsonBuckets) {
       this.watsonBuckets = watsonBuckets;
   }

}

class InnerException {

   @SerializedName("ClassName")
   @Expose
   private String className;
   @SerializedName("Message")
   @Expose
   private String message;
   @SerializedName("Data")
   @Expose
   private Object data;
   @SerializedName("InnerException")
   @Expose
   private Object innerException;
   @SerializedName("HelpURL")
   @Expose
   private Object helpURL;
   @SerializedName("StackTraceString")
   @Expose
   private String stackTraceString;
   @SerializedName("RemoteStackTraceString")
   @Expose
   private Object remoteStackTraceString;
   @SerializedName("RemoteStackIndex")
   @Expose
   private Integer remoteStackIndex;
   @SerializedName("ExceptionMethod")
   @Expose
   private Object exceptionMethod;
   @SerializedName("HResult")
   @Expose
   private Integer hResult;
   @SerializedName("Source")
   @Expose
   private String source;
   @SerializedName("WatsonBuckets")
   @Expose
   private Object watsonBuckets;
   @SerializedName("Severity")
   @Expose
   private String severity;
   @SerializedName("SqlState")
   @Expose
   private String sqlState;
   @SerializedName("MessageText")
   @Expose
   private String messageText;
   @SerializedName("Detail")
   @Expose
   private String detail;
   @SerializedName("Hint")
   @Expose
   private Object hint;
   @SerializedName("Position")
   @Expose
   private Integer position;
   @SerializedName("InternalPosition")
   @Expose
   private Integer internalPosition;
   @SerializedName("InternalQuery")
   @Expose
   private Object internalQuery;
   @SerializedName("Where")
   @Expose
   private Object where;
   @SerializedName("SchemaName")
   @Expose
   private String schemaName;
   @SerializedName("TableName")
   @Expose
   private String tableName;
   @SerializedName("ColumnName")
   @Expose
   private Object columnName;
   @SerializedName("DataTypeName")
   @Expose
   private Object dataTypeName;
   @SerializedName("ConstraintName")
   @Expose
   private String constraintName;
   @SerializedName("File")
   @Expose
   private String file;
   @SerializedName("Line")
   @Expose
   private String line;
   @SerializedName("Routine")
   @Expose
   private String routine;

   public String getClassName() {
       return className;
   }

   public void setClassName(String className) {
       this.className = className;
   }

   public String getMessage() {
       return message;
   }

   public void setMessage(String message) {
       this.message = message;
   }

   public Object getData() {
       return data;
   }

   public void setData(Object data) {
       this.data = data;
   }

   public Object getInnerException() {
       return innerException;
   }

   public void setInnerException(Object innerException) {
       this.innerException = innerException;
   }

   public Object getHelpURL() {
       return helpURL;
   }

   public void setHelpURL(Object helpURL) {
       this.helpURL = helpURL;
   }

   public String getStackTraceString() {
       return stackTraceString;
   }

   public void setStackTraceString(String stackTraceString) {
       this.stackTraceString = stackTraceString;
   }

   public Object getRemoteStackTraceString() {
       return remoteStackTraceString;
   }

   public void setRemoteStackTraceString(Object remoteStackTraceString) {
       this.remoteStackTraceString = remoteStackTraceString;
   }

   public Integer getRemoteStackIndex() {
       return remoteStackIndex;
   }

   public void setRemoteStackIndex(Integer remoteStackIndex) {
       this.remoteStackIndex = remoteStackIndex;
   }

   public Object getExceptionMethod() {
       return exceptionMethod;
   }

   public void setExceptionMethod(Object exceptionMethod) {
       this.exceptionMethod = exceptionMethod;
   }

   public Integer getHResult() {
       return hResult;
   }

   public void setHResult(Integer hResult) {
       this.hResult = hResult;
   }

   public String getSource() {
       return source;
   }

   public void setSource(String source) {
       this.source = source;
   }

   public Object getWatsonBuckets() {
       return watsonBuckets;
   }

   public void setWatsonBuckets(Object watsonBuckets) {
       this.watsonBuckets = watsonBuckets;
   }

   public String getSeverity() {
       return severity;
   }

   public void setSeverity(String severity) {
       this.severity = severity;
   }

   public String getSqlState() {
       return sqlState;
   }

   public void setSqlState(String sqlState) {
       this.sqlState = sqlState;
   }

   public String getMessageText() {
       return messageText;
   }

   public void setMessageText(String messageText) {
       this.messageText = messageText;
   }

   public String getDetail() {
       return detail;
   }

   public void setDetail(String detail) {
       this.detail = detail;
   }

   public Object getHint() {
       return hint;
   }

   public void setHint(Object hint) {
       this.hint = hint;
   }

   public Integer getPosition() {
       return position;
   }

   public void setPosition(Integer position) {
       this.position = position;
   }

   public Integer getInternalPosition() {
       return internalPosition;
   }

   public void setInternalPosition(Integer internalPosition) {
       this.internalPosition = internalPosition;
   }

   public Object getInternalQuery() {
       return internalQuery;
   }

   public void setInternalQuery(Object internalQuery) {
       this.internalQuery = internalQuery;
   }

   public Object getWhere() {
       return where;
   }

   public void setWhere(Object where) {
       this.where = where;
   }

   public String getSchemaName() {
       return schemaName;
   }

   public void setSchemaName(String schemaName) {
       this.schemaName = schemaName;
   }

   public String getTableName() {
       return tableName;
   }

   public void setTableName(String tableName) {
       this.tableName = tableName;
   }

   public Object getColumnName() {
       return columnName;
   }

   public void setColumnName(Object columnName) {
       this.columnName = columnName;
   }

   public Object getDataTypeName() {
       return dataTypeName;
   }

   public void setDataTypeName(Object dataTypeName) {
       this.dataTypeName = dataTypeName;
   }

   public String getConstraintName() {
       return constraintName;
   }

   public void setConstraintName(String constraintName) {
       this.constraintName = constraintName;
   }

   public String getFile() {
       return file;
   }

   public void setFile(String file) {
       this.file = file;
   }

   public String getLine() {
       return line;
   }

   public void setLine(String line) {
       this.line = line;
   }

   public String getRoutine() {
       return routine;
   }

   public void setRoutine(String routine) {
       this.routine = routine;
   }

}