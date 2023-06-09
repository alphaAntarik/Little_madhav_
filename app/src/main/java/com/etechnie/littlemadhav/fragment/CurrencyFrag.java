package com.etechnie.littlemadhav.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.material.snackbar.Snackbar;
import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.activities.MainActivity;
import com.etechnie.littlemadhav.adapters.CurrencyAdapter;
import com.etechnie.littlemadhav.app.App;
import com.etechnie.littlemadhav.app.MyAppPrefsManager;
import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.customs.DialogLoader;
import com.etechnie.littlemadhav.models.banner_model.BannerDetails;
import com.etechnie.littlemadhav.models.category_model.CategoryDetails;
import com.etechnie.littlemadhav.models.currency_model.CurrencyList;
import com.etechnie.littlemadhav.models.currency_model.CurrencyModel;
import com.etechnie.littlemadhav.network.APIClient;
import com.etechnie.littlemadhav.network.StartAppRequests;
import com.etechnie.littlemadhav.utils.Utilities;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Muhammad Nabeel on 08/03/2019.
 */
public class CurrencyFrag extends Fragment {
    
    View rootView;
    
    MyAppPrefsManager appPrefs;
    
    String selectedLanguageID;

    Button saveCurrencyBtn;
    ListView currencyListView;
    FrameLayout banner_adView;
    
    CurrencyAdapter currencyAdapter;
    List<CurrencyList> currencyLists;
    DialogLoader dialogLoader;
    private CheckBox lastChecked_CB = null;
    
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_currency, container, false);
        
        //MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).toggleNavigaiton(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.currency));
        
        
        appPrefs = new MyAppPrefsManager(getContext());
        
        selectedLanguageID = appPrefs.getCurrencyCode();
        dialogLoader = new DialogLoader(getContext());
        
        // Binding Layout Views
        banner_adView = (FrameLayout) rootView.findViewById(R.id.banner_adView);
        saveCurrencyBtn = (Button) rootView.findViewById(R.id.btn_save_currency);
        currencyListView = (ListView) rootView.findViewById(R.id.currency_list);
        
        
        if (ConstantValues.IS_ADMOBE_ENABLED) {
            // Initialize Admobe

        }
        
        
        currencyLists = new ArrayList<>();
        
        // Request Languages
        RequestCurrency();
        
        // Initialize the LanguagesAdapter for RecyclerView
        currencyAdapter = new CurrencyAdapter(getContext(), currencyLists, this);
    
        currencyListView.setAdapter(currencyAdapter);
        
        
        saveCurrencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (!selectedLanguageID.equalsIgnoreCase(String.valueOf(appPrefs.getCurrencyCode()))) {
                    // Change Language
                    
                    appPrefs.setCurrencyCode(selectedLanguageID);
                   // appPrefs.setUserLanguageId(Integer.parseInt(selectedLanguageID));
                    
                    ConstantValues.CURRENCY_CODE = appPrefs.getCurrencyCode();
                    ConstantValues.CURRENCY_SYMBOL = Utilities.getCurrencySymbol(appPrefs.getCurrencyCode());
                    
                  //  ConstantValues.LANGUAGE_CODE = appPrefs.getUserLanguageCode();
                    
                    
                    ChangeLocaleTask changeLocaleTask = new ChangeLocaleTask();
                    changeLocaleTask.execute();
                    
                }
            }
        });
        
        return rootView;
    }
    

    
    //*********** Recreates Activity ********//
    
    private void recreateActivity() {
        My_Cart.ClearCart();
        ((App) getContext().getApplicationContext()).setBannersList(new ArrayList<BannerDetails>());
        ((App) getContext().getApplicationContext()).setCategoriesList(new ArrayList<CategoryDetails>());
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
    
    public void setLastCheckedCB(CheckBox lastChecked_CB) {
        this.lastChecked_CB = lastChecked_CB;
    }
    
    public String getSelectedLanguageID() {
        return selectedLanguageID;
    }
    
    //*********** Adds Orders returned from the Server to the OrdersList ********//
    
    private void addLanguages(CurrencyModel languageData) {
        
        currencyLists.addAll(languageData.getData());
        
        if (selectedLanguageID.equalsIgnoreCase("") && currencyLists.size() != 0) {
    
            selectedLanguageID = currencyLists.get(0).getTitle();
           // selectedLanguageCode = languagesList.get(0).getCode();

            //TODO: CHECK IF IT WORK GOOD OR NOT.

            for (int i=0;  i<currencyLists.size();  i++) {
                if (currencyLists.get(i).getIsDefault()==1) {
                    selectedLanguageID = currencyLists.get(i).getTitle();
                   // selectedLanguageID = languagesList.get(i).getLanguagesId();
                }
            }
            
        }
        else {
            for (int i=0;  i<currencyLists.size();  i++) {
                if (currencyLists.get(i).getTitle().equalsIgnoreCase(String.valueOf(appPrefs.getCurrencyCode()))) {
                    selectedLanguageID = currencyLists.get(i).getTitle();
                   // selectedLanguageID = languagesList.get(i).getLanguagesId();
                }
            }
        }
        
        
        currencyAdapter.notifyDataSetChanged();
    
    
        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                CheckBox currentChecked_CB = (CheckBox) view.findViewById(R.id.cb_currency);
                CurrencyList currencyList = (CurrencyList) parent.getAdapter().getItem(position);
                
                
                // UnCheck last Checked CheckBox
                if (lastChecked_CB != null) {
                    lastChecked_CB.setChecked(false);
                }
                
                currentChecked_CB.setChecked(true);
                lastChecked_CB = currentChecked_CB;
                
                
                selectedLanguageID = currencyList.getCode();
               // selectedLanguageCode = language.getCode();
            }
        });
        
    }
    
    //*********** Request App Languages from the Server ********//
    
    public void RequestCurrency() {
        dialogLoader.showProgressDialog();
        Call<CurrencyModel> call = APIClient.getInstance()
                .getCurrency();
        
        call.enqueue(new Callback<CurrencyModel>() {
            @Override
            public void onResponse(Call<CurrencyModel> call, retrofit2.Response<CurrencyModel> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        
                        // Languages have been returned. Add Languages to the languageList
                        addLanguages(response.body());
                        dialogLoader.hideProgressDialog();
                        
                    }
                    
                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                        dialogLoader.hideProgressDialog();
                    }
                }
                else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    dialogLoader.hideProgressDialog();
                }
            }
            
            @Override
            public void onFailure(Call<CurrencyModel> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                dialogLoader.hideProgressDialog();
            }
        });
    }
    
    private class ChangeLocaleTask extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoader.showProgressDialog();
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            
            // Recall some Network Requests
            StartAppRequests startAppRequests = new StartAppRequests(getContext());
            startAppRequests.StartRequests();
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            
            dialogLoader.hideProgressDialog();
            recreateActivity();
        }
    }

}
