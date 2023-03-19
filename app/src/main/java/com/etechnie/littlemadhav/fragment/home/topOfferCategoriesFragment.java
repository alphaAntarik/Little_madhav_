package com.etechnie.littlemadhav.fragment.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.adapters.Brand_adapter;
import com.etechnie.littlemadhav.datamodel_brands_rv;
import com.etechnie.littlemadhav.datamodel_top_offer_categories_rv;
import com.etechnie.littlemadhav.topOfferCategoriesAdapter;

import java.util.ArrayList;
import java.util.List;


public class topOfferCategoriesFragment extends Fragment implements topOfferCategoriesAdapter.ItemClickListener_topOfferCategories{

     View rootview;
    private List<datamodel_top_offer_categories_rv> list_brands= new ArrayList<>();

    RecyclerView rv3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_top_offer_categories, container, false);
        initRecyclerView(rootview);
        buildListData();

        return rootview;
    }
    private  void initRecyclerView(View view){
        rv3 = view.findViewById(R.id.topOfferCategoriesRV);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        rv3.setHasFixedSize(true);
        rv3.setLayoutManager( gridLayoutManager);

        topOfferCategoriesAdapter adapter = new topOfferCategoriesAdapter(list_brands, this);
        rv3.setAdapter(adapter);
    }
    private void buildListData(){

        for(int i=1;i<=10;i++){
            datamodel_top_offer_categories_rv d=new datamodel_top_offer_categories_rv();

            d.setCategory_name("Brand name" +i);
            d.setBrand_Image("https://img.freepik.com/premium-photo/athletic-young-man-doing-athletic-body-isolated-nature-background-healthy-lifestyle-concept-vertical-view_246930-1610.jpg?w=740");


            list_brands.add(d);
        }

    }

    @Override
    public void onItemClicked_topOfferCategories(datamodel_top_offer_categories_rv datamodel_top_offer_categories_rv) {
        Toast.makeText(getActivity(),"Item clicked",Toast.LENGTH_SHORT).show();

    }
}