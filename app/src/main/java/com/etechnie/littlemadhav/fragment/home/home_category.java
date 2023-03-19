package com.etechnie.littlemadhav.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.catagory_home_adapter_rv;
import com.etechnie.littlemadhav.dataModel_category_rv;
import com.payumoney.sdkui.ui.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

class home_category extends Fragment implements catagory_home_adapter_rv.ItemClickListener{

    private ArrayList<dataModel_category_rv>list= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_category, container, false);
        buildListData();
        initRecyclerView(view);

        return view;
    }

    private  void initRecyclerView(View view){
        RecyclerView rv = view.findViewById(R.id.rvCategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(layoutManager);
        catagory_home_adapter_rv adapter = new catagory_home_adapter_rv(list, this);
        rv.setAdapter(adapter);
    }

    private void buildListData(){
        list.add(new dataModel_category_rv("men"));
        list.add(new dataModel_category_rv("women"));
        list.add(new dataModel_category_rv("kids"));
        list.add(new dataModel_category_rv("beauty"));
        list.add(new dataModel_category_rv("sports"));
        list.add(new dataModel_category_rv("acccessories"));

    }

    @Override
    public void onItemClicked(dataModel_category_rv dataModel_category_rv) {

        Toast.makeText(getActivity(),"Item clicked",Toast.LENGTH_SHORT).show();


    }
}