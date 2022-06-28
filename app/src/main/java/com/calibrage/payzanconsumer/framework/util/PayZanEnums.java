package com.calibrage.payzanconsumer.framework.util;

/**
 * Created by Calibrage19 on 24-10-2017.
 */

public class PayZanEnums {

    /* user related enums*/
    public enum UserEnum{
        noValue,
        LOGIN,
        NOTLOGIN,
        GUEST
    }
    public enum RoleIdEnum {
        Consumer("0a610882-32dc-4a14-8d6e-9b182ff4dc73"),
        Admin("a4555a6f-95a0-4780-a6d4-514faea04d79"),
        Admin_Executive("1742b2e4-4049-44f9-8424-ce90869c1279"),
        Sales_Representative("31cf880e-f94d-4820-819e-472a915f61c5"),
        Agent("aad76dec-3ca5-4231-8943-8cc519bfe6b1");

        private String Id;

        RoleIdEnum(String Id) {
            this.Id = Id;
        }

        public String RID() {
            return Id;
        }
    }
    public enum TransactionsEnum {
        Deposit("29"),
        WithDrawal("30"),
        Transfer("31");
        private String Id;

        TransactionsEnum(String Id) {
            this.Id = Id;
        }

        public String TID() {
            return Id;
        }
    }


}

