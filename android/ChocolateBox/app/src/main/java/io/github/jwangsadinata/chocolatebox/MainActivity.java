package io.github.jwangsadinata.chocolatebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TABLE_USER_MATCH = "Matching";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_EMAIL_ADDRESS = "emailAddress";
    public static final String KEY_HAS_MATCHED = "hasMatched";
    public static final String KEY_MATCHED_USER = "matchedUser";

    @Bind(R.id.tvHelloUser)
    protected TextView tvHelloUser;

    @Bind(R.id.tvUserFullName)
    protected TextView tvUserFullName;

    @Bind(R.id.tvStatus)
    protected TextView tvStatus;

    @Bind(R.id.tvMatchName)
    protected TextView tvMatchName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnFindMatch = (Button) findViewById(R.id.btnFindMatch);

        ButterKnife.bind(this);

        String currentUser = ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_USERNAME, currentUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> match, ParseException e) {
                if (e == null) {
                    final ParseObject object = match.get(0);
                    final boolean hasMatched = object.getBoolean(KEY_HAS_MATCHED);
                    if (hasMatched) {
                        tvUserFullName.setText(object.getString(KEY_FULLNAME));
                        tvStatus.setText("You are currently matched with: ");
                        tvMatchName.setText(object.getString(KEY_MATCHED_USER));
                        tvMatchName.setVisibility(View.VISIBLE);

                        btnFindMatch.setText("Find Another Match");
                        btnFindMatch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                object.put(KEY_HAS_MATCHED, !hasMatched);
                            }
                        });
                    }
                    else {
                        tvUserFullName.setText(object.getString(KEY_FULLNAME));
                        tvStatus.setText("You currently do not have a match");
                        tvMatchName.setText(object.getString(KEY_MATCHED_USER));
                        tvMatchName.setVisibility(View.VISIBLE);

                        btnFindMatch.setText("Find A Match");
                        btnFindMatch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                object.put(KEY_HAS_MATCHED, hasMatched);
                            }
                        });
                    }
                } else {
                    Log.d("error", "Error: " + e.getMessage());
                }
            }
        });

    }
}
