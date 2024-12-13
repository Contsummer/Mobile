package com.example.doanmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmobile.Model.LoginRequest;
import com.example.doanmobile.Model.LoginRespon;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    loginUser(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

                    private void loginUser(String username, String password) {
                        LoginRequest login  = new LoginRequest(username,password);
                        Api apiService = RetrofitClient.getApiService();
                        Call<LoginRespon> call = apiService.Login(login);
                        call.enqueue(new Callback<LoginRespon>() {
                            @Override
                            public void onResponse(Call<LoginRespon> call, Response<LoginRespon> response) {
                                Log.d("response", "onResponse: " + response.body().getStatus()) ;
                                Log.d("response", "onResponse: " + response.body().getName()) ;
                                Log.d("response", "onResponse: " + response.body().getId()) ;
                                if (response.isSuccessful() && response.body() != null) {
                                    LoginRespon loginResponse = response.body();
                                    Log.d("response", "onResponse: " + loginResponse.getStatus());
                                    if ("success".equals(loginResponse.getStatus())) {
                                        navigateToHomeActivity();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (response.body() == null) {
                                    Toast.makeText(LoginActivity.this, "No login data found", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }
            @Override
            public void onFailure(Call<LoginRespon> call, Throwable t) {
                    Log.d("Loi ket noi   ", "onFailure:" + t.getMessage());
                    Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
