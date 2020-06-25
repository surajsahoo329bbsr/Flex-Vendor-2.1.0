package com.example.flexvendor;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {


    static int updateFlag = 0;
    private String id;
    private View parentLayout;
    private EditText etName, etPhone;
    private DatabaseReference updateRef;
    private String checkEmail, uEmail, updateName, updatePhone;
    private boolean onceChecked = false;

    public EditFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parentHolder=inflater.inflate(R.layout.fragment_edit, container, false);

        etName=parentHolder.findViewById(R.id.edName);
        etPhone=parentHolder.findViewById(R.id.edPhone);
        Button btnConfirm=parentHolder.findViewById(R.id.btnEditProfile);
        parentLayout=Objects.requireNonNull(getActivity()).findViewById(android.R.id.content);

        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usrRef = dbRef.child("Vendor");
        updateRef=dbRef.child("Vendor");

        if (!onceChecked) {

            ValueEventListener userListener=new ValueEventListener() {

                final ProgressDialog pd=ProgressDialog.show(getActivity(), "Fetching Data", "Hang on...", true);

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        uEmail = ds.child("vendorMail").getValue(String.class);
                        assert uEmail != null;
                        if (uEmail.equals(checkEmail)) {

                            pd.dismiss();
                            String getName = ds.child("vendorName").getValue(String.class);
                            String getPhone = ds.child("vendorPhone").getValue(String.class);
                            etName.setText(getName);
                            etPhone.setText(getPhone);
                            break;
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    pd.dismiss();
                    Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                }
            };

            usrRef.addListenerForSingleValueEvent(userListener);
            onceChecked=true;
        }


        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                updateName=etName.getText().toString();
                updatePhone=etPhone.getText().toString();


                if (updatePhone.length() == 0 || updateName.length() == 0) {

                    Snackbar.make(parentLayout, "Please enter your name and phone number", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                } else if (updatePhone.length() != 10) {

                    Snackbar.make(parentLayout, "Please enter a valid phone number", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                } else {

                    final ProgressDialog pd=ProgressDialog.show(getActivity(), "Updating", "Hang on...", true);
                    updateRef=dbRef.child("Vendor");

                    ValueEventListener userListener=new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                uEmail = ds.child("vendorMail").getValue(String.class);

                                assert uEmail != null;
                                if (uEmail.equals(checkEmail)) {

                                    pd.dismiss();
                                    id = ds.child("vendorId").getValue(String.class);
                                    assert id != null;
                                    updateRef.child(id).child("vendorName").setValue(updateName);
                                    updateRef.child(id).child("vendorPhone").setValue(updatePhone);
                                    updateFlag = 1;
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("openProfile", true);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    break;
                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            pd.dismiss();
                            Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                        }
                    };

                    updateRef.addListenerForSingleValueEvent(userListener);

                }

            }
        });


        return parentHolder;
    }

}
