package io.github.jwangsadinata.chocolatebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

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
    private ParseObject object;
    private String currentUser;
    private String userFullName;
    private String partnerName;

    @Bind(R.id.tvHelloUser)
    protected TextView tvHelloUser;

    @Bind(R.id.tvUserFullName)
    protected TextView tvUserFullName;

    @Bind(R.id.tvStatus)
    protected TextView tvStatus;

    @Bind(R.id.tvMatchName)
    protected TextView tvMatchName;

    @Bind(R.id.btnFindMatch)
    protected Button btnFindMatch;

    @Bind(R.id.btnRefresh)
    protected Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setup();

    }

    private void setup() {
        currentUser = ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_USERNAME, currentUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> match, ParseException e) {
                if (e == null) {
                    object = match.get(0);
                    userFullName = object.getString(KEY_FULLNAME);
                    boolean hasMatched = object.getBoolean(KEY_HAS_MATCHED);
                    if (hasMatched) {
                        tvUserFullName.setText(userFullName);
                        tvStatus.setText("You are currently matched with: ");
                        tvMatchName.setText(object.getString(KEY_MATCHED_USER));
                        tvMatchName.setVisibility(View.VISIBLE);

                        btnFindMatch.setText("Find Another Match");
                    }
                    else {
                        tvUserFullName.setText(object.getString(KEY_FULLNAME));
                        tvStatus.setText("You currently do not have a match");
                        tvMatchName.setText(object.getString(KEY_MATCHED_USER));
                        tvMatchName.setVisibility(View.INVISIBLE);

                        btnFindMatch.setText("Find A Match");
                    }
                } else {
                    Log.d("error", "Error: " + e.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.btnFindMatch)
    protected void findMatchButton() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> match, ParseException e) {
                if (e == null) {
                    ParseObject object = match.get(0);
                    boolean hasMatched = object.getBoolean(KEY_HAS_MATCHED);
                    if (hasMatched) {
                        resetMatch();
                        object.put(KEY_HAS_MATCHED, false);
                        object.put(KEY_MATCHED_USER, "");
                        object.saveInBackground();
                    }
                    else {
                        object.put(KEY_HAS_MATCHED, true);
                        findMatch();
                        try {
                            object.put(KEY_MATCHED_USER, partnerName);
                        }
                        catch (NullPointerException e1) {
                            e1.printStackTrace();
                        }
                        object.saveInBackground();
                    }
                } else {
                    Log.d("error", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void findMatch() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_HAS_MATCHED, false);
        try {
            List<ParseObject> objects = query.find();
            Random r = new Random();
            int result = r.nextInt(objects.size());
            objects.get(result).put(KEY_MATCHED_USER, userFullName);
            objects.get(result).put(KEY_HAS_MATCHED, true);
            objects.get(result).saveInBackground();
            partnerName = objects.get(result).getString(KEY_FULLNAME);
            Toast.makeText(MainActivity.this, "Partner Name is: " + partnerName, Toast.LENGTH_SHORT).show();
        }
        catch (com.parse.ParseException e){
            e.printStackTrace();
        }
    }

    private void resetMatch() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_MATCHED_USER, userFullName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Click on the refresh button", Toast.LENGTH_SHORT).show();
                    objects.get(0).put(KEY_MATCHED_USER, "");
                    objects.get(0).put(KEY_HAS_MATCHED, false);
                    objects.get(0).saveInBackground();
                }
            }
        });
    }

    @OnClick(R.id.btnRefresh)
    protected void refreshMessages() {
        setup();
    }


}
