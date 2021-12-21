package com.example.nasaproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

// TODO: 5/19/21 Threading in Android (including services and threads that are not attached to the activity): https://www.toptal.com/android/android-threading-all-you-need-to-know

/**
 * A simple android fragment that allows for monitoring of multiple telemetry streams via api calls on different ports
 * FIXME: Fix timer restarting on app close/restart
 * TODO: add setting to input expected mission time by user
 * TODO: find a way to get the data to refresh faster runnable seems to have 20,000 millisecond delay limit as fastest
 */

public class AstroOverviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String xemu0Address; // = AddressManager.getXemu0Address(getContext().getApplicationContext()); //"http://192.168.1.10:8002/telemetry";
    private String xemu1Address; // = AddressManager.getXemu1Address(getContext().getApplicationContext()); //"http://192.168.1.10:8001/telemetry";

    private static TextView Oxygen;
    private static TextView Battery;
    private static TextView OxygenRate;
    private static TextView Oxygen2;
    private static TextView Battery2;
    private static TextView OxygenRate2;
    public static TextView Time;
    public static TextView Time2;
    public long millis;
    public long millis2;

    private Button detailsButton;

    private Button startEVA;
    private Button pauseEVA;
    private Button resumeEVA;
    private Button stopEVA;

    private Button checkARGOS;
    private Button checkXEmu0;
    private Button checkXEmu1;

    private Button startUIA;
    private Button pauseUIA;
    private Button resumeUIA;
    private Button stopUIA;

    public long timeRemaining;

    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer2;

    protected Handler handler = new Handler();

    public void useAPI(String URL) {
        System.gc();
        new GetFromAPI().execute(URL);
    }

    public AstroOverviewFragment() {
        // Required empty public constructor
    }

    public static AstroOverviewFragment newInstance(String param1, String param2) {
        AstroOverviewFragment fragment = new AstroOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        System.gc();
        countDownTimer.cancel();
        countDownTimer2.cancel();
        super.onPause();
    }

    @Override
    public void onResume() {

        if (millis > 0) {
            countDownTimer = new CountDownTimer(millis, 1000) {
                public void onTick(long millisUntilFinished) {
                    Time.setText(String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    ));
                    millis = millisUntilFinished;
                    // Log.i("TIMER",Long.toString(millisUntilFinished));
                }

                public void onFinish() {
                    Time.setText("done!");
                }

            }.start();
        }

        /*else {
            countDownTimer = new CountDownTimer(millis, 1000){
                public void onTick(long millisUntilFinished) {
                    Time.setText(String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)% 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) %60
                    ));
                    millis = millisUntilFinished;
                }


                public void onFinish() {
                    Time.setText("done!");
                }

            }.start();
        } */

        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_astro_overview, container, false);

        xemu0Address = AddressManager.getXemu0Address(getContext().getApplicationContext());
        xemu1Address = AddressManager.getXemu0Address(getContext().getApplicationContext());

        //gives us mission duration in from hours to milliseconds
        millis = TimeUnit.HOURS.toMillis(8);
        millis2 = TimeUnit.HOURS.toMillis(5);

        new GetFromAPI().execute(xemu0Address+"/api/simulation/state");
        new GetFromAPI2().execute(xemu1Address+"/api/simulation/state");

        Oxygen = rootView.findViewById(R.id.Oxygen);
        Battery = rootView.findViewById(R.id.battery);
        OxygenRate = rootView.findViewById(R.id.Pressure);
        Time = rootView.findViewById(R.id.beat);
        detailsButton = rootView.findViewById(R.id.btnMore);
        Oxygen2 = rootView.findViewById(R.id.Oxygen2);
        Battery2 = rootView.findViewById(R.id.battery2);
        OxygenRate2 = rootView.findViewById(R.id.Pressure2);
        Time2 = rootView.findViewById(R.id.beat2);

        handler.postDelayed(runnable, 10000);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment myFrag = new SingleAstroFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, myFrag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        countDownTimer2 = new CountDownTimer(millis2, 1000) {
            public void onTick(long millisUntilFinished) {
                Time2.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                ));
                millis2 = millisUntilFinished;
                // Log.i("TIMER",Long.toString(millisUntilFinished));
            }

            public void onFinish() {
                Time2.setText("done!");
            }

        }.start();

        // This method sets up the control panel
        evaControls(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }
    // A method for refreshing the telemetry stream data

    /*
     * GV: I added the method sandbox(rootView) to test the different UI elements. Each element will
     * handle one action and output anything that is returned using log.i.
     *
     * I also added volley 1.1.1 to build.gradle (Module:NasaProject.app)
     *
     * Start EVA: An example to how to get the EVA started, otherwise the system
     * won't be able to pull the telemetry stream.
     *
     * Get Telemetry: An example of how to pull telemetry once the stream was started.
     *
     * Toggle: To turn some element of the DCU on or off, depending on the state.
     */

    /*
     * GV: This will have to change if the IP/port change
     * We cannot have the loopback, because when this runs on a device (even virtual), the loopback
     * address will try to open a connection on the device itself and not the host.
     */
    //private final String host = xemu0Address; // "192.168.0.104:3000";

    // Examples: https://www.geeksforgeeks.org/volley-library-in-android/

    private void evaControls(View rootView) {
        final String logTag = "EVA Controls";

        // To start the EVA
        final String xemu0 = xemu0Address;

        startEVA = rootView.findViewById(R.id.sandbox_start_eva);
        startEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Start EVA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/start";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "EVA started", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        pauseEVA = rootView.findViewById(R.id.sandbox_pause_eva);
        pauseEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Pause EVA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/pause";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "EVA paused", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        resumeEVA = rootView.findViewById(R.id.sandbox_resume_eva);
        resumeEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Resume EVA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/unpause";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "EVA resumed", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        stopEVA = rootView.findViewById(R.id.sandbox_stop_eva);
        stopEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Stop EVA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/stop";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "EVA stopped", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        checkARGOS = rootView.findViewById(R.id.sandbox_check_argos);
        checkARGOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkARGOS.setEnabled(false);
                    Log.i(logTag, "Checking the availability of ARGOS");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest

                    String url = AddressManager.getArgosAddress(getContext().getApplicationContext());
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "ARGOS is ready", Toast.LENGTH_LONG).show();
                                    checkARGOS.setEnabled(true);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "ARGOS is not available", Toast.LENGTH_LONG).show();
                            checkARGOS.setEnabled(true);
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT).show();
                    checkARGOS.setEnabled(true);
                }
            }
        });

        checkXEmu0 = rootView.findViewById(R.id.sandbox_check_xemu0);
        checkXEmu0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkXEmu0.setEnabled(false);
                try {
                    Log.i(logTag, "Checking the availability of xEMU-0");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = AddressManager.getXemu0Address(getContext().getApplicationContext());
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            url+"/api/simulation/state",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "xEMU-0 is ready", Toast.LENGTH_LONG).show();
                                    checkXEmu0.setEnabled(true);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "xEMU-0 is not available", Toast.LENGTH_LONG).show();
                            checkXEmu0.setEnabled(true);
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT).show();
                    checkXEmu0.setEnabled(true);
                }
            }
        });

        checkXEmu1 = rootView.findViewById(R.id.sandbox_check_xemu1);
        checkXEmu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkXEmu1.setEnabled(false);
                    Log.i(logTag, "Checking the availability of xEMU-1");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = AddressManager.getXemu1Address(getContext().getApplicationContext());
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            url+"/api/simulation/state",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "xEMU-1 is ready", Toast.LENGTH_LONG).show();
                                    checkXEmu1.setEnabled(true);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "xEMU-1 is not available", Toast.LENGTH_LONG).show();
                            checkXEmu1.setEnabled(true);
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT).show();
                    checkXEmu1.setEnabled(true);
                }
            }
        });

        startUIA = rootView.findViewById(R.id.sandbox_start_uia);
        startUIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Start UIA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/uiastart";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "UIA started", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        pauseUIA = rootView.findViewById(R.id.sandbox_pause_uia);
        pauseUIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Pause UIA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/uiapause";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "UIA paused", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        resumeUIA = rootView.findViewById(R.id.sandbox_resume_uia);
        resumeUIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Resume UIA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/uiaunpause";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "UIA resumed", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        stopUIA = rootView.findViewById(R.id.sandbox_stop_uia);
        stopUIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(logTag, "Clicked on the Stop UIA Button");
                    // Sample call: http://localhost:3000/api/simulation/start (POST)
                    // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                    String url = xemu0 + "/api/simulation/uiastop";
                    Log.i(logTag, "Call: " + url);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(logTag, "Successful request...");
                                    Log.i(logTag, "Response: " + response);
                                    Toast.makeText(getContext().getApplicationContext(), "UIA stopped", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(logTag, "An error occurred...");
                            Log.i(logTag, error.toString());
                            Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                        }
                    }
                    );
                    Volley.newRequestQueue(getContext()).add(stringRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast.makeText(getContext().getApplicationContext(), "Connectivity problems", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*
        // To get the telemetry information
        Button getTelemetryInfo = rootView.findViewById(R.id.sandbox_get_telemetry);
        getTelemetryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    Log.i(logTag, "Clicked on the Get Telemetry Button");
                    // Sample call: http://localhost:3000/api/simulation/state (GET)
                    // Returns telemetry in JSON format
                    String url = host + "/api/simulation/state";
                    Log.i(logTag, "Call: " + url);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(logTag, "Successful connection");
                                    Log.i(logTag, response.toString());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(logTag, "Connection error...");
                                    Log.i(logTag, "Error: " + error.toString());
                                }
                            }
                    );

                    Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // To toggle the fan switch on the DCU on and off
        ToggleButton toggleButton = rootView.findViewById(R.id.sandbox_toggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format

                try {
                    String url = host + "/api/simulation/newcontrols?fan_switch=";
                    if (isChecked) { // Turn on
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "true";
                    } else { // Turn off
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "false";
                    }
                    //TODO
                    Log.i(logTag, "Call: " + url);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.PATCH,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(logTag, "Successful connection...");
                                    Log.i(logTag, "Response: " + response.toString());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(logTag, "Something did not work...");
                                    Log.i(logTag, "Error: " + error.toString());
                                }
                            }
                    );

                    Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });*/
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                new GetFromAPI().execute(xemu0Address+"/api/simulation/state");

                handler.postDelayed(this, 5000);
            } catch (Exception e) {
                Log.e("API Call", e.toString());
            }
        }
    };

    //The class we use to get the telemetry data from our local telemetry server
    private class GetFromAPI extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private String oxygen;
        private int battery;
        private String oxygenrate;

        public GetFromAPI() {
            counter = 0;
        }

        private int counter;

        @Override
        protected String doInBackground(String... strings) {
            String request = strings[0];

            Log.i("> Api Call", "Request: " + request);

            try {
                Log.i("API Call", "Initiating Call: " + request);

                java.net.URL url = new URL(request);
                url.openConnection().setConnectTimeout(6000);

                InputStream in = url.openStream();
                Scanner streamInput = new Scanner(in, StandardCharsets.UTF_8.name());

                jsonResponse = streamInput.nextLine();

                streamInput.close();
                in.close();

                // Response: {"_id":"60a48bfa93e18d242aefcad8","time":4.088,"timer":"00:00:04","started_at":"2021-05-19T03:54:34.502Z","heart_bpm":88,"p_sub":"3.96","p_suit":"3.95","t_sub":"31.7","v_fan":"39995","p_o2":"776.15","rate_o2":"0.8","batteryPercent":99.97161111111112,"battery_out":99,"cap_battery":30,"t_battery":"03:59:56","p_h2o_g":"15.74","p_h2o_l":"15.06","p_sop":"876","rate_sop":"0.6","t_oxygenPrimary":"99.96214814814815","t_oxygenSec":"100","ox_primary":"99","ox_secondary":"100","t_oxygen":"05:59:55","cap_water":99.97935353535354,"t_water":"05:29:56","__v":0}

                JSONObject jsonObject = new JSONObject(jsonResponse);

                Log.i("Telemetry Parser", "Request " + counter++ + ": " + jsonResponse);

                Log.i("Telemetry parser", jsonObject.getString("rate_o2"));


                //the json data does not come as a json array so we use jsonObjects to get each value in each object
                //not the jsonobject out of an array which was the original approach
//
                oxygen = jsonObject.getString("ox_primary");
                Oxygen.setText(oxygen);
                battery = jsonObject.getInt("battery_out");
                Battery.setText(Integer.toString(battery));
                oxygenrate = jsonObject.getString("rate_o2");
                OxygenRate.setText(oxygenrate);
//
//                JSONObject jsonOxygen = jsonObject.getJSONObject("0");
//                oxygen = jsonOxygen.getInt("value");
//                JSONObject jsonBattery = jsonObject.getJSONObject("4");
//                JSONObject jsonOxygenRate = jsonObject.getJSONObject("3");
            } catch (Exception e) {
                Log.e("API Call", e.toString());
            }

//            return "API Call Complete - doinbackground1";
//            return request;
            return jsonResponse;
        }

        protected void onPostExecute(String result) {

            Log.i("API Call", "Post Ex Result: " + result);
        try {

            JSONObject jsonObject = new JSONObject(result);
            // Adding updated values to display
            oxygen = jsonObject.getString("ox_primary");
            battery = jsonObject.getInt("battery_out");
            oxygenrate = jsonObject.getString("rate_o2");
            Oxygen.setText("Oxygen Pressure: " + (oxygen) + " psia");
            Battery.setText("Battery Usage: " + (battery) + " amp-hr");
            OxygenRate.setText("Oxygen Flow Rate: " + (oxygenrate) + " psi/min");
//
//
//            JSONObject jsonBattery = jsonObject.getJSONObject("4");
//            JSONObject jsonOxygenRate = jsonObject.getJSONObject("3");
//            Oxygen.setText("Oxygen Pressure: " + (oxygen) + " psia");
//            Battery.setText("Battery Usage: " + (battery) + " amp-hr");
//            OxygenRate.setText("Oxygen Flow Rate: " + (oxygenrate) + " psi/min");
        } catch (Exception e){
            Log.e("API Call", e.toString());
        }
        }
    }

    //we have to use multithreading for additional astronauts as we can't update different values from a single call
    //so we use two async tasks to get the job done
    private class GetFromAPI2 extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private String oxygen;
        private int battery;
        private String oxygenrate;

        @Override
        protected String doInBackground(String... strings) {
            String request = strings[0];

            Log.i("Api Call2", "Request: " + request);

            try {
                Log.i("API Call2", "Initiating Call");

                java.net.URL url = new URL(request);
                url.openConnection().setConnectTimeout(6000);

                InputStream in = url.openStream();
                Scanner streamInput = new Scanner(in, StandardCharsets.UTF_8.name());

                jsonResponse = streamInput.nextLine();

                streamInput.close();
                in.close();

                //JSONArray arrayData = new JSONArray(jsonResponse);

                JSONObject jsonObject = new JSONObject(jsonResponse);

                oxygen = jsonObject.getString("ox_primary");
                Oxygen.setText(oxygen);
                battery = jsonObject.getInt("battery_out");
                Battery.setText(Integer.toString(battery));
                oxygenrate = jsonObject.getString("rate_o2");
                OxygenRate.setText(oxygenrate);

            } catch (Exception e) {
                Log.e("API Call2", e.toString());
            }

            return "API Call Complete - doinbackground2";
        }

        protected void onPostExecute(String result) {
            Log.i("API Call2", "Result: " + result);

           // Oxygen2.setText("Oxygen Pressure: " + (oxygen) + " psia");
           // Battery2.setText("Battery Usage: " + (battery) + " amp-hr");
           // OxygenRate2.setText("Oxygen Flow Rate: " + (oxygenrate) + " psi/min");

            try {
                JSONObject jsonObject = new JSONObject(result);
                // Adding updated values to display
                oxygen = jsonObject.getString("ox_primary");
                battery = jsonObject.getInt("battery_out");
                oxygenrate = jsonObject.getString("rate_o2");
                Oxygen.setText("Oxygen Pressure: " + (oxygen) + " psia");
                Battery.setText("Battery Usage: " + (battery) + " amp-hr");
                OxygenRate.setText("Oxygen Flow Rate: " + (oxygenrate) + " psi/min");

            }catch (Exception e){
                Log.e("API Call", e.toString());
            }
        }
    }
}
