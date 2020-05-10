package com.example.flexvendor;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {

    private View parentHolder;
    private TextView tvRating,tvComment,tvSetRating,tvSetComment;
    private int flag ;
    private EditText etFeedback;
    private String feedbackText="",checkEmail, uemail, feedbackRating = "0";
    private DatabaseReference dbRef;
    private DatabaseReference fdbRef;
    private View parentLayout;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final Activity referenceActivity=getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_feedback, container,
                false);
        assert referenceActivity != null;
        parentLayout = referenceActivity.findViewById(android.R.id.content);

        Button btnFeedback=parentHolder.findViewById(R.id.btnFeedback);
        Button btnGetFeedback=parentHolder.findViewById(R.id.btnGetFeedback);
        etFeedback=parentHolder.findViewById(R.id.etFeedback);
        tvSetComment=parentHolder.findViewById(R.id.tvSetComment);
        tvSetRating=parentHolder.findViewById(R.id.tvSetRating);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();
        dbRef =FirebaseDatabase.getInstance().getReference();

        addListenerOnRatingBar(parentHolder);


        btnGetFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = ProgressDialog.show(referenceActivity,"Getting Feedback","Please wait...",true);

                fdbRef = dbRef.child("Feedback");

                ValueEventListener feedbackListener = new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            uemail = ds.child("userMail").getValue(String.class);

                            assert uemail != null;
                            if (uemail.equals(checkEmail)) {

                                TextView tvReviewHeading=parentHolder.findViewById(R.id.tvRatingTitle);
                                LinearLayout linearLayout=parentLayout.findViewById(R.id.linearLayoutFB);


                                String getRating = ds.child("rating").getValue(String.class);
                                String getFeedback = ds.child("feedback").getValue(String.class);

                                assert getRating != null;
                                if(!getRating.equals("0")) {
                                    double stars = Double.parseDouble(getRating);

                                    linearLayout.setVisibility(View.VISIBLE);
                                    tvReviewHeading.setVisibility(View.VISIBLE);

                                    if(stars != 0.0) {
                                        NumberFormat nf = new DecimalFormat("#.####");
                                        String strStars = nf.format(stars);
                                        tvSetRating.setText("Rating : "+strStars+" / 5");
                                        tvSetRating.setVisibility(View.VISIBLE);

                                    } else {
                                        tvSetRating.setText("Rating : 1 / 5");
                                        tvSetRating.setVisibility(View.VISIBLE);
                                    }

                                    Snackbar.make(parentLayout,"Please scroll down to view your feedback", Snackbar.LENGTH_LONG)
                                            .setDuration(3000)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                            .show();

                                    pd.dismiss();


                                } else {

                                    Snackbar.make(parentLayout,"Please giving rating first", Snackbar.LENGTH_LONG)
                                            .setDuration(3000)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                            .show();

                                    pd.dismiss();


                                }

                                assert getFeedback != null;
                                if(!getFeedback.equals("")) {
                                    tvSetComment.setText(getFeedback);
                                    tvSetComment.setVisibility(View.VISIBLE);
                                }


                                break;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        pd.dismiss();

                    }
                };

                fdbRef.addListenerForSingleValueEvent(feedbackListener);

                pd.dismiss();


            }
        });


        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fdbRef = dbRef.child("Feedback");

                if(flag == 1) {

                    final ProgressDialog pd = ProgressDialog.show(referenceActivity,"Sending Feedback","Please wait...",true);

                    feedbackText = etFeedback.getText().toString();

                    ValueEventListener feedbackListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                uemail = ds.child("userMail").getValue(String.class);

                                assert uemail != null;
                                if (uemail.equals(checkEmail)) {

                                    String id = ds.child("userId").getValue(String.class);
                                    assert id != null;
                                    fdbRef.child(id).child("feedback").setValue(feedbackText);
                                    fdbRef.child(id).child("rating").setValue(feedbackRating);

                                    Snackbar.make(parentLayout,"Thank you for your feedback", Snackbar.LENGTH_LONG)
                                            .setDuration(3000)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                            .show();

                                    pd.dismiss();

                                    break;
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            pd.dismiss();

                        }
                    };

                    fdbRef.addListenerForSingleValueEvent(feedbackListener);

                } else {
                    Snackbar.make(parentLayout,"Please giving rating", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();


                }

            }
        });


        return parentHolder;

    }



     private void addListenerOnRatingBar(View view) {

         RatingBar rb=parentHolder.findViewById(R.id.ratingBar);
         tvRating=view.findViewById(R.id.tvRating);
         tvComment=view.findViewById(R.id.tvComment);

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @SuppressLint("SetTextI18n")
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                if(rating < 1.0f)
                    ratingBar.setRating(1.0f);

                Double stars =(double) rating;

                NumberFormat nf = new DecimalFormat("#.####");
                String strStars = nf.format(stars);


                if(String.valueOf(rating).equals("1.0") || String.valueOf(rating).equals("1.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("Ohh ! Please give feedback.");

                } else if(String.valueOf(rating).equals("2.0") || String.valueOf(rating).equals("2.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("That's poor. Please give feedback.");
                } else if(String.valueOf(rating).equals("3.0") || String.valueOf(rating).equals("3.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("Not Good, Not Terrible. Please give feedback.");
                } else if(String.valueOf(rating).equals("4.0") || String.valueOf(rating).equals("4.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("Thank you ! Please give feedback.");
                } else if(String.valueOf(rating).equals("5.0")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("We are pleased. Please give feedback and suggestions if any.");
                } else {

                    tvRating.setText("Your Rating : 1 / 5");
                    tvComment.setText("Ohh ! Please give feedback.");

                }

                tvRating.setVisibility(View.VISIBLE);
                tvComment.setVisibility(View.VISIBLE);

                feedbackRating = String.valueOf(rating);
                flag = 1;

            }
        });
    }

}
