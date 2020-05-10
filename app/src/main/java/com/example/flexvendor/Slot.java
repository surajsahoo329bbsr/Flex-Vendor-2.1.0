package com.example.flexvendor;


import androidx.annotation.NonNull;

public class Slot {

    private String userMail;
    private String userId;
    private int slotFlag;
    private String showDate;
    private String showStartTime;
    private String showWorkHours;

    public Slot() {

    }


    Slot(String userMail, String userId, int slotFlag, String showDate, String showStartTime, String showWorkHours) {

        this.userMail=userMail;
        this.userId=userId;
        this.slotFlag=slotFlag;
        this.showDate=showDate;
        this.showStartTime=showStartTime;
        this.showWorkHours=showWorkHours;

    }

    public String getUserMail() {
        return userMail;
    }

    public String getUserId() {
        return userId;
    }

    public int getSlotFlag() {
        return slotFlag;
    }

    public String getShowDate() {
        return showDate;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public String getShowWorkHours() {
        return showWorkHours;
    }




    @NonNull
    @Override
    public String toString() {

        char[] dateArr= showDate.toCharArray();
        char[] modDateArr=new char[showDate.length()];
        int count=0;

        for (int i=0; i < dateArr.length; i++) {
            if (dateArr[i] == '-')
                count++;
            if (count == 2)
                break;

            modDateArr[i]=dateArr[i];
        }

        String modTime = String.valueOf(modDateArr);
        return modTime + ", " + showStartTime + " | " + showWorkHours;
    }


}

