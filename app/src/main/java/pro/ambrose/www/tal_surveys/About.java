package pro.ambrose.www.tal_surveys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.6");
        Element adsElement = new Element();
        adsElement.setTitle("Developed By TrustFinity");
        View aboutPage = new AboutPage(getApplicationContext())
                .isRTL(false)
                .setDescription("TraceAfricaConnect is an online freight-hub created for the sole purpose of connecting shippers and carriers to improve the logistics experience in Africa. TraceAfricaConnect is a real-time platform that consolidates online freight information entered by large and small TL and LTL freight carriers across Africa and other worldwide logistics hubs.")
                .setImage(R.mipmap.ic_launcher)
                .addItem(versionElement)
                .addItem(adsElement)
                .addEmail("admin@traceafricalogistics.com", "Send us an email")
                .addWebsite("http://traceafricalogistics.com/", "Visit our website")
                .addFacebook("TraceAfricaLogistics", "Like us on facebook")
                .addTwitter("TraceAfricaLog", "Follow us on twitter")
                .addPlayStore("pro.ambrose.www.tal_surveys", "Rate us on Google Play")
                .create();
        setContentView(aboutPage);
        setTitle("About App");
    }
}