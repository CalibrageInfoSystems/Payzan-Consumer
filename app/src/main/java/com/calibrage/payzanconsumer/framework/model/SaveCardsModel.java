package com.calibrage.payzanconsumer.framework.model;

/**
 * Created by Calibrage25 on 11/21/2017.
 */

public class SaveCardsModel {

    public String accountno;


    public SaveCardsModel(String accountno) {

        this.accountno = accountno;

    }


    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

}
