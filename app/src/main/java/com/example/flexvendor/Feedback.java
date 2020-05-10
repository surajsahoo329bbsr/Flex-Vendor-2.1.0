package com.example.flexvendor;

public class Feedback {

    private String userId;
    private String userMail;
    private String feedback;
    private String rating;

    Feedback(String userId,String userMail,String rating,String feedback)
    {
        this.userId = userId;
        this.userMail = userMail;
        this.rating = rating;
        this.feedback = feedback;
    }

    public String getUserId() { return userId; }

    public String getUserMail()
    {
        return userMail;
    }

    public String getFeedback()
    {
        return feedback;
    }

    public String getRating()
    {
        return rating;
    }
}
