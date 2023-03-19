package com.etechnie.littlemadhav.fragment.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.adapters.Brand_adapter;
import com.etechnie.littlemadhav.catagory_home_adapter_rv;
import com.etechnie.littlemadhav.dataModel_category_rv;
import com.etechnie.littlemadhav.datamodel_brands_rv;
import com.etechnie.littlemadhav.models.banner_model.BannerDetails;

import java.util.ArrayList;
import java.util.List;

public class Brand_rv extends Fragment implements Brand_adapter.ItemClickListener_brands{
    private List<datamodel_brands_rv> list_brands= new ArrayList<>();

    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_brand_rv, container, false);
        initRecyclerView(view);
        buildListData();



        return view;
    }

    private  void initRecyclerView(View view){
        rv = view.findViewById(R.id.brandRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);

        Brand_adapter adapter = new Brand_adapter(list_brands, this);
        rv.setAdapter(adapter);
    }

    private void buildListData(){

        for(int i=1;i<=10;i++){
            datamodel_brands_rv d=new datamodel_brands_rv();

            d.setBrand_name("Brand name" +i);
            d.setBrand_image("https://img.freepik.com/premium-photo/athletic-young-man-doing-athletic-body-isolated-nature-background-healthy-lifestyle-concept-vertical-view_246930-1610.jpg?w=740");
            d.setPrice("price" +i);

            list_brands.add(d);
        }

    }

    @Override
    public void onItemClicked_brands(datamodel_brands_rv datamodel_brands_rv) {

        Toast.makeText(getActivity(),"Item clicked",Toast.LENGTH_SHORT).show();


    }
}