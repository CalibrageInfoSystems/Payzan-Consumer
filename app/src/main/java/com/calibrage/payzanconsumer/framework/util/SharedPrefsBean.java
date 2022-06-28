package com.calibrage.payzanconsumer.framework.util;

/**
 * Created by Calibrage19 on 24-10-2017.
 */

class SharedPrefsBean { private String key;
    private String valueString;
    private int valueInt;
    private boolean isInt = false;

    public SharedPrefsBean(String key, String valueString, int valueInt, boolean isInt){
        this.key = key;
        this.valueString = valueString;
        this.valueInt = valueInt ;
        this.isInt = isInt;
    }

    public boolean getIsInt() {
        return isInt;
    }

    public void setIsInt(boolean isInt) {
        this.isInt = isInt;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }
}
