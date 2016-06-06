package io.github.jwangsadinata.chocolatebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    // Instantiating canvas objects
    @Bind(R.id.register_progress)
    protected ProgressBar register_progress;

    @Bind(R.id.etRegisterFullName)
    protected EditText etFullName;

    @Bind(R.id.etRegisterUsername)
    protected EditText etUserName;

    @Bind(R.id.etRegisterEmailAddress)
    protected EditText etEmail;

    @Bind(R.id.etRegisterPassword)
    protected EditText etPassword;

    @Bind(R.id.btnRegister)
    protected Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind the Butterknife library
        ButterKnife.bind(this);
    }

    // Register the user.
    @OnClick(R.id.btnRegister)
    protected void registerUser() {
        ParseUser user = new ParseUser();
        user.setUsername(etUserName.getText().toString());
        user.setPassword(etPassword.getText().toString());

        // Error checking
        if ("".equals(etFullName.getText().toString())) {
            etFullName.setError("This field cannot be empty");
        }
        else if ("".equals(etUserName.getText().toString())) {
            etUserName.setError("This field cannot be empty");
        }
        else if ("".equals(etEmail.getText().toString())) {
            etEmail.setError("This email address is invalid");
        }
        else if ("".equals(etPassword.getText().toString())) {
            etPassword.setError("Password cannot be empty");
        }

        else {
            register_progress.setVisibility(View.VISIBLE);

            // Use parse server to create a user object and register
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    register_progress.setVisibility(View.GONE);

                    if (e == null) {
                        Toast.makeText(RegisterActivity.this, "Registration Success",
                                Toast.LENGTH_SHORT).show();

                        // If successful, start the mainActivity
                        Intent intentStartMainActivity = new Intent(
                                RegisterActivity.this, MainActivity.class);
                        startActivity(intentStartMainActivity);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Also, create a new class called Matching, containing the relevant user information.
        ParseObject object = new ParseObject(MainActivity.TABLE_USER_MATCH);
        object.put(MainActivity.KEY_USERNAME, etUserName.getText().toString());
        object.put(MainActivity.KEY_FULLNAME, etFullName.getText().toString());
        object.put(MainActivity.KEY_EMAIL_ADDRESS, etEmail.getText().toString());
        object.put(MainActivity.KEY_MATCHED_USER, "");
        object.put(MainActivity.KEY_HAS_MATCHED, false);
        object.saveInBackground();
    }
}
