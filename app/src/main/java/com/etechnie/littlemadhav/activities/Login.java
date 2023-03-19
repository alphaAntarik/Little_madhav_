package com.etechnie.littlemadhav.activities;

import static com.etechnie.littlemadhav.app.App.getContext;

import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

import com.etechnie.littlemadhav.models.ResponseData;
import com.etechnie.littlemadhav.models.login_model.PostLogin;
import com.etechnie.littlemadhav.models.login_model.ResponseLogin;
import com.etechnie.littlemadhav.utils.JWTUtils;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.etechnie.littlemadhav.R;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import am.appwise.components.ni.NoInternetDialog;
import com.etechnie.littlemadhav.constant.ConstantValues;
import com.etechnie.littlemadhav.customs.DialogLoader;
import com.etechnie.littlemadhav.models.user_model.UserData;
import com.etechnie.littlemadhav.models.user_model.UserDetails;
import com.etechnie.littlemadhav.network.APIClient;
import com.etechnie.littlemadhav.network.StartAppRequests;
import com.etechnie.littlemadhav.app.MyAppPrefsManager;
import com.etechnie.littlemadhav.utils.LocaleHelper;
import com.etechnie.littlemadhav.utils.ValidateInputs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.etechnie.littlemadhav.databases.User_Info_DB;
import com.paypal.android.sdk.v;

/**
 * Login activity handles User's Email, Facebook and Google Login
 **/


public class Login extends AppCompatActivity {

    View parentView;
    Toolbar toolbar;
    ActionBar actionBar;

    EditText user_email, user_password;
    TextView forgotPasswordText, signupText;
    Button loginBtn, facebookLoginBtn, googleLoginBtn;


    User_Info_DB userInfoDB;
    DialogLoader dialogLoader;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    private CallbackManager callbackManager;


    private static final int RC_SIGN_IN = 100;


    private UserDetails userDetails;
    private Dialog dialogOTP;
    private boolean flag = true;
    EditText ed_otp;
    private int TEMP_USER_TYPE;
    private static String user_current_phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(Login.this).build();
        // noInternetDialog.show();


        setContentView(R.layout.login);


        // setting Toolbar
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.login));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Binding Layout Views
        user_email = (EditText) findViewById(R.id.user_email);
        user_password = (EditText) findViewById(R.id.user_password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        facebookLoginBtn = (Button) findViewById(R.id.facebookLoginBtn);
        googleLoginBtn = (Button) findViewById(R.id.googleLoginBtn);
        signupText = (TextView) findViewById(R.id.login_signupText);
        forgotPasswordText = (TextView) findViewById(R.id.forgot_password_text);

        parentView = signupText;


        if (ConstantValues.IS_GOOGLE_LOGIN_ENABLED) {
            googleLoginBtn.setVisibility(View.VISIBLE);
        } else {
            googleLoginBtn.setVisibility(View.GONE);
        }

        if (ConstantValues.IS_FACEBOOK_LOGIN_ENABLED) {
            facebookLoginBtn.setVisibility(View.VISIBLE);
        } else {
            facebookLoginBtn.setVisibility(View.GONE);
        }


        dialogLoader = new DialogLoader(Login.this);

        userInfoDB = new User_Info_DB();
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);


//        user_email.setText(sharedPreferences.getString("userEmail", null));


        // Handle on Forgot Password Click

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateLogin() && flag){
                    PostLogin p=new PostLogin();
                    flag = false;
                    p.setUsername(user_email.getText().toString());
                    RequestAllProducts(p);
                }
            }
        });



        
    }

    //*********** Called if Connection fails for Google Login ********//
    


    
    //*********** Receives the result from a previous call of startActivityForResult(Intent, int) ********//
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // Handle Activity Result

        }
        
        callbackManager.onActivityResult(requestCode, resultCode, data);
        
    }





