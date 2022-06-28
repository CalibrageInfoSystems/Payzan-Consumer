package com.calibrage.payzanconsumer.framework.model;

/**
 * Created by Calibrage11 on 12/22/2017.
 */

public class PayNetBankModel  {

    private   int images;
    private   boolean isActive;

    public  PayNetBankModel(int images,boolean isActive){
        this.images = images;
        this.isActive = isActive;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
