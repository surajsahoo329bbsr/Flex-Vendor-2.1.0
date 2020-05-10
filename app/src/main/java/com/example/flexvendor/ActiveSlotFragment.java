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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class ActiveSlotFragment extends Fragment {

    private String checkMail, vEmail, name, phone;
    private List<String> getImageUrl;
    private ListView activeListViewSlot;
    private CustomListViewAdapter adapter;
    private int companyId;

    private List<Users> users;


    public ActiveSlotFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity refActivity=getActivity();
        final View parentHolder=inflater.inflate(R.layout.fragment_active_slot, container, false);

        final ProgressDialog pd=ProgressDialog.show(refActivity, "Loading slots", "Please wait...", true);

        FirebaseUser vendUser=FirebaseAuth.getInstance().getCurrentUser();
        assert vendUser != null;
        checkMail = vendUser.getEmail();
        assert refActivity != null;

        activeListViewSlot=parentHolder.findViewById(R.id.activeListViewSlot);
        users=new ArrayList<>();
        getImageUrl=new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference vendRef = database.getReference("Vendor");
        final DatabaseReference slotRef = database.getReference("Slot");
        final DatabaseReference userRef = database.getReference("User");

        vendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    vEmail=ds.child("vendorMail").getValue(String.class);
                    assert vEmail != null;
                    if (vEmail.equals(checkMail)) {

                        companyId=ds.child("companyId").getValue(Integer.class);

                        slotRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    int slotFlag = ds.child("slotFlag").getValue(Integer.class);

                                    if (slotFlag == companyId) {

                                        String id=ds.child("slotId").getValue(String.class);
                                        final String email=ds.child("userMail").getValue(String.class);
                                        final String date=ds.child("showDate").getValue(String.class);
                                        final String stTime=ds.child("showStartTime").getValue(String.class);
                                        final String hours=ds.child("showWorkHours").getValue(String.class);
                                        final String transactionDateTime="not_null";
                                        final String transactionMoney="not_null";

                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy");
                                        Date strDate = null;
                                        try {
                                            assert date != null;
                                            strDate = df.parse(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Date currDate = Calendar.getInstance().getTime();

                                        //currDate.compareTo(strDate) > 0 means slot date has passed or same date

                                        if (currDate.compareTo(strDate) > 0) {

                                            int timeCount=8, hourCount=2;
                                            assert stTime != null;
                                            switch (stTime) {

                                                case "9 am":
                                                    timeCount=9;
                                                    break;
                                                case "10 am":
                                                    timeCount=10;
                                                    break;
                                                case "11 am":
                                                    timeCount=11;
                                                    break;
                                                case "12 pm":
                                                    timeCount=12;
                                                    break;
                                                case "1 pm":
                                                    timeCount=13;
                                                    break;
                                                case "2 pm":
                                                    timeCount=14;
                                                    break;
                                                case "3 pm":
                                                    timeCount=15;
                                                    break;
                                                case "4 pm":
                                                    timeCount=16;
                                                    break;
                                                case "5 pm":
                                                    timeCount=17;
                                                    break;
                                                case "6 pm":
                                                    timeCount=18;
                                                    break;
                                            }

                                            assert hours != null;
                                            switch (hours) {
                                                case "3 hours":
                                                    hourCount=3;
                                                    break;
                                                case "4 hours":
                                                    hourCount=4;
                                                    break;
                                                case "5 hours":
                                                    hourCount=5;
                                                    break;
                                                case "6 hours":
                                                    hourCount=6;
                                                    break;
                                                case "7 hours":
                                                    hourCount=7;
                                                    break;
                                                case "8 hours":
                                                    hourCount=8;
                                                    break;
                                                case "9 hours":
                                                    hourCount=9;
                                                    break;
                                                case "10 hours":
                                                    hourCount=10;
                                                    break;
                                                case "11 hours":
                                                    hourCount=11;
                                                    break;
                                                case "12 hours":
                                                    hourCount=12;
                                                    break;
                                            }

                                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
                                            Date sDate=new Date();
                                            String currentDate=sdf.format(sDate);

                                            int currentHour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY); //Current hour

                                            if (currentDate.equals(date)) {

                                                // current hour passed
                                                if (currentHour >= (timeCount + hourCount)) {


                                                    DatabaseReference dbSlot=FirebaseDatabase.getInstance().getReference("Slot");
                                                    assert id != null;
                                                    dbSlot.child(id).child("slotFlag").setValue(8);
                                                    DatabaseReference databaseHistory=database.getReference("History");
                                                    String idHistory=databaseHistory.push().getKey();
                                                    Slot slot=new Slot(email, idHistory, slotFlag, date, stTime, hours);
                                                    assert idHistory != null;
                                                    databaseHistory.child(idHistory).setValue(slot);

                                                } else {

                                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                final String inLoopEmail=ds.child("userMail").getValue(String.class);

                                                                assert email != null;
                                                                if (email.equals(inLoopEmail)) {

                                                                    char[] dateArr=date.toCharArray();
                                                                    final char[] modDateArr=new char[date.length()];
                                                                    int count=0;

                                                                    for (int i=0; i < dateArr.length; i++) {
                                                                        if (dateArr[i] == '-')
                                                                            count++;
                                                                        if (count == 2)
                                                                            break;

                                                                        modDateArr[i]=dateArr[i];
                                                                    }

                                                                    final StorageReference mStorageRef=FirebaseStorage.getInstance().getReference();
                                                                    final StorageReference imgRef=mStorageRef.child(inLoopEmail + "/photo.jpg");

                                                                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {

                                                                            getImageUrl.add(uri.toString());
                                                                            name=ds.child("userName").getValue(String.class);
                                                                            phone=ds.child("userPhone").getValue(String.class);
                                                                            String modTime=String.valueOf(modDateArr) + ", " + stTime + " | " + hours;
                                                                            Users item=new Users(inLoopEmail, name, phone, modTime);
                                                                            item.setTransactionDateTime(transactionDateTime);
                                                                            item.setTransactionMoney(transactionMoney);
                                                                            item.setPaid(false);
                                                                            users.add(item);
                                                                            adapter=new CustomListViewAdapter(refActivity, R.layout.list_slot, users, getImageUrl);
                                                                            activeListViewSlot.setAdapter(adapter);
                                                                            Collections.sort(getImageUrl);


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
                                            } else {
                                                //Date passed code
                                                DatabaseReference dbSlot=FirebaseDatabase.getInstance().getReference("Slot");
                                                assert id != null;
                                                dbSlot.child(id).child("slotFlag").setValue(8);
                                                DatabaseReference databaseHistory=database.getReference("History");
                                                String idHistory=databaseHistory.push().getKey();
                                                Slot slot=new Slot(email, idHistory, slotFlag, date, stTime, hours);
                                                assert idHistory != null;
                                                databaseHistory.child(idHistory).setValue(slot);

                                            }

                                        }
                                        else {

                                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                                        final String inLoopEmail=ds.child("userMail").getValue(String.class);

                                                        assert email != null;
                                                        if (email.equals(inLoopEmail)) {

                                                            final char[] modDateArr=date.toCharArray();

                                                            final StorageReference mStorageRef=FirebaseStorage.getInstance().getReference();
                                                            final StorageReference imgRef=mStorageRef.child(inLoopEmail + "/photo.jpg");

                                                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {

                                                                    getImageUrl.add(uri.toString());
                                                                    name=ds.child("userName").getValue(String.class);
                                                                    phone=ds.child("userPhone").getValue(String.class);
                                                                    String modTime=String.valueOf(modDateArr) + ", " + stTime + " | " + hours;
                                                                    Users item=new Users(inLoopEmail, name, phone, modTime);
                                                                    item.setTransactionDateTime(transactionDateTime);
                                                                    item.setTransactionMoney(transactionMoney);
                                                                    item.setPaid(false);
                                                                    users.add(item);
                                                                    adapter=new CustomListViewAdapter(refActivity, R.layout.list_slot, users, getImageUrl);
                                                                    activeListViewSlot.setAdapter(adapter);

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

                                            pd.dismiss();
                                        }


                                    }

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });

                        pd.dismiss();
                        break;

                    }
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pd.dismiss();

        activeListViewSlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView=view.findViewById(R.id.invisibleEmail);
                String getMailFromList=textView.getText().toString();
                Intent it=new Intent(getActivity(), LicenseDetailsActivity.class);
                it.putExtra("email", getMailFromList);
                startActivity(it);
            }
        });


        return parentHolder;
    }


}