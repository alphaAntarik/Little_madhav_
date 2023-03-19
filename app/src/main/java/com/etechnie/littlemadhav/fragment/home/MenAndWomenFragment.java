package com.etechnie.littlemadhav.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.activities.MainActivity;


public class MenAndWomenFragment extends Fragment {
    Button menbutton;
    Button womenbutton;

    ForHim _fhim;
    ForHer _fher;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_men_and_women, container, false);
        fragmentManager = getFragmentManager();
        _fhim = new ForHim();
        _fher = new ForHer();

        menbutton = view.findViewById(R.id.forHim);

        menbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction().replace(R.id.homelayout, _fhim).addToBackStack(null).commit();
                Toast.makeText(getActivity(),"Item clicked",Toast.LENGTH_SHORT).show();

            }
        });
        womenbutton = view.findViewById(R.id.forHer);

        womenbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.homelayout, _fher).addToBackStack(null).commit();

                Toast.makeText(getActivity(),"Item clicked",Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
}