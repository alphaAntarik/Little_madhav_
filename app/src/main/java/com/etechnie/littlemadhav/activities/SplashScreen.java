package com.etechnie.littlemadhav.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.etechnie.littlemadhav.models.Brand_model.Brand_Model;
import com.etechnie.littlemadhav.models.ResponseData;
import com.etechnie.littlemadhav.models.category_model.Category_model;
import com.etechnie.littlemadhav.models.product_model.ProductData;
import com.etechnie.littlemadhav.network.APIClient;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.etechnie.littlemadhav.app.App;
import com.etechnie.littlemadhav.models.device_model.AppSettingsDetails;
import com.etechnie.littlemadhav.app.MyAppPrefsManager;

import com.etechnie.littlemadhav.R;

import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.utils.Utilities;
import com.etechnie.littlemadhav.network.StartAppRequests;
import com.paypal.android.sdk.payments.LoginActivity;

import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * SplashScreen activity, appears on App Startup
 **/

public class SplashScreen extends AppCompatActivity {

    View rootView;
    ProgressBar progressBar;

    MyTask myTask;
    StartAppRequests startAppRequests;
    MyAppPrefsManager myAppPrefsManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        
        Log.i("VC_Shop", "AndroidEcommerce_Version = "+ ConstantValues.CODE_VERSION);

        progressBar = findViewById(R.id.progressBar);
        rootView = (View) findViewById(R.id.mainView);
        // Intializing No internet Dialogue
        new NoInternetDialog.Builder(SplashScreen.this).build();
        //noInternetDialog.show();
        // Bind Layout Views

        // Initializing StartAppRequests and PreferencesManager
        startAppRequests = new StartAppRequests(this);
        myAppPrefsManager = new MyAppPrefsManager(this);


//      ConstantValues.IS_ADMOBE_ENABLED = true;
        ConstantValues.LANGUAGE_ID = myAppPrefsManager.getUserLanguageId();
        ConstantValues.LANGUAGE_CODE = myAppPrefsManager.getUserLanguageCode();
        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
        ConstantValues.IS_PUSH_NOTIFICATIONS_ENABLED = myAppPrefsManager.isPushNotificationsEnabled();
        ConstantValues.IS_LOCAL_NOTIFICATIONS_ENABLED = myAppPrefsManager.isLocalNotificationsEnabled();

