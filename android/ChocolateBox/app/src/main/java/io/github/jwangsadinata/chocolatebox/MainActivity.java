package io.github.jwangsadinata.chocolatebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TABLE_USER_MATCH = "Matching";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_HAS_MATCHED = "hasMatched";
    public static final String KEY_MATCHED_USER = "matchedUser";

    @Bind(R.id.tvHelloUser)
    protected TextView tvHelloUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        final String currentUser = ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_USERNAME, currentUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> match, ParseException e) {
                if (e == null) {
                    if (match.get(0).getBoolean(KEY_HAS_MATCHED)) {
                        tvHelloUser.setText("Hello " + currentUser + ", you are currently matched with " + match.get(0).getString(KEY_MATCHED_USER));
                    }
                    else {
                        tvHelloUser.setText("Hello " + currentUser + ", you don't have a match yet");
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }
}
