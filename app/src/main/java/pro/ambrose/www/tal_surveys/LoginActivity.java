package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginTag";
    public String create_user_url = "http://tal-surveys.herokuapp.com/api/v1/new-respondent";
    public Profile profile;
    public String access_token;
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
            handlePostUserData("user already logged in");
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
                saveUserDataOnline(loginResult.getAccessToken());
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void saveUserDataOnline(AccessToken accessToken) {
        profile = Profile.getCurrentProfile();
        access_token = accessToken.toString();
        new PostUserData(create_user_url).execute();
    }

    public void handlePostUserData(String response) {
        startActivity(new Intent(LoginActivity.this, Home.class));
        finish();
        Log.d(TAG, response);
    }

    private class PostUserData extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();
        private String URL;

        private PostUserData(String URL) {
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody body = new FormBody.Builder()
                    .add("facebook_id", access_token)
                    .add("firstname", profile.getFirstName())
                    .add("middle_name", profile.getMiddleName())
                    .add("lastname", profile.getLastName())
                    .add("image_url", profile.getProfilePictureUri(500, 500).toString())
                    .build();
            Request request = new Request.Builder()
                    .url(this.URL)
                    .post(body)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handlePostUserData(s);
        }
    }
}
