package com.calibrage.payzanconsumer.framework.model;

/**
 * Created by Calibrage11 on 9/18/2017.
 */

public class Contact {

    //private variables
    int _id;
    String _name;
    String _phone_number;
    private String amount;
    private String operator;
    private String operatorState;

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(int id, String name, String _phone_number, String amount, String operator, String operatorState){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this.amount = amount;
        this.operator = operator;
        this.operatorState = operatorState;
    }

    // constructor
    public Contact(String name, String _phone_number){
        this._name = name;
        this._phone_number = _phone_number;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }

    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorState() {
        return operatorState;
    }

    public void setOperatorState(String operatorState) {
        this.operatorState = operatorState;
    }
}