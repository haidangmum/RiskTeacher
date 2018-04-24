package com.riskteacher.teamcoin.riskteacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUser extends AppCompatActivity {

    private EditText firstname_edittext, lastname_edittext, email_edittext, password_edittext;
    private Button create_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        firstname_edittext = findViewById(R.id.firstname_edittext);
        lastname_edittext = findViewById(R.id.lastname_edittext);
        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        create_button = findViewById(R.id.create_button);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(firstname_edittext.getText()) ||
                        TextUtils.isEmpty(lastname_edittext.getText()) ||
                        TextUtils.isEmpty(email_edittext.getText()) ||
                        TextUtils.isEmpty(password_edittext.getText()))
                    Toast.makeText(CreateUser.this, "All fields must not be blank!!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(CreateUser.this, "Account created successfully!!!", Toast.LENGTH_LONG).show();

                    Intent resultIntent = new Intent();
                    String email = email_edittext.getText().toString();
                    String password = password_edittext.getText().toString();
                    resultIntent.putExtra("email", email);
                    resultIntent.putExtra("password", password);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

}
