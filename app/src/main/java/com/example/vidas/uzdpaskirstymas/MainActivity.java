package com.example.vidas.uzdpaskirstymas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.login_label);

        final User user = new User(MainActivity.this);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        final EditText etUsername = findViewById(R.id.username);
        final EditText etPassword = findViewById(R.id.password);

        final CheckBox cbRememberMe = findViewById(R.id.rememberMe);
        cbRememberMe.setChecked(user.isRemembered());

        etUsername.setError(null);
        etPassword.setError(null);

        if (user.isRemembered()) {
            etUsername.setText(user.getUsernameForLogin(), TextView.BufferType.EDITABLE);
            etPassword.setText(user.getPasswordForLogin(), TextView.BufferType.EDITABLE);
        } else {
            etUsername.setText("", TextView.BufferType.EDITABLE);
            etPassword.setText("", TextView.BufferType.EDITABLE);
        }

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent goToRegisterActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToRegisterActivity);
                MainActivity.this.finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                DatabaseSQLiteUser db = new DatabaseSQLiteUser(getApplicationContext());

                if(!Validation.isValidCredentials(etUsername.getText().toString())){
                    valid = false;
                    etUsername.requestFocus();
                    etUsername.setError(getResources().getString(R.string.login_invalid_username));
                }else if(!Validation.isValidCredentials(etPassword.getText().toString())){
                    valid = false;
                    etPassword.requestFocus();
                    etPassword.setError(getResources().getString(R.string.login_invalid_password));
                }else{

                    if(db.isValidUser(etUsername.getText().toString(), etPassword.getText().toString())){
                        valid = true;
                    }else {
                        valid = false;
                        Toast.makeText(MainActivity.this, "Tokio vartotojo duomenų bazėje nėra",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                if(valid){
                    user.setUsernameForLogin(etUsername.getText().toString());
                    user.setPasswordForLogin(etPassword.getText().toString());
                    if(cbRememberMe.isChecked()){
                        user.setRememberMeKeyForLogin(true);
                    }else {
                        user.setRememberMeKeyForLogin(false);
                    }

                    int userlevel;
                    userlevel = db.isAdmin(etUsername.getText().toString()); //userlevel patikrinimas

                    Intent intent = new Intent(MainActivity.this, TableActivity.class);
                    intent.putExtra("userlevel", userlevel);
                    intent.putExtra("username", etUsername.getText().toString());
                    startActivity(intent);
                    MainActivity.this.finish();
                    db.close();
                }
            }
        });
    }
}
