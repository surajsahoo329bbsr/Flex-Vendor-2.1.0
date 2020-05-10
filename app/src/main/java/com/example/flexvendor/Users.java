package com.example.flexvendor;

import androidx.annotation.NonNull;

public class Users {

    private String email;
    private String name;
    private String phone;
    private String timings;
    private String transactionDateTime;
    private String transactionMoney;
    private boolean isPaid;

    Users(String email,String name, String phone, String timings) {

        this.email = email;
        this.name = name;
        this.phone = phone;
        this.timings = timings;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings=timings;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTransactionMoney() {
        return transactionMoney;
    }

    public void setTransactionMoney(String transactionMoney) {
        this.transactionMoney = transactionMoney;
    }
    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }


    @NonNull
    @Override
    public String toString() {
        return name+ "\n"+phone+"\n" + timings;
    }
}
