package com.creativitude.jeewa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.creativitude.jeewa.Login;
import com.creativitude.jeewa.R;

import java.util.Locale;

public class Drawer extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected int item_id;
    protected Toolbar toolbar;
    protected NavigationView navigationView;
    Locale myLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();


    }


    public void initNavigationDrawer() {

        navigationView = findViewById(R.id.navi_view);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item_id = item.getItemId();

                switch (item_id) {
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
//                        startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
                        break;

                    case R.id.requests:
                        Toast.makeText(getApplicationContext(), "Requests", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
//                        startActivity(new Intent(getApplicationContext(),MainMenuActivity.class));
                        break;

                    case R.id.my_requests:
                        Toast.makeText(getApplicationContext(), "my requests", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
//                        startActivity(CreateGroupActivity.class);
                        break;

                    case R.id.notifications:
                        Toast.makeText(getApplicationContext(), "notifications", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
//                        startActivity(InitActivity.class);
                        break;

                    case R.id.important_info:
                        Toast.makeText(getApplicationContext(), "important info", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        //drawerLayout.closeDrawers();
                        break;

                    case R.id.profile:
                        Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        //startActivity(ImageTranslateActivity.class);
                        break;

                    case R.id.settings:
                        Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
//                        startActivity(ProfileActivity.class);
                        break;


                    case R.id.logout:
                        Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
//                        logout();
                        break;

                    case R.id.about_developer:
                        Toast.makeText(getApplicationContext(), "about us", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.terms:
                        Toast.makeText(getApplicationContext(), "terms and conditions", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                }


                return true;
            }
        });



        View header = navigationView.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }

        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void logout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        },200);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void startActivity(final Class toClass){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),toClass));
            }
        },200);

    }

}