public void RequestAllProducts(PostLogin p) {


    Call<ResponseData<ResponseLogin>> call = APIClient.getInstance()
            .login
                    (
                            p
                    );

    call.enqueue(new Callback<ResponseData<ResponseLogin>>() {
        @Override
        public void onResponse(Call<ResponseData<ResponseLogin>> call, Response<ResponseData<ResponseLogin>> response) {
            flag = true;
            if (response.isSuccessful()) {
                if (String.valueOf(response.body().getStatus()).equalsIgnoreCase("1")) {

                    ResponseLogin data =   response.body().getData();
                    try {
                        String body= JWTUtils.decoded(data.getAccessToken());

                        Intent i = new Intent(Login.this, VerifyPhoneActivity.class);
                        i.putExtra("data",body);
                        i.putExtra("type","login");
                        i.putExtra("token",data.getAccessToken());
                        i.putExtra("phone", user_email.getText().toString());
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    // Unable to get Success status
                    Toast.makeText(Login.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                }

            } else {
                // Show the Error Message
                String Str = response.message();
                Toast.makeText(Login.this, response.message(), Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onFailure(Call<ResponseData<ResponseLogin>> call, Throwable t) {
            dialogLoader.hideProgressDialog();
            flag = true;
            Toast.makeText(Login.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();


        }   });}



    private void showPhoneDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_phone, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        final Button okayButton = dialogView.findViewById(R.id.dialog_button);
        final EditText phoneEditText = dialogView.findViewById(R.id.dialog_input);
        if(!phoneEditText.getText().toString().isEmpty()) {
            phoneEditText.setText(phoneEditText.getText().toString());
        }
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateInputs.isValidPhoneNo(phoneEditText.getText().toString().trim())) {
                    
                    
                    // Request for OTP
                    sendOTP(getContext().getResources().getString(R.string.indian_code)+phoneEditText.getText().toString().trim());
                    user_current_phone_number = getResources().getString(R.string.indian_code)+phoneEditText.getText().toString().trim();
                }
                else {Snackbar.make(parentView, getString(R.string.invalid_contact), Snackbar.LENGTH_LONG).show(); }
                alertDialog.dismiss();
            }
        });
    }



    private void showOTPDialog(final String phoneNumber) {
        dialogOTP = new Dialog(Login.this);
        dialogOTP.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOTP.setCancelable(true);
        dialogOTP.setContentView(R.layout.dialog_otp);

        ed_otp = dialogOTP.findViewById(R.id.ed_otp);
        AppCompatButton btn_resend,btn_submit;
        btn_resend = dialogOTP.findViewById(R.id.btn_resend);
        btn_submit = dialogOTP.findViewById(R.id.btn_submit);

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { sendOTP(phoneNumber); }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  }
        });
        dialogOTP.show();
    }



