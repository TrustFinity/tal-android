package pro.ambrose.www.tal_surveys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginTag";
    LoginButton loginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (Profile.getCurrentProfile() != null){
            updateUI();
        }
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("public_profile", "user_friends", "email");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult.toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        if (Profile.getCurrentProfile() != null){
            Log.d(TAG, Profile.getCurrentProfile().getProfilePictureUri(720, 500).toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d(TAG, accessToken.toString());
        // create or update user.
        // start the home activity
        updateUI();
    }

    private void updateUI() {
        Profile profile = Profile.getCurrentProfile();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("first_name", profile.getFirstName());
        editor.putString("picture", profile.getProfilePictureUri(600, 400).toString());
        editor.putString("middle_name", profile.getMiddleName());
        editor.putString("last_name", profile.getLastName());
        editor.commit();
        startActivity(new Intent(LoginActivity.this, Home.class));
        finish();
    }
}
