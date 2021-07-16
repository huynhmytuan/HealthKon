package com.pinkteam.android.healthkon.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pinkteam.android.healthkon.CustomClass.SwipeDismissBaseActivity;
import com.pinkteam.android.healthkon.R;

public class AboutUsActivity extends SwipeDismissBaseActivity {
    private Button mBackButton;
    LinearLayout tuanButton, duongButton, nhanButton, longButton, phuongButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_layout);
        tuanButton = findViewById(R.id.tuan_faceboook);
        tuanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(AboutUsActivity.this, "https://www.facebook.com/tuan.huynh.5201254/","tuan.huynh.5201254");
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        duongButton = findViewById(R.id.duong_facebook);
        duongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(AboutUsActivity.this, "https://www.facebook.com/shin11300","shin11300");
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        nhanButton = findViewById(R.id.nhan_faceook);
        nhanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(AboutUsActivity.this, "https://www.facebook.com/luis.phan.798","luis.phan.798");
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        longButton = findViewById(R.id.long_facebook);
        longButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(AboutUsActivity.this, "https://www.facebook.com/profile.php?id=1000083701342360","profile.php?id=1000083701342360");
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        phuongButton = findViewById(R.id.phuong_facebook);
        phuongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(AboutUsActivity.this, "https://www.facebook.com/henry.tr.398345","henry.tr.398345");
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        mBackButton = findViewById(R.id.back_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context, String FACEBOOK_URL, String FACEBOOK_PAGE_ID) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
