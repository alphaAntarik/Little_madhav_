package com.etechnie.littlemadhav.network;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.etechnie.littlemadhav.models.ResponseData;
import com.etechnie.littlemadhav.models.banner_model.BannerDetails;
import com.etechnie.littlemadhav.models.category_model.CategoryDetails;
import com.etechnie.littlemadhav.models.product_model.GetAllProducts;
import com.etechnie.littlemadhav.models.product_model.ProductData;
import com.etechnie.littlemadhav.models.product_model.ProductDetails;
import com.google.gson.Gson;
import com.etechnie.littlemadhav.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.etechnie.littlemadhav.app.App;
import com.etechnie.littlemadhav.utils.Utilities;
import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.models.user_model.UserData;
import com.etechnie.littlemadhav.models.banner_model.BannerData;
import com.etechnie.littlemadhav.models.category_model.CategoryData;
import com.etechnie.littlemadhav.models.device_model.AppSettingsData;
import com.etechnie.littlemadhav.models.device_model.DeviceInfo;
import com.etechnie.littlemadhav.models.pages_model.PagesDetails;
import com.etechnie.littlemadhav.models.pages_model.PagesData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.etechnie.littlemadhav.app.App.getContext;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * StartAppRequests contains some Methods and API Requests, that are Executed on Application Startup
 **/

public class StartAppRequests {

    private App app = new App();
    static List<ProductDetails> productsList;



    public StartAppRequests(Context context) {
        app = ((App) context.getApplicationContext());
    }



    //*********** Contains all methods to Execute on Startup ********//

    public void StartRequests(){
        List<BannerDetails> bannerDetailsList=new ArrayList<BannerDetails>();
        for(int i=1;i<=10;i++){
            BannerDetails b=new BannerDetails();
            b.setId(i);
            b.setTitle("title"+i);
            b.setImage("https://pngimg.com/uploads/mountain/mountain_PNG22.png");
            b.setUrl("https://pngimg.com/uploads/mountain/mountain_PNG22.png");
            bannerDetailsList.add(b);

        }

//        List<CategoryDetails> categoryData = new ArrayList<CategoryDetails>();
//
//        for(int i = 1; i<=10;i++){
//            CategoryDetails c = new CategoryDetails();
//            c.setName("boy"+i);
//            c.setParentId("0");
//            categoryData.add(c);
//
//        }


        app.setBannersList(bannerDetailsList);


//        app.setCategoriesList(categoryData);

        //RequestBanners();
        //RequestAllCategories();
      //  RequestAppSetting();
       // RequestStaticPagesData();
        
    }
    private static void addProducts(List<ProductDetails> productData) {

        // Add Products to ProductsList from the List of ProductData
        for (int i = 0; i < productData.size(); i++) {
            ProductDetails productDetails = productData.get(i);
            productsList.add(productDetails);
        }}


    //*********** Register Device to Admin Panel with the Device's Info ********//
    
    public static void RegisterDeviceForFCM(final Context context) {

        GetAllProducts getAllProducts = new GetAllProducts();
        Call<ResponseData<List<ProductDetails>>> productsCall = APIClient.getInstance()
                .getAllProducts
                        (
                                getAllProducts
                        );

        productsCall.enqueue(new Callback<ResponseData<List<ProductDetails>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<ProductDetails>>> call, retrofit2.Response<ResponseData<List<ProductDetails>>> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        // Products have been returned. Add Products to the ProductsList
                        addProducts(response.body().getData());
                    }
                    else if (response.body().getStatus().equalsIgnoreCase("0")) {
                        // Products haven't been returned. Call the method to process some implementations
                        addProducts(response.body().getData());
                        // Show the Message to the User

                    }
                    else {
                        // Unable to get Success status
                             }

                    // Hide the ProgressBar

                }
                else {
                       Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseData<List<ProductDetails>>> call, Throwable t) {

            }
        });
    
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
    
        String deviceID = "";
        DeviceInfo device = Utilities.getDeviceInfo(context);
        String customer_ID = sharedPreferences.getString("userID", "");
        
        
        if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("onesignal")) {
            deviceID = OneSignal.getPermissionSubscriptionState ().getSubscriptionStatus().getUserId();
        }
        else if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("fcm")) {
            deviceID = FirebaseInstanceId.getInstance().getToken();
        }
        
        
        
        Call<UserData> call = APIClient.getInstance()
                .registerDeviceToFCM
                        (
                                deviceID,
                                device.getDeviceType(),
                                device.getDeviceRAM(),
                                device.getDeviceProcessors(),
                                device.getDeviceAndroidOS(),
                                device.getDeviceLocation(),
                                device.getDeviceModel(),
                                device.getDeviceManufacturer(),
                                customer_ID
                        );
        
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        
                        Log.i("notification", response.body().getMessage());
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    
                    }
                    else {
                        
                        Log.i("notification", response.body().getMessage());
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.i("notification", response.errorBody().toString());
                }
            }
            
            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
