package com.example.flexvendor;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*TextView textView=parentHolder.findViewById(R.id.textViewAbout);
        Typeface myFont1=Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/prod_sans_medium");
        textView.setTypeface(myFont1);*/ //For Specific text to be changed

        return inflater.inflate(R.layout.fragment_about, container,
                false);

    }

}
