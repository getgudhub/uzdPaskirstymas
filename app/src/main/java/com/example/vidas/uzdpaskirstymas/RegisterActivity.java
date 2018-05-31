package com.example.vidas.uzdpaskirstymas;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.register_label);

        final EditText etUsername = findViewById(R.id.username);
        final EditText etPassword = findViewById(R.id.password);
        final EditText etPasswordR = findViewById(R.id.passwordR);
        final EditText etFullName = findViewById(R.id.fullName);
        final EditText etEmail = findViewById(R.id.email);

        Button btnregister = findViewById(R.id.btnRegister);

        final DatabaseSQLiteUser dbSQLite = new DatabaseSQLiteUser(this);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUsername.getText().toString().isEmpty() || !Validation.isValidCredentials(etUsername.getText().toString())){
                    etUsername.requestFocus();
                    etUsername.setError(getResources().getString(R.string.invalid_username));
                }else if(dbSQLite.checkName(etUsername.getText().toString())){
                    etUsername.requestFocus();
                    etUsername.setError(getResources().getString(R.string.invalid_username_taken));
                }else if(etPassword.getText().toString().isEmpty() || !Validation.isValidCredentials(etPassword.getText().toString())) {
                    etPassword.requestFocus();
                    etPassword.setError(getResources().getString(R.string.invalid_password));
                }else if(!etPasswordR.getText().toString().equals(etPassword.getText().toString())) {
                    etPasswordR.requestFocus();
                    etPasswordR.setError(getResources().getString(R.string.invalid_passwordR));
                }else if(etFullName.getText().toString().isEmpty() || !Validation.isValidFullName(etFullName.getText().toString()) ||
                        dbSQLite.checkFullName(etFullName.getText().toString())) {
                        etFullName.requestFocus();
                        etFullName.setError(getResources().getString(R.string.invalid_fullName));
                }else if(etEmail.getText().toString().isEmpty() || !Validation.isValidEmail(etEmail.getText().toString())) {
                    etEmail.requestFocus();
                    etEmail.setError(getResources().getString(R.string.invalid_email));
                }else {
                    System.out.println(etUsername.getText().toString());

                        User user = new User(1,etUsername.getText().toString(),
                                etPassword.getText().toString(), etFullName.getText().toString(), etEmail.getText().toString());
                        dbSQLite.addUser(user);


                    Toast.makeText(RegisterActivity.this,
                            "Ačiū, kad užsiregistravote "+ etUsername.getText().toString(),
                            Toast.LENGTH_LONG).show();

                    Intent toLoginActivity = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(toLoginActivity);
                    RegisterActivity.this.finish();
                    dbSQLite.close();

                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        RegisterActivity.this.finish();
    }
}
