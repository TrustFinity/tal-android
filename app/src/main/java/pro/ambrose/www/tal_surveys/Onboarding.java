package pro.ambrose.www.tal_surveys;

import android.content.Intent;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class Onboarding extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard card_one = new AhoyOnboarderCard("Welcome", "Looks like you are new, welcome to TAL surveys" +
                " where your opinion matters.", R.mipmap.ic_launcher);
        card_one.setBackgroundColor(R.color.black_transparent);
        card_one.setTitleColor(R.color.white);
        card_one.setDescriptionColor(R.color.grey_200);
        card_one.setTitleTextSize(dpToPixels(10, this));
        card_one.setDescriptionTextSize(dpToPixels(4, this));
        card_one.setIconLayoutParams(500, 500, 5, 5, 5, 5);

        AhoyOnboarderCard card_two = new AhoyOnboarderCard("Welcome", "Unlock the value of your opinions, get rewarded.", R.mipmap.ic_launcher);
        card_two.setBackgroundColor(R.color.black_transparent);
        card_two.setTitleColor(R.color.white);
        card_two.setDescriptionColor(R.color.grey_200);
        card_two.setTitleTextSize(dpToPixels(10, this));
        card_two.setDescriptionTextSize(dpToPixels(4, this));
        card_two.setIconLayoutParams(500, 500, 5, 5, 5, 5);


        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(card_one);
        pages.add(card_two);
        setGradientBackground();
        setFinishButtonTitle("Get started");
        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
