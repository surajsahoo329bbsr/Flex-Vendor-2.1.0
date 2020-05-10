package com.example.flexvendor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LicenseDetailsActivity extends AppCompatActivity {

    private View parentLayout;
    private TextView tvDLNumber, tvName, tvDOB, tvAddress, tvIssueDate, tvExpiryDate;
    private String checkMail="", umail="", dlNumber, dlName, DOB, address, issueDate, expiryDate;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_details);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        final ProgressDialog pd=ProgressDialog.show(LicenseDetailsActivity.this, "Loading License Details", "Please wait...", true);

        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();

        Intent it=getIntent();
        checkMail=it.getStringExtra("email");
        parentLayout=findViewById(android.R.id.content);

        tvDLNumber=findViewById(R.id.tvDLNumber);
        tvName=findViewById(R.id.tvDLName);
        tvDOB=findViewById(R.id.tvDOB);
        tvAddress=findViewById(R.id.tvAddress);
        tvIssueDate=findViewById(R.id.tvIssueDate);
        tvExpiryDate=findViewById(R.id.tvExpiryDate);


        DatabaseReference dlRef=dbRef.child("DrivingLicense");

        ValueEventListener dlListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    umail=ds.child("userMail").getValue(String.class);

                    assert umail != null;
                    if (umail.equals(checkMail)) {
                        dlNumber=ds.child("licenseNumber").getValue(String.class);
                        dlName=ds.child("userName").getValue(String.class);
                        DOB=ds.child("userDOB").getValue(String.class);
                        address=ds.child("userAddress").getValue(String.class);
                        issueDate=ds.child("licenseIssueDate").getValue(String.class);
                        expiryDate=ds.child("licenseExpiryDate").getValue(String.class);

                        break;
                    }
                }

                if (dlNumber != null && dlName != null && DOB != null && address != null && tvIssueDate != null && tvExpiryDate != null) {
                    tvDLNumber.setText(dlNumber);
                    tvName.setText(dlName);
                    tvDOB.setText(DOB);
                    tvAddress.setText(address);
                    tvIssueDate.setText(issueDate);
                    tvExpiryDate.setText(expiryDate);

                }

                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

                Snackbar.make(parentLayout, "Failed to load license details", Snackbar.LENGTH_LONG)
                        .setDuration(3000)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                        .show();


            }
        };

        dlRef.addListenerForSingleValueEvent(dlListener);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(LicenseDetailsActivity.this, MainActivity.class);
        intent.putExtra("openSlots", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(LicenseDetailsActivity.this, MainActivity.class);
        intent.putExtra("openSlots", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
}
