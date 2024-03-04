package com.pedro.streamer.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.pedro.streamer.MainActivity;
import com.pedro.streamer.R;
import com.pedro.streamer.auth.vm.LoginViewModel;
import com.pedro.streamer.data.local.SessionManager;
import com.pedro.streamer.databinding.ActivityLoginScreenBinding;
import com.pedro.streamer.utils.Utility;

public class LoginScreen extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Context context;
    private ActivityLoginScreenBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_login_screen);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen);
        context = this;
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }

        binding.setViewModel(loginViewModel);

        initView();

    }

    private void initView() {

        binding.tvGoToRegister.setOnClickListener(view -> {

                Intent intent = new Intent(context, RegisterScreen.class);
                startActivity(intent);

        });
        binding.tvForgotpass.setOnClickListener(view -> {

                Intent intent = new Intent(context, ForgetPsd.class);
                startActivity(intent);

        });
        binding.btnSignin.setOnClickListener(view -> {
            if (isValidForm()) {
               loginViewModel.login(context);
               /* Intent intent = new Intent(context, HomeScreen.class);
                startActivity(intent);*/
            }
        });





if(!SessionManager.getAuthenticationToken(context).toString().equals("") && !SessionManager.getAuthenticationToken(context).toString().equals("")  ) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivity(intent);

}

    }


    private boolean isValidForm() {
        boolean isValid = true;
        loginViewModel.email.setValue(binding.etEmail.getText().toString());
        loginViewModel.password.setValue(binding.etPsd.getText().toString());

        String email = loginViewModel.email.getValue();
         String password = loginViewModel.password.getValue();

        if (!Utility.isCheckEmptyOrNull(email)) {
            isValid = false;
           Utility.toast(this, "Please enter email address");
            //  Utility.setErrorBg(context, "Please enter email address", binding.tvEmailError, binding.llEmail);
        } else if (!Utility.isValidEmail(email)) {
            isValid = false;
            //  Utility.setErrorBg(context, "Please enter valid email address", binding.tvEmailError, binding.llEmail);
            Utility.toast(this, "Please enter valid email address");
        } else if (!Utility.isCheckEmptyOrNull(password)) {
            isValid = false;
            //  Utility.setErrorBg(context, "Please enter valid email address", binding.tvEmailError, binding.llEmail);
            Utility.toast(this, "Please enter password");
        }
        else   if (!Utility.isValidPassword(password)) {
            isValid = false;
            //         Utility.setErrorBg(context, "Please enter password", binding.tvPasswordError, binding.llPassword);
            Utility.toast(this, "Please enter password at least 8 digit, including uppercase, lowercase, number and special character");
        }

        return isValid;
    }


}