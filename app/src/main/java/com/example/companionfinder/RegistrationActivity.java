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
import com.example.companionfinder.Model.User;

public class RegistrationActivity extends AppCompatActivity {
    Button register;
    EditText login;
    EditText password;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        login = findViewById(R.id.editRegistrationLogin);
        password = findViewById(R.id.editRegistrationPassword);
        register = findViewById(R.id.registrationButton);
        context = this;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getInstance(context);
                if(login.getText().toString().length() > 3 && password.getText().toString().length() > 5 && db.userDao().getUserByLogin(login.getText().toString()) == null){
                    User user = new User();
                    user.name = login.getText().toString();
                    user.password = password.getText().toString();
                    db.userDao().insert(user);
                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(context, "Incorrect data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
