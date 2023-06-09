package com.etechnie.littlemadhav.fragment;


import static com.facebook.FacebookSdk.getApplicationContext;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etechnie.littlemadhav.models.ResponseData;
import com.facebook.shimmer.ShimmerFrameLayout;

import com.etechnie.littlemadhav.R;

import java.util.ArrayList;
import java.util.List;

import com.etechnie.littlemadhav.adapters.ProductAdapter;
import com.etechnie.littlemadhav.app.App;
import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.models.product_model.GetAllProducts;
import com.etechnie.littlemadhav.models.product_model.ProductData;
import com.etechnie.littlemadhav.models.product_model.ProductDetails;
import com.etechnie.littlemadhav.network.APIClient;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class Most_Liked extends Fragment {

    String customerID;
    Boolean isHeaderVisible;
    Call<ResponseData<List<ProductDetails>>> call;

    TextView emptyRecord, headerText;
    RecyclerView most_liked_recycler;
     List<ProductDetails> productsList;
    ProductAdapter productAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    List<ProductDetails> mostLikedProductList;

    ProgressBar progressBar;

    public void invalidateProducts(){
        productAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_products_horizontal, container, false);

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
       // noInternetDialog.show();

        // Get the Boolean from Bundle Arguments
        isHeaderVisible = getArguments().getBoolean("isHeaderVisible");

        // Get the CustomerID from SharedPreferences
        customerID = this.getContext().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");


        // Binding Layout Views
        emptyRecord = (TextView) rootView.findViewById(R.id.empty_record_text);
        headerText = (TextView) rootView.findViewById(R.id.products_horizontal_header);
        most_liked_recycler = (RecyclerView) rootView.findViewById(R.id.products_horizontal_recycler);
        progressBar = rootView.findViewById(R.id.progressBar);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerFrame);

        // Hide some of the Views
        emptyRecord.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    
        // Check if Header must be Invisible or not
        if (!isHeaderVisible) {
            headerText.setVisibility(View.GONE);
        } else {
            headerText.setText(getString(R.string.most_liked));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                headerText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_most_liked, 0, 0, 0);
            } else {
                headerText.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_most_liked), null, null, null);
            }
        }
    
    
        mostLikedProductList = new ArrayList<>();
    
    
        // RecyclerView has fixed Size
        most_liked_recycler.setHasFixedSize(true);
        most_liked_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    
        // Initialize the ProductAdapter for RecyclerView
        productAdapter = new ProductAdapter(getContext(), mostLikedProductList, true,false);
    
        // Set the Adapter and LayoutManager to the RecyclerView
        most_liked_recycler.setAdapter(productAdapter);
    
    
        // Request for Most Sold Products
        RequestMostLikedProducts();


        return rootView;
    }



    //*********** Adds Products returned from the Server to the MostLikedProductList ********//

    private  void addProducts(List<ProductDetails> productData) {

        // Add Products to ProductsList from the List of ProductData
        for (int i = 0; i < productData.size(); i++) {
            ProductDetails productDetails = productData.get(i);
            productsList.add(productDetails);
        }}



    //*********** Request all the Products from the Server based on Product's Total Likes ********//

    public void RequestMostLikedProducts() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        GetAllProducts getAllProducts = new GetAllProducts();
        getAllProducts.setPageNumber(0);
        getAllProducts.setLanguageId(ConstantValues.LANGUAGE_ID);
        getAllProducts.setCustomersId(customerID);
        getAllProducts.setType("most liked");
        getAllProducts.setCurrencyCode(ConstantValues.CURRENCY_CODE);


        call = APIClient.getInstance()
                .getAllProducts
                        (
                                getAllProducts
                        );

        call.enqueue(new Callback<ResponseData<List<ProductDetails>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<ProductDetails>>> call, retrofit2.Response<ResponseData<List<ProductDetails>>> response) {
                
                if (response.isSuccessful()) {
                    
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        // Products have been returned. Add Products to the mostLikedProductList
                        addProducts(response.body().getData());
                        emptyRecord.setVisibility(View.GONE);

                    }
                    else if (response.body().getStatus().equalsIgnoreCase("0")) {
                        // Products haven't been returned
                        emptyRecord.setVisibility(View.VISIBLE);
                        if (isHeaderVisible){
                            emptyRecord.setVisibility(View.GONE);
                            headerText.setVisibility(View.GONE);
                        }
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<ProductDetails>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(App.getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }
        });
    }



    @Override
    public void onPause() {

        // Check if NetworkCall is being executed
        if (call.isExecuted()){
            // Cancel the NetworkCall
            call.cancel();
        }

        super.onPause();
    }

}