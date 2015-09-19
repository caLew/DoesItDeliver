package did.doesitdeliver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

/**
 * Created by saldinger on 9/19/2015.
 */
public class Login extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.v("Login was", "created");

    }

    @Override
    protected void onStart() {
        super.onStart();

        //This wil retrieve the current ParseUser if there is one
        ParseUser currentUser;
        //currentUser = ParseUser.getCurrentUser();
        currentUser = null;
       // ParseUser.logOut();
        if(currentUser == null) {
            Log.v("User is ", "null");
            ParseLoginBuilder builder = new ParseLoginBuilder(getApplicationContext());
            startActivityForResult(builder.build(), 0);
        }
        else
            Log.v("User is ", "not null");
            startActivity(new Intent(this, ScreenSlideActivity.class));
    }
}
