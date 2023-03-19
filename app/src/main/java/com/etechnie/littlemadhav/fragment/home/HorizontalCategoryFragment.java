package com.etechnie.littlemadhav.fragment.home;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.adapters.CategoryListAdapter_1;
import com.etechnie.littlemadhav.adapters.ProductAdapter;
import com.etechnie.littlemadhav.adapters.home_adapter.HorizontalCategoryListAdapter;
import com.etechnie.littlemadhav.app.App;
import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.fragment.Categories_1;
import com.etechnie.littlemadhav.models.category_model.CategoryDetails;
import com.etechnie.littlemadhav.models.category_model.Category_model;
import com.etechnie.littlemadhav.models.product_model.GetAllProducts;
import com.etechnie.littlemadhav.models.product_model.ProductData;
import com.etechnie.littlemadhav.models.product_model.ProductDetails;
import com.etechnie.littlemadhav.network.APIClient;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class HorizontalCategoryFragment extends Fragment {

    String customerID;
    Boolean isHeaderVisible=false
            ;
    Call<ProductData> networkCall;

    ShimmerFrameLayout shimmerFrameLayout;
    ProgressBar progressBar;
    TextView emptyRecord, headerText;
    RecyclerView top_seller_recycler;

    HorizontalCategoryListAdapter categoryListAdapter_1;

    List<Category_model> allCategoriesList;
    public void invalidateProducts(){
        categoryListAdapter_1.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_products_horizontal, container, false);

        // Get the Boolean from Bundle Arguments
      //  isHeaderVisible = getArguments().getBoolean("isHeaderVisible");

        // Get the CustomerID from SharedPreferences
        customerID = this.getContext().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
        //noInternetDialog.show();
        allCategoriesList = new ArrayList<>();

        // Get CategoriesList from ApplicationContext
        allCategoriesList = ((App) getContext().getApplicationContext()).getCategory_list();


        // Binding Layout Views
        emptyRecord = (TextView) rootView.findViewById(R.id.empty_record_text);
        headerText = (TextView) rootView.findViewById(R.id.products_horizontal_header);
        top_seller_recycler = (RecyclerView) rootView.findViewById(R.id.products_horizontal_recycler);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerFrame);
        progressBar = rootView.findViewById(R.id.progressBar);

        // Hide some of the Views
        emptyRecord.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    
        // Check if Header must be Invisible or not
        if (!isHeaderVisible) {
            headerText.setVisibility(View.GONE);
        } else {
            headerText.setText(getString(R.string.top_seller));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                headerText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sale, 0, 0, 0);
            } else {
                headerText.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_sale), null, null, null);
            }
        }
    
    

        

        // RecyclerView has fixed Size
        top_seller_recycler.setHasFixedSize(true);
        top_seller_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    
        // Initialize the ProductAdapter for RecyclerView
        categoryListAdapter_1 = new HorizontalCategoryListAdapter(getContext(), allCategoriesList, false);
    
        // Set the Adapter and LayoutManager to the RecyclerView
        top_seller_recycler.setAdapter(categoryListAdapter_1);
        




        return rootView;
    }




    @Override
    public void onPause() {

        // Check if NetworkCall is being executed

        super.onPause();
    }
}

