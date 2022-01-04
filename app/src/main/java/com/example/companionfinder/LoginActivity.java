package com.example.companionfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.companionfinder.Database.AppDatabase;

public class LoginActivity extends AppCompatActivity
{
    EditText login;
    EditText password;
    Button loginButton;
    Context context;
    Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        login = findViewById(R.id.editRegistrationLogin);
        password = findViewById(R.id.editRegistrationPassword);
        loginButton = (Button) findViewById(R.id.registrationButton);
        context = this;
        //Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.getText().toString() != "" || password.getText().toString() != ""){
                    if (checkUserPassword(password.getText().toString(), login.getText().toString())){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        String userName = login.getText().toString();
                        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                        int userId = db.userDao().getUserByLogin(userName).userId;
                        intent.putExtra("UserId", userId);
                        startActivity(intent);
                    }else {
                        Toast.makeText(context, "Incorrect data", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(context, "Please enter login and password!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //Go to registration
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public boolean checkUserPassword(String password, String user){
        AppDatabase db = AppDatabase.getInstance(this);
        return db.userDao().getUserByPassword(password,user) != null;
    }
}
