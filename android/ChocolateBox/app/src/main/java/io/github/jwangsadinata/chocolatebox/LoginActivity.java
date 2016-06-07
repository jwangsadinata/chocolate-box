package io.github.jwangsadinata.chocolatebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    // Instantiating canvas objects
    @Bind(R.id.login_progress)
    protected ProgressBar login_progress;

    @Bind(R.id.etLoginUserName)
    protected EditText etUserName;

    @Bind(R.id.etLoginPassword)
    protected EditText etPassword;

    @Bind(R.id.btnToRegister)
    protected Button btnRegister;

    @Bind(R.id.btnLogin)
    protected Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind the Butterknife Library
        ButterKnife.bind(this);

        // Logout user just in case the previous user forgot to do so
        ParseUser.logOut();
    }

    // Register button -> navigate to the RegisterActivity page
    @OnClick(R.id.btnToRegister)
    protected void registerUser() {
        Intent intentStartRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intentStartRegisterActivity);
        finish();
    }

    // Login button -> login the user to the MainActivity if they use the correct login information
    @OnClick(R.id.btnLogin)
    protected void loginUser() {
        login_progress.setVisibility(View.VISIBLE);

        // Use the parse service to login
        ParseUser.logInInBackground(etUserName.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                login_progress.setVisibility(View.GONE);
                if (e == null) {
                    Toast.makeText(LoginActivity.this,
                            "Login Success",
                            Toast.LENGTH_SHORT).show();

                    // If successful, start the MainActivity
                    Intent intentStartMainActivity = new Intent(
                            LoginActivity.this, MainActivity.class);
                    startActivity(intentStartMainActivity);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Login failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
