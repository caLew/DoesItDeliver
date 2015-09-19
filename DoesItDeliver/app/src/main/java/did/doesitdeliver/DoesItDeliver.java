package did.doesitdeliver;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by saldinger on 9/19/2015.
 */
public class DoesItDeliver extends Application {

    private final String PARSE_APPLICATION_ID = "93fwKdyc3dyMqr5WYpM547js5l3Ocu55AWmEKUk4";
    private final String PARSE_CLIENT_KEY = "Yhmqm2t9W9JiWGxhfRAo8KJeWAwg0Quvmctl0hUD";

    @Override
    public void onCreate() {
        super.onCreate();


        Parse.enableLocalDatastore(this);
        //Initialize the Parse SDK
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }


}
