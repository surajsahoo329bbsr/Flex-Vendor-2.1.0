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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private TextView textView;
    private View parentLayout;
    static int sentMailFlag = 0;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(ForgotPasswordActivity.this, android.R.color.background_light));// set status background white
        }

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);


        etEmail=findViewById(R.id.etEmail);
        Button btnResetPassword=findViewById(R.id.btnChPass);
        parentLayout=findViewById(android.R.id.content);
        textView=findViewById(R.id.tvTryAgain);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setVisibility(View.VISIBLE);


                FirebaseAuth.getInstance().sendPasswordResetEmail(etEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                ProgressDialog pd=ProgressDialog.show(ForgotPasswordActivity.this, "Sending email", "Please wait...", true);

                                if( task.isSuccessful()) {
                                    pd.dismiss();
                                    sentMailFlag = 1;
                                    Intent it=new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(it);
                                    finish();
                                } else {
                                    pd.dismiss();
                                    Snackbar.make(parentLayout,"Try again", Snackbar.LENGTH_LONG)
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
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }
}
