package com.example.flexvendor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmailPhone, etPassword;
    private FirebaseAuth auth;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, android.R.color.background_light));// set status background white
        }

        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        etEmailPhone=findViewById(R.id.etEmailPhone);
        etPassword=findViewById(R.id.etPassword);
        Button btnLogin=findViewById(R.id.btnProfile);
        TextView tvForgotPassword=findViewById(R.id.tvForgotPassword);
        TextView tvRegister=findViewById(R.id.tvRegister);
        parentLayout = findViewById(android.R.id.content);

        auth = FirebaseAuth.getInstance();

        if(DeleteAccountActivity.deleteAccountFlag == 1)
        {
            Snackbar.make(parentLayout,"Account deleted", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            DeleteAccountActivity.deleteAccountFlag=0;

        }

        if(RegisterActivity.registerFlag == 1)
        {
            Snackbar.make(parentLayout,"Account created", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            RegisterActivity.registerFlag = 0;

        }

        if (ForgotPasswordActivity.sentMailFlag == 1)
        {
            Snackbar.make(parentLayout,"Please check your email", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            ForgotPasswordActivity.sentMailFlag=0;

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailPhone = etEmailPhone.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();

                if(emailPhone.length()==0)
                {
                    Snackbar.make(parentLayout,"Please enter your email", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                    etEmailPhone.requestFocus();
                }
                else if(password.length()==0)
                {
                    Snackbar.make(parentLayout,"Please enter your password", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();
                    etPassword.requestFocus();
                }
                else
                {
                    final ProgressDialog pd =ProgressDialog.show(LoginActivity.this,"Verifying Credentials","Please wait...",true);

                    auth.signInWithEmailAndPassword(emailPhone, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {

                                        pd.dismiss();
                                        Snackbar.make(parentLayout,"Invalid Credentials", Snackbar.LENGTH_LONG)
                                                .setDuration(3000)
                                                .setAction("Close", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                .show();

                                    } else {

                                        pd.dismiss();
                                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("openSlots", true);
                                        overridePendingTransition(0, 0);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        finish();
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });
                }

            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(it);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_round)
                .setTitle("Closing App")
                .setMessage("Do you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent it = new Intent(Intent.ACTION_MAIN);
                        it.addCategory(Intent.CATEGORY_HOME);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);

                    }
                })
                .setNegativeButton("No",null).show();
    }



}
