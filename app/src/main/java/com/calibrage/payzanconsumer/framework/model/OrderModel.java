package com.calibrage.payzanconsumer.framework.model;

/**
 * Created by Calibrage25 on 11/21/2017.
 */

public class OrderModel
{
    public  String DateTIme;
    public  String payment;
    public  String Isuscess;

    public OrderModel(String dateTIme, String payment, String isuscess) {
        DateTIme = dateTIme;
        this.payment = payment;
        Isuscess = isuscess;
    }

    public String getDateTIme() {
        return DateTIme;
    }

    public void setDateTIme(String dateTIme) {
        DateTIme = dateTIme;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getIsuscess() {
        return Isuscess;
    }

    public void setIsuscess(String isuscess) {
        Isuscess = isuscess;
    }
}
