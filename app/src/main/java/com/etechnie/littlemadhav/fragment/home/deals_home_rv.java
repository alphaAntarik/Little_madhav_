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

import java.util.ArrayList;
import java.util.List;


public class deals_home_rv extends Fragment implements Deals_adapter.ItemClickListener_deals{

        View rootview ;
    private List<datamodel_deals_rv> list_deals= new ArrayList<>();

    RecyclerView rv1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_deals_home_rv, container, false);;
        initRecyclerView(rootview);
        buildListData();
        return rootview;
    }

    private  void initRecyclerView(View view){
        rv1 = view.findViewById(R.id.dealsRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv1.setLayoutManager(linearLayoutManager);

        Deals_adapter adapter = new Deals_adapter(list_deals, this);
        rv1.setAdapter(adapter);
    }

    private void buildListData(){

        for(int i=1;i<=10;i++){
            datamodel_deals_rv d=new datamodel_deals_rv();


            d.setBrand_Image("https://img.freepik.com/free-vector/fashion-sale-template-concept_52683-33763.jpg?w=826&t=st=1663384127~exp=1663384727~hmac=b3ec4eccde9208fb2ae2194b0392cb1dfc3222d9d34854eb52e48d359ca2159b");


            list_deals.add(d);
        }

    }

    @Override
    public void onItemClicked_deals(datamodel_deals_rv datamodel_deals_rv) {
        Toast.makeText(getActivity(),"Item clicked",Toast.LENGTH_SHORT).show();

    }
}