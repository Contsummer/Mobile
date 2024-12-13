package com.example.doanmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmobile.Model.LoginRequest;
import com.example.doanmobile.Model.LoginRespon;
import com.example.doanmobile.Model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsernameRegister, edtEmailRegister, edtPasswordRegister, edtPasswordRegister2;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsernameRegister = findViewById(R.id.edtUsernameRegister);
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtPasswordRegister2 = findViewById(R.id.edtPasswordRegister2);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsernameRegister.getText().toString();
                String email = edtEmailRegister.getText().toString();
                String password = edtPasswordRegister.getText().toString();
                String verifyPassword = edtPasswordRegister2.getText().toString();
                User user = new User(username,email,password);
                if (!password.equals(verifyPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Invalid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                register(user);
            }
        });
    }


    private void register(User user){
        Api apiService = RetrofitClient.getApiService();
        Call<LoginRespon> call = apiService.Register(user);
        call.enqueue(new Callback<LoginRespon>(){

            @Override
            public void onResponse(Call<LoginRespon> call, Response<LoginRespon> response) {
            if(response.body() !=null){
                LoginRespon loginResponse = response.body();
                if(!loginResponse.getStatus().equals("success")) {
                    Toast.makeText(RegisterActivity.this, "Lỗi đăng ký , email hoặc user name đã tồn tại  ", Toast.LENGTH_SHORT).show();
                    return;
                } ;
                Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                navigateToHomeActivity();
            }
            }

            @Override
            public void onFailure(Call<LoginRespon> call, Throwable t) {

            }
        });

    }
    private void navigateToHomeActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
