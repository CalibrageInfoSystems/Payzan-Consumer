package com.calibrage.payzanconsumer.framework.model;

/**
 * Created by Calibrage25 on 12/30/2017.
 */

public class SupportModel {


    public SupportModel(String heading, String subHeading) {
        this.heading = heading;
        this.subHeading = subHeading;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    private String heading;

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    private String subHeading;


}
