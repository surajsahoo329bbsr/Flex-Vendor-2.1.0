package com.example.flexvendor;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    FirebaseAuth auth;
    TextView tvWelcome, tvEmail, tvTabTitle;
    DatabaseReference dbRef,usrRef;
    String uEmail,checkEmail;
    String id, name, getImageUrl;
    FrameLayout frameLayout;
    boolean openProfile, openSlots;
    private TabLayout tabLayout;

    ImageView imageView;
    private ViewPager viewPager;

    @RequiresApi(api=VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);

        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Flex Vendor");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.background_dark));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.background_dark));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        frameLayout=findViewById(R.id.fragment_container);
        tvTabTitle=findViewById(R.id.tvTabTitle);
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);


        dbRef=FirebaseDatabase.getInstance().getReference();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();
        usrRef=dbRef.child("Vendor");

        final StorageReference mStorageRef=FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef=mStorageRef.child(checkEmail + "/photo.jpg");
        final long TEN_MEGABYTES=10024 * 10024;

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                getImageUrl=uri.toString();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });

        imgRef.getBytes(TEN_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Glide.with(MainActivity.this)
                        .load(getImageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });


        auth = FirebaseAuth.getInstance();

        View hView = navigationView.getHeaderView(0);
        tvWelcome=hView.findViewById(R.id.tvWelcome);
        tvEmail=hView.findViewById(R.id.tvNavEmail);
        imageView=hView.findViewById(R.id.nav_header_imageView);

        View parentLayout = findViewById(android.R.id.content);

        getUName();

        if (ChangePasswordFragment.resetPass == 1) {
            Snackbar.make(parentLayout,"Password changed successfully", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();
            ChangePasswordFragment.resetPass=0;
        }

        if (EditFragment.updateFlag == 1) {
            Snackbar.make(parentLayout, "Profile Updated", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            EditFragment.updateFlag=0;
        }

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        Bundle extras=getIntent().getExtras();

        if (extras != null && extras.containsKey("openProfile"))
            openProfile=extras.getBoolean("openProfile");
        if (openProfile) {

            tvTabTitle.setVisibility(View.GONE);
            ft.replace(R.id.fragment_container, new ProfileFragment());
            ft.addToBackStack(null);
            ft.commit();
        }

        if (extras != null && extras.containsKey("openSlots"))
            openSlots=extras.getBoolean("openSlots");
        if (openSlots) {

            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            TabAdapter adapter=new TabAdapter(getSupportFragmentManager());
            adapter.addFragment(new ActiveSlotFragment(), "Active Slots");
            adapter.addFragment(new HistorySlotFragment(), "History Slots");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    private void getUName() {


        ValueEventListener userListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    uEmail = ds.child("vendorMail").getValue(String.class);

                    assert uEmail != null;
                    if (uEmail.equals(checkEmail)) {

                        name = ds.child("vendorName").getValue(String.class);
                        assert name != null;
                        tvWelcome.setText(name);
                        tvEmail.setText(checkEmail);
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        };

        usrRef.addListenerForSingleValueEvent(userListener);

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(getSupportFragmentManager().getBackStackEntryCount()==0)
        {
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
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment ob ;
        switch (menuItem.getItemId()) {

            case R.id.nav_slots:

                frameLayout.setVisibility(View.GONE);
                tvTabTitle.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                TabAdapter adapter=new TabAdapter(getSupportFragmentManager());
                adapter.addFragment(new ActiveSlotFragment(), "Active Slots");
                adapter.addFragment(new HistorySlotFragment(), "History Slots");
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_company:

                Intent it=new Intent(MainActivity.this, InformationActivity.class);
                it.putExtra("flipkart", "0");
                it.putExtra("fedex", "0");
                it.putExtra("aramex", "0");
                it.putExtra("delhivery", "0");
                it.putExtra("bluedart", "0");
                it.putExtra("dtdc", "0");
                it.putExtra("ipost", "0");

                final FirebaseDatabase database=FirebaseDatabase.getInstance();
                final DatabaseReference vendRef=database.getReference("Vendor");

                vendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String vEmail=ds.child("vendorMail").getValue(String.class);
                            assert vEmail != null;
                            if (vEmail.equals(checkEmail)) {
                                int position=ds.child("companyId").getValue(Integer.class);
                                Intent it=new Intent(MainActivity.this, InformationActivity.class);

                                it.putExtra("flipkart", "0");
                                it.putExtra("fedex", "0");
                                it.putExtra("aramex", "0");
                                it.putExtra("delhivery", "0");
                                it.putExtra("bluedart", "0");
                                it.putExtra("dtdc", "0");
                                it.putExtra("ipost", "0");


                                switch (position) {

                                    case 1:
                                        it.putExtra("flipkart", "1");
                                        finish();
                                        startActivity(it);
                                        break;

                                    case 2:
                                        it.putExtra("fedex", "2");
                                        finish();
                                        startActivity(it);
                                        break;

                                    case 3:
                                        it.putExtra("aramex", "3");
                                        finish();
                                        startActivity(it);
                                        break;

                                    case 4:
                                        it.putExtra("delhivery", "4");
                                        finish();
                                        startActivity(it);
                                        break;

                                    case 5:
                                        it.putExtra("bluedart", "5");
                                        finish();
                                        startActivity(it);
                                        break;

                                    case 6:
                                        it.putExtra("dtdc", "6");
                                        finish();
                                        startActivity(it);
                                        break;

                                    case 7:
                                        it.putExtra("ipost", "7");
                                        finish();
                                        startActivity(it);
                                        break;

                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_profile:

                tvTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob=new ProfileFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_contacts:

                tvTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob = new ContactsFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_feedback:

                tvTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob = new FeedbackFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_about:

                tvTabTitle.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ob = new AboutFragment();
                ft.replace(R.id.fragment_container,ob);
                ft.addToBackStack(null);
                ft.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_share:

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Flex - Delivering Jobs");
                String message = "\nLet me recommend you this application - Flex (Beta)\n\n";
                message = message + "https://drive.google.com/drive/folders/1pw-ZdvNKvJ0ITXn9YvyXYb9v7zlIPb_q?usp=sharing \n\n";
                i.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, "Share Via"));
                break;

            case R.id.nav_logout:

                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_round)
                        .setTitle("Logging out")
                        .setMessage("Do you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth.signOut();
                                Intent it = new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(it);
                                finish();
                            }
                        })
                        .setNegativeButton("No",null).show();


        }


        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}

