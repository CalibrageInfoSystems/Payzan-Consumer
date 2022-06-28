package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Calibrage11 on 11/8/2017.
 */

public class ChangePasswordModel {


    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("OldPassword")
    @Expose
    private String oldPassword;
    @SerializedName("NewPassword")
    @Expose
    private String newPassword;
    @SerializedName("ConfirmPassword")
    @Expose
    private String confirmPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
