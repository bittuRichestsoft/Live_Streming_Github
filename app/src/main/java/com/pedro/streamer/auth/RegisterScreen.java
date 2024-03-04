package com.pedro.streamer.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.pedro.streamer.R;
import com.pedro.streamer.auth.vm.LoginViewModel;
import com.pedro.streamer.auth.vm.RegisterViewModel;
import com.pedro.streamer.databinding.ActivityLoginScreenBinding;
import com.pedro.streamer.databinding.ActivityRegisterScreenBinding;
import com.pedro.streamer.utils.Utility;

public class RegisterScreen extends AppCompatActivity {
    private static final String TAG = "RegisterScreen ";
    private Context context;
    private ActivityRegisterScreenBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_register_screen);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_screen);
        context = this;
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.setViewModel(registerViewModel);

        initView();


    }

    private void initView() {

        binding.tvGoToLogin.setOnClickListener(view -> {

            Intent intent = new Intent(context, LoginScreen.class);
            startActivity(intent);
finish();
        });
        binding.tvTermCond.setOnClickListener(view -> {

            String url = "https://www.facebook.com";
             Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
   try{
                 startActivity(intent);
            } catch (Exception  excep)  {
Utility.toast(context,"Terms and condition link");
            }
        });

        binding.tvPrivacyPolicy.setOnClickListener(view -> {
            String url = "https://www.google.com";
             Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
   try {
       startActivity(intent);
   }

   catch (Exception  excep) {
       Utility.toast(context, "Privacy policy link");
   }
        });


        binding.btnRegister.setOnClickListener(view -> {
            if (isValidForm()) {
                registerViewModel.signup(context);
           /*     Intent intent = new Intent(context, HomeScreen.class);
                startActivity(intent);*/
            }
        });
    }

    private boolean isValidForm() {
        boolean isValid = true;
        registerViewModel.preferredName.setValue(binding.etName.getText().toString());
        registerViewModel.email.setValue(binding.etEmail.getText().toString());
        registerViewModel.phone.setValue(binding.etPhone.getText().toString());
        registerViewModel.password.setValue(binding.etPsd.getText().toString());
        registerViewModel.confirmPassword.setValue(binding.etConPsd.getText().toString());

        String email = registerViewModel.email.getValue();
        String name = registerViewModel.preferredName.getValue();
        String password = registerViewModel.password.getValue();
        String confirmPasswordValue = registerViewModel.confirmPassword.getValue();
        String phone = registerViewModel.phone.getValue();
        boolean chkBoxVal = binding.checkbox.isChecked();

        if (!Utility.isCheckEmptyOrNull(name)) {
            isValid = false;
            Utility.toast(this, "Please enter name");
            //  Utility.setErrorBg(context, "Please enter email address", binding.tvEmailError, binding.llEmail);
        } else   if (!Utility.isCheckEmptyOrNull(email)) {
            isValid = false;
            Utility.toast(this, "Please enter email address");
            //  Utility.setErrorBg(context, "Please enter email address", binding.tvEmailError, binding.llEmail);
        } else if (!Utility.isValidEmail(email)) {
            isValid = false;
            //  Utility.setErrorBg(context, "Please enter valid email address", binding.tvEmailError, binding.llEmail);
            Utility.toast(this, "Please enter valid email address");
        }
    /*  else  if (!Utility.isValidPassword(password)) {
            isValid = false;
            //         Utility.setErrorBg(context, "Please enter password", binding.tvPasswordError, binding.llPassword);
            Utility.toast(this, "Please enter password at least 8 digit, including uppercase, lowercase, number and special character");

        }
        else  if (!Utility.isValidPassword(confirmPasswordValue)) {
            isValid = false;
            //         Utility.setErrorBg(context, "Please enter password", binding.tvPasswordError, binding.llPassword);
            Utility.toast(this, "Please enter confirm password at least 8 digit, including uppercase, lowercase, number and special character");
        }   else  if (!password.equals(confirmPasswordValue)) {
            isValid = false;
            //         Utility.setErrorBg(context, "Please enter password", binding.tvPasswordError, binding.llPassword);
            Utility.toast(this, "Confirm password is not matched");
        }else  if (!chkBoxVal) {
            isValid = false;
            //         Utility.setErrorBg(context, "Please enter password", binding.tvPasswordError, binding.llPassword);
            Utility.toast(this, "Please agree to terms and conditions");
        }*/

        else if (!Utility.isCheckEmptyOrNull(phone) || phone.length()!=10 ) {
            isValid = false;
            //  Utility.setErrorBg(context, "Please enter valid email address", binding.tvEmailError, binding.llEmail);
            Utility.toast(this, "Please enter valid 10 digit phone number");
        }

        return isValid;
    }


}