        getAllBrands();
        getAllCategories();
        // Start MyTask after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myTask = new MyTask();
                myTask.execute();
            }
        }, 3000);

    }

    //*********** Sets App configuration ********//
    
    private void setAppConfig() {
        
        AppSettingsDetails appSettingsDetails = ((App) getApplicationContext()).getAppSettingsDetails();
        
        if (appSettingsDetails != null) {
            
            ConstantValues.DEFAULT_HOME_STYLE = getString(R.string.actionHome) +" "+ appSettingsDetails.getHomeStyle();
            ConstantValues.DEFAULT_CATEGORY_STYLE = getString(R.string.actionCategory) +" "+ appSettingsDetails.getCategoryStyle();
            ConstantValues.DEFAULT_PRODUCT_CARD_STYLE = (appSettingsDetails.getCardStyle() == null || appSettingsDetails.getCardStyle().isEmpty() ? 0 : Integer.parseInt(appSettingsDetails.getCardStyle()));
            ConstantValues.DEFAULT_BANNER_STYLE = (appSettingsDetails.getBannerStyle() == null || appSettingsDetails.getBannerStyle().isEmpty() ? 0 : Integer.parseInt(appSettingsDetails.getBannerStyle()));
            ConstantValues.MAINTENANCE_MODE = appSettingsDetails.getApp_web_environment();

            ConstantValues.CURRENCY_CODE = myAppPrefsManager.getCurrencyCode();
            ConstantValues.CURRENCY_SYMBOL = Utilities.getCurrencySymbol(ConstantValues.CURRENCY_CODE).replace("US","");
            
            if (appSettingsDetails.getAppName() != null  &&  !TextUtils.isEmpty(appSettingsDetails.getAppName())) {
//                ConstantValues.APP_HEADER = appSettingsDetails.getAppName();
                ConstantValues.APP_HEADER = getString(R.string.app_name);
            } else {
                ConstantValues.APP_HEADER = getString(R.string.app_name);
            }
            
            if(appSettingsDetails.getMaintenance_text()!=null &&  !TextUtils.isEmpty(appSettingsDetails.getMaintenance_text())){
                
                ConstantValues.MAINTENANCE_TEXT = appSettingsDetails.getMaintenance_text();
            }
            
            /*
            if (appSettingsDetails.getCurrencySymbol() != null  &&  !TextUtils.isEmpty(appSettingsDetails.getCurrencySymbol())) {
                ConstantValues.CURRENCY_SYMBOL = Utilities.getCurrencySymbol(appSettingsDetails.getCurrencySymbol());
            } else {
                ConstantValues.CURRENCY_SYMBOL = "₹";
            }
            */

            if (appSettingsDetails.getPacking_charge_tax() != null && !TextUtils.isEmpty(appSettingsDetails.getCurrencySymbol())) {
                ConstantValues.PACKING_CHARGE = appSettingsDetails.getPacking_charge_tax();
            }
                else {
                ConstantValues.PACKING_CHARGE = "0.0";
            }
            
            if (appSettingsDetails.getNewProductDuration() != null  &&  !TextUtils.isEmpty(appSettingsDetails.getNewProductDuration())) {
                ConstantValues.NEW_PRODUCT_DURATION = Long.parseLong(appSettingsDetails.getNewProductDuration());
            } else {
                ConstantValues.NEW_PRODUCT_DURATION = 30;
            }
    
            
            ConstantValues.IS_GOOGLE_LOGIN_ENABLED = (appSettingsDetails.getGoogleLogin().equalsIgnoreCase("1"));
            ConstantValues.IS_FACEBOOK_LOGIN_ENABLED = (appSettingsDetails.getFacebookLogin().equalsIgnoreCase("1"));
            ConstantValues.IS_ADD_TO_CART_BUTTON_ENABLED = (appSettingsDetails.getCartButton().equalsIgnoreCase("1"));
            
            ConstantValues.IS_ADMOBE_ENABLED = (appSettingsDetails.getAdmob().equalsIgnoreCase("1"));
            ConstantValues.ADMOBE_ID = appSettingsDetails.getAdmobId();
            ConstantValues.AD_UNIT_ID_BANNER = appSettingsDetails.getAdUnitIdBanner();
            ConstantValues.AD_UNIT_ID_INTERSTITIAL = appSettingsDetails.getAdUnitIdInterstitial();
            
            
            myAppPrefsManager.setLocalNotificationsTitle(appSettingsDetails.getNotificationTitle());
            myAppPrefsManager.setLocalNotificationsDuration(appSettingsDetails.getNotificationDuration());
            myAppPrefsManager.setLocalNotificationsDescription(appSettingsDetails.getNotificationText());
            
        }
        else {
//            ConstantValues.APP_HEADER = getString(R.string.app_name);
            ConstantValues.APP_HEADER = getResources().getString(R.string.app_name);
            
            ConstantValues.CURRENCY_SYMBOL = "$";
            ConstantValues.NEW_PRODUCT_DURATION = 30;
            ConstantValues.IS_ADMOBE_ENABLED = false;
            
            ConstantValues.IS_GOOGLE_LOGIN_ENABLED = true;
            ConstantValues.IS_FACEBOOK_LOGIN_ENABLED = true;
            ConstantValues.IS_ADD_TO_CART_BUTTON_ENABLED = true;
    
            ConstantValues.DEFAULT_HOME_STYLE = getString(R.string.actionHome) +" "+ 1;
            ConstantValues.DEFAULT_CATEGORY_STYLE = getString(R.string.actionCategory) +" "+ 1;
            ConstantValues.DEFAULT_PRODUCT_CARD_STYLE = 1;
            ConstantValues.DEFAULT_BANNER_STYLE = 1;
        }
        
    }



    /************* MyTask is Inner Class, that handles StartAppRequests on Background Thread *************/

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // Check for Internet Connection from the static method of Helper class
            if (Utilities.hasActiveInternetConnection(SplashScreen.this)) {

                // Call the method of StartAppRequests class to process App Startup Requests
                startAppRequests.StartRequests();

                return "1";
            } else {

                return "0";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("0")) {

                progressBar.setVisibility(View.GONE);

                // No Internet Connection
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.retry), new View.OnClickListener() {

                            // Handle the Retry Button Click
                            @Override
                            public void onClick(View v) {

                                progressBar.setVisibility(View.VISIBLE);

                                // Restart MyTask after 3 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        myTask = new MyTask();
                                        myTask.execute();
                                    }
                                }, 3000);
                            }
                        })
                        .show();

            }
            else {
                // Navigate to MainActivity
                if(myAppPrefsManager.isUserLoggedIn() && !myAppPrefsManager.getCurrentToken().equalsIgnoreCase("0")){

                        ConstantValues.Token=myAppPrefsManager.getCurrentToken();

                        Intent i = new Intent(SplashScreen.this, MainActivity.class);

                        startActivity(i);
                        finish();

                }
                else {
                    startActivity(new Intent(getBaseContext(), Login.class));
                    finish();
                }

            }
        }

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    private void getAllBrands(){
        Call<ResponseData<List<Brand_Model>>> call = APIClient.getInstance()
                .getAllBrands
                        (

                        );

        call.enqueue(new Callback<ResponseData<List<Brand_Model>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Brand_Model>>> call, retrofit2.Response<ResponseData<List<Brand_Model>>> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        // Products have been returned. Add Products to the ProductsListresponse.body().getData()+
                        ((App) getApplicationContext()).setBrand_list(response.body().getData());
                    }

                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }

                    // Hide the ProgressBar

                }
                else {
                    Toast.makeText(SplashScreen.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Brand_Model>>> call, Throwable t) {
                // Toast.makeText(App.getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAllCategories(){
        Call<ResponseData<List<Category_model>>> call = APIClient.getInstance()
                .getAllCategory
                        (

                        );

        call.enqueue(new Callback<ResponseData<List<Category_model>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Category_model>>> call, retrofit2.Response<ResponseData<List<Category_model>>> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        // Products have been returned. Add Products to the ProductsListresponse.body().getData()+
                        ((App) getApplicationContext()).setCategory_list(response.body().getData());
                    }

                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }

                    // Hide the ProgressBar

                }
                else {
                    Toast.makeText(SplashScreen.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Category_model>>> call, Throwable t) {
                // Toast.makeText(App.getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

}


