package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NoInternet extends AppCompatActivity {


    private static final String TAG = "In Internet";
    private Button retry_internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        getSupportActionBar().hide();


        retry_internet = findViewById(R.id.retry_internet);
        retry_internet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent activity = getIntent();
                try {
                    startActivity(new Intent(getApplicationContext(), Class.forName(activity.getStringExtra("activity"))));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
