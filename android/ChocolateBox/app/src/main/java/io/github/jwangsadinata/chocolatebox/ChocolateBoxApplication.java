package io.github.jwangsadinata.chocolatebox;

import android.app.Application;

import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by Jason on 6/4/16.
 */

public class ChocolateBoxApplication extends Application {

    public static final String APP_ID =
            "259williams";
    public static final String SERVER_URL = "https://chocolate-box-wesleyan.herokuapp.com/parse/";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APP_ID)
                .clientKey(null)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server(SERVER_URL).build());
    }

}
