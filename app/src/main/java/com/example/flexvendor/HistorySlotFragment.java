package com.example.flexvendor;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class HistorySlotFragment extends Fragment {

    private String checkMail, vEmail, name, phone, upiId;
    private ListView historyListViewSlot;
    private List<String> getImageUrl;
    private CustomListViewAdapter adapter;
    private int companyId;

    private List<Users> users;


    public HistorySlotFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity refActivity=getActivity();
        final View parentHolder=inflater.inflate(R.layout.fragment_history_slot, container, false);

        final ProgressDialog pd=ProgressDialog.show(refActivity, "Loading slots", "Please wait...", true);

        FirebaseUser vendUser=FirebaseAuth.getInstance().getCurrentUser();
        assert vendUser != null;
        checkMail=vendUser.getEmail();
        assert refActivity != null;

        historyListViewSlot=parentHolder.findViewById(R.id.historyListViewSlot);
        users=new ArrayList<>();
        getImageUrl=new ArrayList<>();


        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference histRef=database.getReference("History");
        final DatabaseReference vendRef=database.getReference("Vendor");
        final DatabaseReference userRef=database.getReference("User");

        vendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    vEmail=ds.child("vendorMail").getValue(String.class);
                    assert vEmail != null;
                    if (vEmail.equals(checkMail)) {

                        companyId=ds.child("companyId").getValue(Integer.class);

                        histRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    int slotFlag=ds.child("slotFlag").getValue(Integer.class);

                                    if (slotFlag == companyId) {

                                        final String email=ds.child("userMail").getValue(String.class);
                                        final String date=ds.child("showDate").getValue(String.class);
                                        final String stTime=ds.child("showStartTime").getValue(String.class);
                                        final String hours=ds.child("showWorkHours").getValue(String.class);
                                        final String transactionDateTime=ds.child("transactionDateTime").getValue(String.class);
                                        final String transactionMoney=ds.child("transactionMoney").getValue(String.class);
                                        final String slotId = ds.child("slotId").getValue(String.class);

                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                                                    final String inLoopEmail=ds.child("userMail").getValue(String.class);

                                                    assert inLoopEmail != null;
                                                    if (inLoopEmail.equals(email)) {

                                                        assert date != null;
                                                        final char[] modDateArr=date.toCharArray();
                                                        final StorageReference mStorageRef=FirebaseStorage.getInstance().getReference();
                                                        final StorageReference imgRef=mStorageRef.child(inLoopEmail + "/photo.jpg");

                                                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {

                                                                getImageUrl.add(uri.toString());
                                                                name=ds.child("userName").getValue(String.class);
                                                                phone=ds.child("userPhone").getValue(String.class);
                                                                upiId = ds.child("userUpiId").getValue(String.class);

                                                                String modTime=String.valueOf(modDateArr) + ", " + stTime + " | " + hours;
                                                                Users item=new Users(inLoopEmail, name, phone, modTime);
                                                                assert transactionDateTime != null;
                                                                assert transactionMoney != null;
                                                                if(transactionDateTime.equals("") && transactionMoney.equals(""))
                                                                {
                                                                    item.setTransactionDateTime("null");
                                                                    item.setTransactionMoney("null");
                                                                    item.setPaid(false);
                                                                    item.setUpiId(upiId);
                                                                    item.setSlotId(slotId);
                                                                }
                                                                else{
                                                                    item.setTransactionDateTime(transactionDateTime);
                                                                    item.setTransactionMoney(transactionMoney);
                                                                    item.setPaid(true);
                                                                    item.setUpiId("null");
                                                                    item.setSlotId("null");
                                                                }
                                                                users.add(item);
                                                                adapter=new CustomListViewAdapter(refActivity, R.layout.list_slot, users, getImageUrl);
                                                                historyListViewSlot.setAdapter(adapter);

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {


                                                            }
                                                        });
                                                    }


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }
                }

                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        historyListViewSlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvEmail = view.findViewById(R.id.invisibleEmail);
                TextView tvUpiId = view.findViewById(R.id.invisibleUpiId);
                TextView tvUserName = view.findViewById(R.id.tvUserName);
                TextView tvUsrPhone = view.findViewById(R.id.tvUserPhone);
                TextView tvTimings = view.findViewById(R.id.tvTimings);
                TextView tvSlot = view.findViewById(R.id.invisibleSlotId);
                if (!tvUpiId.getText().equals("")) {
                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra("email", tvEmail.getText().toString());
                    intent.putExtra("upiId", tvUpiId.getText().toString());
                    intent.putExtra("name", tvUserName.getText().toString());
                    intent.putExtra("phone", tvUsrPhone.getText().toString());
                    intent.putExtra("timings", tvTimings.getText().toString());
                    intent.putExtra("slotId", tvSlot.getText().toString());
                    startActivity(intent);
                }

            }
        });


        return parentHolder;
    }

}