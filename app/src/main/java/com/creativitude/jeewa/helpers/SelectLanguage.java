package com.creativitude.jeewa.helpers;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.creativitude.jeewa.Dashboard;
import com.creativitude.jeewa.R;

import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {

    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language);

        RadioGroup group = findViewById(R.id.langGroup);
        final RadioButton sinhala = findViewById(R.id.radioSinhala);
        RadioButton english = findViewById(R.id.radioEnglish);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (radioGroup.getCheckedRadioButtonId() == sinhala.getId()) {
                    setLocale("si");

                } else {
                    setLocale("en");

                }

            }
        });


    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Dashboard.class);
        startActivity(refresh);
        finish();
    }
}
