package com.etechnie.littlemadhav.fragment.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.etechnie.littlemadhav.Deals_adapter;
import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.datamodel_deals_rv;
import com.etechnie.littlemadhav.datamodel_top_picks_rv;
import com.etechnie.littlemadhav.topPicksAdapter;

import java.util.ArrayList;
import java.util.List;


public class topPicks_home_rv extends Fragment implements topPicksAdapter.ItemClickListener_topPicks{
    View rootview;
    private List<datamodel_top_picks_rv> list_top_pickss= new ArrayList<>();

    RecyclerView rv2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_top_picks_home_rv, container, false);
        initRecyclerView(rootview);
        buildListData();
        return rootview;
    }
    private  void initRecyclerView(View view){
        rv2 = view.findViewById(R.id.topPicksRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv2.setLayoutManager(linearLayoutManager);

        topPicksAdapter adapter = new topPicksAdapter(list_top_pickss, this);
        rv2.setAdapter(adapter);
    }

    private void buildListData(){

        for(int i=1;i<=10;i++){
            datamodel_top_picks_rv d=new datamodel_top_picks_rv();


            d.setBrand_Image("https://img.freepik.com/free-psd/fashion-poster-template_23-2148838187.jpg?w=826&t=st=1663390937~exp=1663391537~hmac=c6972505169d67aca95ddc46f04e9871e9e1a4f13f0138cac94269836e83f5ec");


            list_top_pickss.add(d);
        }

    }

    @Override
    public void onItemClicked_topPicks(datamodel_top_picks_rv datamodel_top_picks_rv) {
        Toast.makeText(getActivity(),"Item clicked",Toast.LENGTH_SHORT).show();

    }
}