package com.example.nasaproject;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
The main activity of the application
Holds Menu information
Allows user to navigate between sections
Displays initial overview fragment
 */

/*
IVA list:
    Priority A:
        DCU/UIA activities - Chana
        TODO: Lunar Navigation Map
        -Customview activity
        -get map working
        Single Astro Activity (full telemetry display) - Shawn
        -includes Telemetry Row Recycler Adapter, recycler view, etc.
        -implement constructor
        -add arrayList for telemetry object
        -add name to onBindViewholder
        Settings Fragment - Shawn
        -rename + change params # & type
        -refresh rates
        RecordFrag - Brian
        Beacon Monitoring Activity - Shawn

    Priority B:
        Main activity - Shawn w/EVA help
        -add rover & EVA mission frags
        -create settings menu for manual entry of IP/API - Chana?
        EVA activity
        -add EVA details to Details activity
        -rename param arguments & change # & type
        Navigation/Menu implementation - Chana

    Priority C:
        Live Stream
        -create stream to embed EVA camera
        Astro Overview Frag
        -fix timer to restart on open/close?
        -add setting "Input expected mission time"
        -get data to refresh faster
        Single Astro
        -change Fahrenheit to Celsius
 */
// TODO: Incorporate Rover fragment
// TODO: Add EVA mission overview fragment

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String pass;
    private DrawerLayout drawer;
    private Button moreButton;

    private final String URL = "http://192.168.1.10:8002/test";

    private TextView Oxygen;

    /*public static final String PREFS_NAME = "ARGOS";
    public static final String ARGOS_ADDR = "ARGOS_ADDR";
    public static final String XEMU_0_ADDR = "XEMU_0_ADDR";
    public static final String XEMU_1_ADDR = "XEMU_1_ADDR";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AstroOverviewFragment()).commit();
        /*moreButton = findViewById(R.id.btnMore);
        //pressureButton.setText(Help);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent (MainActivity.this, singleAstro.class);

                startActivity(i);
            }

        });*/

        /*Intent i = new Intent(this, Sandbox.class);
        startActivity(i); */
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i("MainActivity", "Menu choice made:");
        switch (menuItem.getItemId()) {
            case R.id.main:
                try {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AstroOverviewFragment()).commit();
                    Log.i("MainActivity", "Main fragment");
                    AstroOverviewFragment astro = new AstroOverviewFragment();
                    //input whatever ip/api the telemetry server runs on
                    //TODO: create settings menu that allows for manual entry of IP/API
                    astro.useAPI("http://192.168.1.10:8002/telemetry");

                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast toast = Toast.makeText(getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            //case R.id.details:
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new details()).commit();
            // Log.i("MainActivity", "Details fragment");
            //break;
            case R.id.field:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FieldFragment()).commit();
                break;
            case R.id.rover:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RoverFragment()).commit();
                break;
            case R.id.single:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SingleAstroFragment()).commit();
                break;
            //connect to ubalt astrobees twitch stream
            //uses default browser if one is set, if not asks for browser
            case R.id.Stream:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LiveStream()).commit();
                String url = getString(R.string.twitch_address);
                WebView webView;
                webView = (WebView)findViewById(R.id.webview1);
                webView.setWebChromeClient(new WebChromeClient());
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(false);
                webSettings.setUseWideViewPort(true);
                webSettings.setLoadWithOverviewMode(true);
                webView.loadUrl(url);
                break;
            //case R.id.main:
            //  getSupportFragmentManager().beginTransaction().replace( new MainActivity()).commit();
            //   break;

            case R.id.EVA:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EVAFragment()).commit();
                break;

            case R.id.MENU_REMOTE_CONTROL:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecordFragment()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RemoteControlFragment()).commit();
                break;

            case R.id.lunarMap:
                Intent startWaypointActivity = new Intent(this, WaypointManagerActivity.class);
                startActivity(startWaypointActivity);
                break;

            /*case R.id.lunarMap:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Sandbox()).commit();
                break;*/

            case R.id.dcuFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DCUFragment()).commit();
                break;

            case R.id.uiaFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UIAFragment()).commit();
                break;

            case R.id.Beacon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BeaconsFragment()).commit();
                break;

            case R.id.Settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();

            /*case R.id.lunarMap:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Sandbox()).commit();*/

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
