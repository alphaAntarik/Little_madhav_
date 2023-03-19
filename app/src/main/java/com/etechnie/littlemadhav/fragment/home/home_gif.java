package com.etechnie.littlemadhav.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.etechnie.littlemadhav.R;

import pl.droidsonroids.gif.GifImageButton;


public class home_gif extends Fragment {

    View view;
    GifImageButton gif;
    FragmentManager fragmentManager;

    ClickedOnGif _clickedOnGif;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_gif, container, false);
        gif = view.findViewById(R.id.gif);

        _clickedOnGif = new ClickedOnGif();

        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.homelayout, _clickedOnGif).addToBackStack(null).commit();
                Toast.makeText(getActivity(),"GIF is clicked",Toast.LENGTH_SHORT).show();


            }
        });
        return view;
    }


}