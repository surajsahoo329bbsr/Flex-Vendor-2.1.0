package com.example.flexvendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordFragment extends Fragment {

    static int resetPass=0;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private DatabaseReference dbRef, usrRef;
    private String checkEmail, uEmail;
    private String currentPass, confirmPass, newPass;
    View parentLayout;

    ChangePasswordFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View parentHolder=inflater.inflate(R.layout.fragment_change_password, container, false);

        parentLayout=getActivity().findViewById(android.R.id.content);

        etCurrentPassword=parentHolder.findViewById(R.id.edCurrentPassword);
        etNewPassword=parentHolder.findViewById(R.id.edNewPassword);
        etConfirmNewPassword=parentHolder.findViewById(R.id.edConfirmNewPassword);
        Button btnChangePassword=parentHolder.findViewById(R.id.ChangePassword);

        dbRef=FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usrRef=dbRef.child("Vendor");
                currentPass=etCurrentPassword.getText().toString();
                newPass=etNewPassword.getText().toString();
                confirmPass=etConfirmNewPassword.getText().toString();

                if (!newPass.equals(confirmPass) || newPass.length() == 0) {
                    Snackbar.make(parentLayout, "Passwords do not match or cannot be empty", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                } else if (confirmPass.length() < 8) {
                    Snackbar.make(parentLayout, "Password must be at least 8 characters", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                } else {

                    final ProgressDialog pd=ProgressDialog.show(getActivity(), "Changing password", "Please wait...", true);
                    ValueEventListener userListener=new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                uEmail=ds.child("vendorMail").getValue(String.class);

                                assert uEmail != null;
                                if (uEmail.equals(checkEmail)) {

                                    AuthCredential credential=EmailAuthProvider.getCredential(checkEmail, currentPass);

                                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                user.updatePassword(confirmPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            pd.dismiss();
                                                            resetPass=1;
                                                            Intent intent=new Intent(getActivity(), MainActivity.class);
                                                            intent.putExtra("openProfile", true);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                            startActivity(intent);

                                                        } else {

                                                            pd.dismiss();

                                                        }
                                                    }


                                                });
                                            } else {
                                                pd.dismiss();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            pd.dismiss();
                                            Snackbar.make(parentLayout, "Old password and current password do not match", Snackbar.LENGTH_LONG)
                                                    .setDuration(3000)
                                                    .setAction("Close", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                        }
                                                    })
                                                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                    .show();

                                        }
                                    });

                                    break;
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                        }
                    };

                    usrRef.addListenerForSingleValueEvent(userListener);


                }
            }
        });

        return parentHolder;
    }
}
