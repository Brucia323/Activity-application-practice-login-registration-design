package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button loginB, newUser;
    EditText usernameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loginB.setOnClickListener(v -> Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show());
        newUser.setOnClickListener(v -> {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName(MainActivity.this, MainActivity2.class);
            intent.setComponent(componentName);
            startActivity(intent);
        });
    }

    /**
     * 初始化视图绑定
     */
    void initView() {
        loginB = findViewById(R.id.login);
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        newUser = findViewById(R.id.newUser);
    }
}