private void sendOTP(String otp){

}



    private void loginUser(UserDetails userDetails){
        // Save User Data to Local Databases
        if (userInfoDB.getUserData(userDetails.getId()) != null) {
            // User already exists
            userInfoDB.updateUserData(userDetails);
        } else {
            // Insert Details of New User
            userInfoDB.insertUserData(userDetails);
        }

        // Save necessary details in SharedPrefs
        editor = sharedPreferences.edit();
        editor.putString("userID", userDetails.getId());
        editor.putString("userEmail", userDetails.getEmail());
        editor.putString("userName", userDetails.getFirstName()+" "+userDetails.getLastName());
        editor.putString("userTelephone", user_current_phone_number);
        editor.putString("userDefaultAddressID", userDetails.getDefaultAddressId());
        editor.putBoolean("isLogged_in", true);
        editor.commit();

        // Set UserLoggedIn in MyAppPrefsManager
        MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(Login.this);
        myAppPrefsManager.setUserLoggedIn(true);

        // Set isLogged_in of ConstantValues
        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
        StartAppRequests.RegisterDeviceForFCM(Login.this);
        // Navigate back to MainActivity
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }

    private void loginGmailUser(UserDetails userDetails){
        // Save User Data to Local Databases
        if (userInfoDB.getUserData(userDetails.getId()) != null) {
            // User already exists
            userInfoDB.updateUserData(userDetails);
        } else {
            // Insert Details of New User
            userInfoDB.insertUserData(userDetails);
        }

        // Save necessary details in SharedPrefs
        editor = sharedPreferences.edit();
        editor.putString("userID", userDetails.getId());
        editor.putString("userEmail", userDetails.getEmail());
        editor.putString("userName", userDetails.getFirstName()+" "+userDetails.getLastName());
        editor.putString("userTelephone", user_current_phone_number);
        editor.putString("userDefaultAddressID", userDetails.getDefaultAddressId());
        editor.putBoolean("isLogged_in", true);
        editor.commit();

        // Set UserLoggedIn in MyAppPrefsManager
        MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(Login.this);
        myAppPrefsManager.setUserLoggedIn(true);

        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();

        StartAppRequests.RegisterDeviceForFCM(Login.this);


        // Navigate back to MainActivity
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }

    private void loginFacebookUser(UserDetails userDetails){
        // Save User Data to Local Databases
        if (userInfoDB.getUserData(userDetails.getId()) != null) {
            // User already exists
            userInfoDB.updateUserData(userDetails);
        } else {
            // Insert Details of New User
            userInfoDB.insertUserData(userDetails);
        }

        // Save necessary details in SharedPrefs
        editor = sharedPreferences.edit();
        editor.putString("userID", userDetails.getId());
        editor.putString("userEmail", userDetails.getEmail());
        editor.putString("userName", userDetails.getFirstName()+" "+userDetails.getLastName());
        editor.putString("userTelephone", user_current_phone_number);
        editor.putString("userDefaultAddressID", userDetails.getDefaultAddressId());
        editor.putBoolean("isLogged_in", true);
        editor.commit();
        // Set UserLoggedIn in MyAppPrefsManager
        MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(Login.this);
        myAppPrefsManager.setUserLoggedIn(true);

        // Set isLogged_in of ConstantValues
        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();

        StartAppRequests.RegisterDeviceForFCM(Login.this);

        // Navigate back to MainActivity
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }

    //*********** Proceed Forgot Password Request ********//

    private void processForgotPassword(String email) {
    
        dialogLoader.showProgressDialog();

        Call<UserData> call = APIClient.getInstance()
                .processForgotPassword
                        (
                                email
                        );

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
    
                dialogLoader.hideProgressDialog();
                
                if (response.isSuccessful()) {
                    // Show the Response Message
                    String message = response.body().getMessage();
                    Snackbar.make(parentView, message, Snackbar.LENGTH_LONG).show();

                } else {
                    // Show the Error Message
                    Toast.makeText(Login.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(Login.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }



    //*********** Proceed Facebook Registration Request ********//

    private void processFacebookRegistration(String access_token) {

        dialogLoader.showProgressDialog();
        Log.i("VC_Shop", "[FacebookRegistration] > access_token"+access_token);
    

        Call<UserData> call = APIClient.getInstance()
                .facebookRegistration
                        (
                                access_token
                        );

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                dialogLoader.hideProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1") || response.body().getSuccess().equalsIgnoreCase("2"))
                    {
                        // Get the User Details from Response
                        userDetails = response.body().getData().get(0);
                        TEMP_USER_TYPE = 2; // 2 for Facebook Login.
                        //showPhoneDialog();
                        loginFacebookUser(userDetails);
                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        // Get the Error Message from Response
                        String message = response.body().getMessage();
                        Snackbar.make(parentView, message, Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Login.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Show the Error Message
                    Toast.makeText(Login.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(Login.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    
    //*********** Proceed Google Registration Request ********//

    private void processGoogleRegistration(GoogleSignInAccount account) {

        dialogLoader.showProgressDialog();

        String accountStr =  account.getIdToken();
        String accountID = account.getId();
        String name = account.getGivenName();
        String familyName = account.getFamilyName();
        String email =  account.getEmail();

        String photoURL = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";

        Call<UserData> call = APIClient.getInstance()
                .googleRegistration
                        (
                                account.getIdToken(),
                                account.getId(),
                                account.getGivenName(),
                                account.getFamilyName(),
                                account.getEmail(),
                                photoURL
                        );


        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                String gson = response.raw().body().toString();

                dialogLoader.hideProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1") || response.body().getSuccess().equalsIgnoreCase("2")) {

                        String gsonStr = new Gson().toJson(response.body().getData().get(0));
                        userDetails = response.body().getData().get(0);
                        TEMP_USER_TYPE = 1; // 1 for Gmail login.
                        //showPhoneDialog();
                        loginGmailUser(userDetails);
                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(parentView, response.body().getMessage(), Snackbar.LENGTH_SHORT).show();
                        
                    }
                    else {
                        Toast.makeText(Login.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                    }
                    
                }
                else {
                    // Show the Error Message
                    Toast.makeText(Login.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(Login.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    
    //*********** Validate Login Form Inputs ********//

    private boolean validateLogin() {
        if (!ValidateInputs.isValidPhoneNo(user_email.getText().toString().trim())) {
            user_email.setError(getString(R.string.email_phone_invalid));
            return false;
        }

        else {

            return true;
        }
    }
    
    
    //*********** Set the Base Context for the ContextWrapper ********//
    
    @Override
    protected void attachBaseContext(Context newBase) {
    
        String languageCode = ConstantValues.LANGUAGE_CODE;
        if ("".equalsIgnoreCase(languageCode))
            languageCode = ConstantValues.LANGUAGE_CODE = "en";
    
        super.attachBaseContext(LocaleHelper.wrapLocale(newBase, languageCode));
    }
    
    
    //*********** Called when the Activity has detected the User pressed the Back key ********//
    
    @Override
    public void onBackPressed() {
        
        // Navigate back to MainActivity
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