//                Toast.makeText(context, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });


        
    }



    //*********** API Request Method to Fetch App Banners ********//

    public void RequestBanners() {
    
        Call<BannerData> call = APIClient.getInstance()
                .getBanners();
    
        try {
            Response<BannerData> response = call.execute();
    
            BannerData bannerData = new BannerData();
        
            if (response.isSuccessful()) {
    
                bannerData = response.body();
    
                if (!TextUtils.isEmpty(bannerData.getSuccess()))
                    app.setBannersList(bannerData.getData());
            
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    //*********** API Request Method to Fetch All Categories ********//
    
    public void RequestAllCategories() {
    
        Call<CategoryData> call = APIClient.getInstance()
                .getAllCategories
                        (
                                ConstantValues.LANGUAGE_ID
                        );
        
        try {
            Response<CategoryData> response = call.execute();

            CategoryData categoryData = new CategoryData();
            
            if (response.isSuccessful()) {

                String json= new Gson().toJson(response.body());
                categoryData = response.body();

                if (!TextUtils.isEmpty(categoryData.getSuccess()))
                    app.setCategoriesList(categoryData.getData());
            
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }



    //*********** Request App Settings from the Server ********//
    
    private void RequestAppSetting() {
    
        Call<AppSettingsData> call = APIClient.getInstance()
                .getAppSetting();
    
        try {
            Response<AppSettingsData> response = call.execute();
        
            if (response.isSuccessful()) {
    

                AppSettingsData appSettingsData = null;
    
                appSettingsData = response.body();
                String strJson = new Gson().toJson(appSettingsData);
                if (!TextUtils.isEmpty(appSettingsData.getSuccess()))
                    app.setAppSettingsDetails(appSettingsData.getAppDetails());

            }
           else {
               
               Log.e("Appsettings","Response is not successful");
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    //*********** Request Static Pages Data from the Server ********//
    
    private void RequestStaticPagesData() {
    
        ConstantValues.ABOUT_US = app.getString(R.string.lorem_ipsum);
        ConstantValues.TERMS_SERVICES = app.getString(R.string.lorem_ipsum);
        ConstantValues.PRIVACY_POLICY = app.getString(R.string.lorem_ipsum);
        ConstantValues.REFUND_POLICY = app.getString(R.string.lorem_ipsum);
        ConstantValues.A_Z = app.getString(R.string.lorem_ipsum);
    
    
        Call<PagesData> call = APIClient.getInstance()
                .getStaticPages
                        (
                                ConstantValues.LANGUAGE_ID
                        );
    
        try {
            Response<PagesData> response = call.execute();
    
            PagesData pages = new PagesData();
            
            if (response.isSuccessful()) {
                
                pages = response.body();
    
                if (pages.getSuccess().equalsIgnoreCase("1")) {
        
                    app.setStaticPagesDetails(pages.getPagesData());
        
                    for (int i=0;  i<pages.getPagesData().size();  i++) {
                        PagesDetails page = pages.getPagesData().get(i);
            
                        if (page.getSlug().equalsIgnoreCase("about-us")) {
                            ConstantValues.ABOUT_US = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("term-services")) {
                            ConstantValues.TERMS_SERVICES = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("privacy-policy")) {
                            ConstantValues.PRIVACY_POLICY = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("refund-policy")) {
                            ConstantValues.REFUND_POLICY = page.getDescription();
                        }
                        else if(page.getSlug().equalsIgnoreCase("A-Z")){
                            ConstantValues.A_Z = page.getDescription();
                        }
                    }
                }
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
