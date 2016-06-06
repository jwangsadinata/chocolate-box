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

    // Variable declarations
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
    private String emailDestination;

    // Instantiating Canvas objects
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

    @Bind(R.id.btnSetATime)
    protected Button btnSetATime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the Butterknife library
        ButterKnife.bind(this);

        setup();

    }

    // Setting up how the page looks
    private void setup() {
        currentUser = ParseUser.getCurrentUser().getUsername();

        // Use the ParseQuery to get the objects in our database
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_USERNAME, currentUser);

        // Query in Background, so that we do not have to use the main Thread
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> match, ParseException e) {
                if (e == null) {
                    object = match.get(0);
                    userFullName = object.getString(KEY_FULLNAME);
                    boolean hasMatched = object.getBoolean(KEY_HAS_MATCHED);

                    // If a person has a match, we want to show the necessary information
                    if (hasMatched) {
                        tvUserFullName.setText(userFullName);
                        tvStatus.setText("You are currently matched with: ");
                        tvMatchName.setText(object.getString(KEY_MATCHED_USER));
                        tvMatchName.setVisibility(View.VISIBLE);

                        // Using .split to obtain firstname from their fullname.
                        String[] splitName = object.getString(KEY_MATCHED_USER).split(" ");
                        String firstName = splitName[0];

                        btnFindMatch.setText("Done with " + firstName);
                        btnSetATime.setVisibility(View.VISIBLE);
                    }

                    // Otherwise, we want to show that he/she currently do not have a match
                    else {
                        tvUserFullName.setText(object.getString(KEY_FULLNAME));
                        tvStatus.setText("You currently do not have a match");
                        tvMatchName.setText(object.getString(KEY_MATCHED_USER));
                        tvMatchName.setVisibility(View.INVISIBLE);

                        btnFindMatch.setText("Find A Match");
                        btnSetATime.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.d("error", "Error: " + e.getMessage());
                }
            }
        });
    }

    // Find A Match button -> do a query in background to find a possible match.
    @OnClick(R.id.btnFindMatch)
    protected void findMatchButton() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_USERNAME, currentUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> match, ParseException e) {
                if (e == null) {
                    ParseObject object = match.get(0);
                    boolean hasMatched = object.getBoolean(KEY_HAS_MATCHED);

                    // If the person already has a match, clicking the button reset's it.
                    if (hasMatched) {
                        resetMatch();
                        object.put(KEY_HAS_MATCHED, false);
                        object.put(KEY_MATCHED_USER, "");
                        object.saveInBackground();
                    }

                    // If the person does not have a match, clicking the button finds a match.
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

    // A method to find a match based on the remaining pool of user who does not have a match.
    private void findMatch() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USER_MATCH);
        query.whereEqualTo(KEY_HAS_MATCHED, false);
        query.whereNotEqualTo(KEY_USERNAME, currentUser);
        try {
            // We use query.find() so that we can store the results on the main thread
            List<ParseObject> objects = query.find();

            // The idea is to use a random index in finding the user's match
            Random r = new Random();
            int result = r.nextInt(objects.size());

            // We do the necessary updates to the partner, and get the necessary information for the user
            objects.get(result).put(KEY_MATCHED_USER, userFullName);
            objects.get(result).put(KEY_HAS_MATCHED, true);
            objects.get(result).saveInBackground();
            partnerName = objects.get(result).getString(KEY_FULLNAME);
            emailDestination = objects.get(result).getString(KEY_EMAIL_ADDRESS);
            Toast.makeText(MainActivity.this, "Partner Name is: " + partnerName + ", click on the refresh button", Toast.LENGTH_SHORT).show();
        }
        catch (com.parse.ParseException e){
            e.printStackTrace();
        }
    }

    // method to resetMatch, basically makes sure that the partner also has the appropriate information.
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

    // Clicking on refresh button calls setup(), which recreates the whole page all over again.
    @OnClick(R.id.btnRefresh)
    protected void refreshMessages() {
        setup();
    }

    // Set a time button allows user to send an email to set up a time, with most fields autocompleted by the app.
    @OnClick(R.id.btnSetATime)
    protected void setATime() { sendEmail();}

    // A method to send email to the recipient using external application.
    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{emailDestination});
        i.putExtra(Intent.EXTRA_SUBJECT,
                "ChocolateBox Match");
        i.putExtra(Intent.EXTRA_TEXT,
                "Hey there, let's set up a time and date for our ChocolateBox date. Looking forward to your reply.");
        try {
            startActivity(Intent.createChooser(i, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


}
