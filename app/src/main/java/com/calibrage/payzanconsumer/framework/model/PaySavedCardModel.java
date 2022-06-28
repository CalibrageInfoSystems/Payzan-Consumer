package com.calibrage.payzanconsumer.framework.model;

/**
 * Created by Calibrage11 on 12/22/2017.
 */

public class PaySavedCardModel {

    public String accountno;
    private boolean inActive;


    public PaySavedCardModel(String accountno,boolean inActive) {
        this.accountno = accountno;
        this.inActive = inActive;
    }


    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public boolean getInActive() {
        return inActive;
    }

    public void setInActive(boolean inActive) {
        this.inActive = inActive;
    }
}
