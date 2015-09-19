package did.doesitdeliver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

    }

    @Override
    protected void onStart() {
        super.onStart();

        //This wil retrieve the current ParseUser if there is one
        ParseUser currentUser;
        currentUser = ParseUser.getCurrentUser();

        if(currentUser == null) {
            ParseLoginBuilder builder = new ParseLoginBuilder(getApplicationContext());
            startActivityForResult(builder.build(), 0);
        }
        else
            startActivity(new Intent(this, ScreenSlideActivity.class));

    }
}
