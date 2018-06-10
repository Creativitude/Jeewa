package com.creativitude.jeewa.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.creativitude.jeewa.Dashboard;
import com.creativitude.jeewa.Login;
import com.creativitude.jeewa.R;

public class Drawer extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected int item_id;
    protected Toolbar toolbar;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = findViewById(R.id.toolbar);
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
                        drawerLayout.closeDrawers();
                        startActivity(Dashboard.class);
                        break;

                    case R.id.requests:
                        drawerLayout.closeDrawers();
                        startActivity(AllRequests.class);
                        break;

                    case R.id.my_requests:
                        drawerLayout.closeDrawers();
                        startActivity(MyActivities.class);
                        break;

                    case R.id.notifications:
                        drawerLayout.closeDrawers();
                        startActivity(Notifications.class);
                        break;

                    case R.id.important_info:
                        drawerLayout.closeDrawers();
//                        startActivity(Notifications.class);
                        break;

                    case R.id.profile:
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.settings:
                        drawerLayout.closeDrawers();
                        startActivity(Settings.class);
                        break;


                    case R.id.logout:
                        logout();
                        break;

                    case R.id.about_developer:
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.terms:
                        drawerLayout.closeDrawers();
                        break;

                }


                return true;
            }
        });


//        View header = navigationView.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
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
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }, 200);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void startActivity(final Class toClass) {

        final Intent intent = new Intent(getApplicationContext(),toClass);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    startActivity(intent,options.toBundle());

                }
            },300);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                }
            },300);

        }



    }

}
