package com.etechnie.littlemadhav.fragment;


import static com.etechnie.littlemadhav.fragment.Top_Seller.productsList;
import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.Nullable;

import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etechnie.littlemadhav.app.App;
import com.etechnie.littlemadhav.models.ResponseData;
import com.facebook.shimmer.ShimmerFrameLayout;

import com.etechnie.littlemadhav.R;

import java.util.ArrayList;
import java.util.List;

import com.etechnie.littlemadhav.adapters.ProductAdapterRemovable;
import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.databases.User_Recents_DB;
import com.etechnie.littlemadhav.models.product_model.GetAllProducts;
import com.etechnie.littlemadhav.models.product_model.ProductData;
import com.etechnie.littlemadhav.models.product_model.ProductDetails;
import com.etechnie.littlemadhav.network.APIClient;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class RecentlyViewed extends Fragment {

    String customerID;

    TextView emptyRecord, headerText;
    RecyclerView recents_recycler;
    static List<ProductDetails> productsList;
    ProductAdapterRemovable productAdapter;
    User_Recents_DB recents_db = new User_Recents_DB();
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<Integer> recents;
    List<ProductDetails> recentViewedList;
    ProgressBar progressBar;

    public void invalidateProducts(){

        recents = recents_db.getUserRecents();

        if (recents.size() < 1 ) {
            headerText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            headerText.setVisibility(View.VISIBLE);
            recentViewedList.clear();
            for (int i=0;  i<recents.size();  i++) {
                // Request current Product's Details
                RequestProductDetails(recents.get(i));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_products_horizontal, container, false);

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
       // noInternetDialog.show();

        recents = new ArrayList<>();
        recentViewedList  = new ArrayList<>();

        // Get the List of RecentlyViewed Product's IDs from the Local Databases User_Recents_DB
        recents = recents_db.getUserRecents();

        // Get the CustomerID from SharedPreferences
        customerID = this.getContext().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");


        // Binding Layout Views
        emptyRecord = (TextView) rootView.findViewById(R.id.empty_record_text);
        headerText = (TextView) rootView.findViewById(R.id.products_horizontal_header);
        recents_recycler = (RecyclerView) rootView.findViewById(R.id.products_horizontal_recycler);
        progressBar = rootView.findViewById(R.id.progressBar);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerFrame);

        // Hide some of the Views
        emptyRecord.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        // Set text of Header
        headerText.setText(getString(R.string.recentlyViewed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            headerText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_recents, 0, 0, 0);
        } else {
            headerText.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_recents), null, null, null);
        }
    
    
        // Initialize the ProductAdapterRemovable and LayoutManager for RecyclerView
        productAdapter = new ProductAdapterRemovable(getContext(), recentViewedList, true, true, headerText);
        recents_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Set the Adapter and LayoutManager to the RecyclerView
        recents_recycler.setAdapter(productAdapter);
        

        // Check if the recents List isn't empty
        if (recents.size() < 1 ) {
            headerText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            headerText.setVisibility(View.VISIBLE);
            recentViewedList.clear();
            for (int i=0;  i<recents.size();  i++) {
                // Request current Product's Details
                RequestProductDetails(recents.get(i));
            }
        }


        return rootView;
    }



    //*********** Adds Products returned from the Server to the RecentViewedList ********//

    private void addRecentProducts(ProductData productData) {

        // Add Products to recentViewedList
        if (productData.getProductData().size() > 0 ) {
            recentViewedList.add(productData.getProductData().get(0));
        }

        // Notify the Adapter
        productAdapter.notifyDataSetChanged();
    }



    //*********** Request the Product's Details from the Server based on Product's ID ********//

    public void RequestProductDetails(final int products_id) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        GetAllProducts getAllProducts = new GetAllProducts();
        getAllProducts.setPageNumber(0);
        getAllProducts.setLanguageId(ConstantValues.LANGUAGE_ID);
        getAllProducts.setCustomersId(customerID);
        getAllProducts.setProductsId(String.valueOf(products_id));
        getAllProducts.setCurrencyCode(ConstantValues.CURRENCY_CODE);


        Call<ResponseData<List<ProductDetails>>> call = APIClient.getInstance()
                .getAllProducts
                        (
                                getAllProducts
                        );

        call.enqueue(new Callback<ResponseData<List<ProductDetails>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<ProductDetails>>> call, retrofit2.Response<ResponseData<List<ProductDetails>>> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        // Product's Details has been returned.
                        // Add Product to the recentViewedList
                        addProducts(response.body().getData());

                    }
                    else if (response.body().getStatus().equalsIgnoreCase("0")) {
                        // Product's Details haven't been returned.
                        // Call the method to process some implementations
                        addProducts(response.body().getData());

                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<ProductDetails>>> call, Throwable t) {
                Toast.makeText(getActivity(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
        
    }

    private static void addProducts(List<ProductDetails> productData) {

        // Add Products to ProductsList from the List of ProductData
        for (int i = 0; i < productData.size(); i++) {
            ProductDetails productDetails = productData.get(i);
            productsList.add(productDetails);
        }}

}

