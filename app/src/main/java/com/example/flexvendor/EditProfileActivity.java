package com.example.flexvendor;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class EditProfileActivity extends AppCompatActivity {

    View parentLayout;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(EditProfileActivity.this, android.R.color.background_light));// set status background white
        }

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable()); // Add Color.Parse("#000") inside ColorDrawable() for color change
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        ViewPager viewPager=findViewById(R.id.viewPager2);
        TabLayout tabLayout=findViewById(R.id.tabLayout2);

        TabAdapter adapter=new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new EditFragment(), "Edit Details");
        adapter.addFragment(new ChangePasswordFragment(), "Change Password");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra("openProfile", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra("openProfile", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        finish();

    }

}
