package com.calibrage.payzanconsumer.framework.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Calibrage11 on 10/31/2017.
 */


    public class SendMoneyModel {

        @SerializedName("userWalletHistory")
        @Expose
        private UserWalletHistory userWalletHistory;
        @SerializedName("recieverUserName")
        @Expose
        private String recieverUserName;

        public UserWalletHistory getUserWalletHistory() {
            return userWalletHistory;
        }

        public void setUserWalletHistory(UserWalletHistory userWalletHistory) {
            this.userWalletHistory = userWalletHistory;
        }

        public String getRecieverUserName() {
            return recieverUserName;
        }

        public void setRecieverUserName(String recieverUserName) {
            this.recieverUserName = recieverUserName;
        }




    }

