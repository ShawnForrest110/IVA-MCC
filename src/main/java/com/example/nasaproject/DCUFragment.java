package com.example.nasaproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DCUFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView dcuHeader;
    private TextView suitPower;
    private TextView fanSwitch;
    private TextView o2Switch;
    private TextView rca;
    private TextView aux;
    private TextView pump;

    private TextView dcuheader2;
    private TextView suitPower2;
    private TextView fanSwitch2;
    private TextView o2Switch2;
    private TextView rca2;
    private TextView aux2;
    private TextView pump2;

    private Switch switchOne;
    private Switch switchTwo;
    private Switch switchThree;
    private Switch switchFour;
    private Switch switchFive;
    private Switch switchSix;

    private Switch switchSuitPwr2;
    private Switch switchFanSwt2;
    private Switch switchO2Swt2;
    private Switch switchRca2;
    private Switch switchAux2;
    private Switch switchPump2;

    private String xemu0Address;// = "http://192.168.1.191:3000/api/simulation/";
    private String xemu1Address;// = "http://192.168.1.191:3001/api/simulation/";

    final String logTag = "DCU State";
    private View myView;
    protected Handler handler = new Handler();

    public void useAPI(String URL) {
        System.gc();
        new GetFromAPI().execute(URL);
    }

    public DCUFragment() {
        //required empty public constructor
    }

    public static DCUFragment newInstance(String param1, String param2) {
        DCUFragment fragment = new DCUFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return myView = inflater.inflate(R.layout.fragment_dcu, container, false);

        //new GetFromAPI().execute(host1);
        //new GetFromAPI2().execute(host2);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        xemu0Address = AddressManager.getXemu0Address(getContext().getApplicationContext());
        xemu1Address = AddressManager.getXemu1Address(getContext().getApplicationContext());

        dcuHeader = myView.findViewById(R.id.dcu_header);
        suitPower = myView.findViewById(R.id.suit_pwr);
        fanSwitch = myView.findViewById(R.id.fan_swt);
        o2Switch = myView.findViewById(R.id.o2_swt);
        rca = myView.findViewById(R.id.rca);
        aux = myView.findViewById(R.id.aux);
        pump = myView.findViewById(R.id.pump);

        dcuheader2 = myView.findViewById(R.id.dcu_header2);
        suitPower2 = myView.findViewById(R.id.suit_pwr_dcu2);
        fanSwitch2 = myView.findViewById(R.id.fan_swt_dcu2);
        o2Switch2 = myView.findViewById(R.id.o2_swt_dcu2);
        rca2 = myView.findViewById(R.id.rca_dcu2);
        aux2 = myView.findViewById(R.id.aux_dcu2);
        pump2 = myView.findViewById(R.id.pump_dcu2);

        switchOne = myView.findViewById(R.id.switch1);
        switchTwo = myView.findViewById(R.id.switch2);
        switchThree = myView.findViewById(R.id.switch3);
        switchFour = myView.findViewById(R.id.switch4);
        switchFive = myView.findViewById(R.id.switch5);
        switchSix = myView.findViewById(R.id.switch6);

        switchSuitPwr2 = myView.findViewById(R.id.switch1_dcu2);
        switchFanSwt2 = myView.findViewById(R.id.switch2_dcu2);
        switchO2Swt2 = myView.findViewById(R.id.switch3_dcu2);
        switchRca2 = myView.findViewById(R.id.switch4_dcu2);
        switchAux2 = myView.findViewById(R.id.switch5_dcu2);
        switchPump2 = myView.findViewById(R.id.switch6_dcu2);

        new GetFromAPI().execute(xemu0Address);
        //Log.i("DCU", "Is it working?" );
        //new GetFromAPI2().execute(xemu1Address);
        //new GetFromAPI().execute(host1a);

        handler.postDelayed(runnable, 10000);

        //switch for suit power -CG
        switchOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(logTag, "Switch clicked...");
                try {
                    String url = xemu0Address + "/api/simulation/newcontrols?battery_switch=";
                    if (switchOne.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
                    Log.i(logTag, "onClick call: " + url);

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
                                    error.printStackTrace();
                                }
                            }
                    );

                    Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
                } catch (Exception e) {
                    Log.e("Error Connecting", e.toString());
                    e.printStackTrace();
                    Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //switch for fan switch -CG
        switchTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String url = xemu0Address + "/api/simulation/newcontrols?fan_switch=";
                    if (switchTwo.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for O2 -CG
        switchThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu0Address + "/api/simulation/newcontrols?O2_switch=";
                    if (switchThree.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for RCA -CG
        switchFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu0Address + "/api/simulation/newcontrols?switch3=";
                    if (switchFour.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for AUX -CG
        switchFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu0Address + "/api/simulation/newcontrols?switch4=";
                    if (switchFive.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for pump -CG
        switchSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu0Address + "/api/simulation/newcontrols?switch5=";
                    if (switchSix.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for suit power -CG
        switchSuitPwr2.setEnabled(false);
        switchSuitPwr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3001/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu1Address + "/api/simulation/newcontrols?battery_switch=";
                    if (switchSuitPwr2.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for fan switch -CG
        switchFanSwt2.setEnabled(false);
        switchFanSwt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3001/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu1Address + "/api/simulation/newcontrols?fan_switch=";
                    if (switchFanSwt2.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for O2 -CG
        switchO2Swt2.setEnabled(false);
        switchO2Swt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3001/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu1Address + "/api/simulation/newcontrols?O2_switch=";
                    if (switchO2Swt2.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for RCA -CG
        switchRca2.setEnabled(false);
        switchRca2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu1Address + "/api/simulation/newcontrols?switch3=";
                    if (switchRca2.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for AUX -CG
        switchAux2.setEnabled(false);
        switchAux2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3001/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu1Address + "/api/simulation/newcontrols?switch4=";
                    if (switchAux2.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
        });

        //switch for pump -CG
        switchPump2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {
                    String url = xemu1Address + "/api/simulation/newcontrols?switch5=";
                    if (switchPump2.isChecked()) {
                        Log.i(logTag, "Attempting to turn something on...");
                        url += "true";
                    } else {
                        Log.i(logTag, "Attempting to turn something off...");
                        url += "false";
                    }

                    //test call to octavia
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
                    Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                new GetFromAPI().execute(xemu0Address);
                Log.i("Thread", "Thread executed...");
                handler.postDelayed(this, 2000);
            } catch (Exception e) {
                Log.e("API Call", e.toString());
            }
        }
    };

    //The class we use to get the telemetry data from our local telemetry server
    private class GetFromAPI extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private boolean suitPwr;
        private boolean fanSwth;
        private boolean o2Swth;
        private boolean rcaSwth;
        private boolean auxSwth;
        private boolean pumpSwth;

        @Override
        protected String doInBackground(String... strings) {
            String request = strings[0] + "/api/simulation/newcontrols";

            Log.i("DCU API Call", "Request: " + request);
            final Context applicationContext = getContext().getApplicationContext();

            // Updated method

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PATCH,
                    request,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(logTag, "Successful connection...");
                            Log.i(logTag, "Response: " + response.toString());

                            try {
                                // "battery_switch":false
                                switchOne.setChecked(response.getBoolean("battery_switch"));
                            } catch (Exception e) {
                                switchOne.setChecked(false);
                                Log.i(logTag, "Problems with the power switch");
                            }

                            try {
                                // "O2_switch":false
                                switchTwo.setChecked(response.getBoolean("fan_switch"));
                            } catch (Exception e) {
                                switchTwo.setChecked(false);
                                Log.i(logTag, "Problems with the O2 switch");
                            }

                            try {
                                // "switch3":false
                                switchThree.setChecked(response.getBoolean("O2_switch"));
                            } catch (Exception e) {
                                switchThree.setChecked(false);
                                Log.i(logTag, "Problems with switch three");
                            }

                            try {
                                // "switch4":false
                                switchFour.setChecked(response.getBoolean("switch3"));
                            } catch (Exception e) {
                                switchFour.setChecked(false);
                                Log.i(logTag, "Problems with switch 4");
                            }

                            try {
                                // "switch5":false
                                switchFive.setChecked(response.getBoolean("switch4"));
                            } catch (Exception e) {
                                switchFive.setChecked(false);
                                Log.i(logTag, "Problems with switch 5");
                            }

                            try {
                                // "fan_switch":false
                                switchSix.setChecked(response.getBoolean("switch5"));
                            } catch (Exception e) {
                                switchSix.setChecked(false);
                                Log.i(logTag, "Problems with switch 5");
                            }
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

            Volley.newRequestQueue(applicationContext).add(jsonObjectRequest);

            // --------- OLD METHOD ------------------

            /*try {
                Log.i("DCU API Call", "Initiating Call");

                java.net.URL url = new URL(request);
                url.openConnection().setConnectTimeout(6000);

                InputStream in = url.openStream();
                Scanner streamInput = new Scanner(in, StandardCharsets.UTF_8.name());

                jsonResponse = streamInput.nextLine();

                streamInput.close();
                in.close();

                JSONObject jsonObject = new JSONObject(jsonResponse);
                Log.i(logTag, jsonObject.toString());

                //the json data does not come as a json array so we use jsonObjects to get each value in each object
                //not the jsonobject out of an array which was the original approach
                JSONObject jsonSuitPwer = jsonObject.getJSONObject("0");
                suitPwr = jsonSuitPwer.getBoolean("value");

                JSONObject jsonFanSwth = jsonObject.getJSONObject("5");
                fanSwth = jsonFanSwth.getBoolean("value");

                JSONObject jsonO2Swth = jsonObject.getJSONObject("1");
                o2Swth = jsonO2Swth.getBoolean("value");

                JSONObject jsonrcaSwth = jsonObject.getJSONObject("2");
                rcaSwth = jsonrcaSwth.getBoolean("value");

                JSONObject jsonauxSwth = jsonObject.getJSONObject("3");
                auxSwth = jsonauxSwth.getBoolean("value");

                JSONObject jsonpumpSwth = jsonObject.getJSONObject("4");
                pumpSwth = jsonpumpSwth.getBoolean("value");

            } catch (Exception e) {
                Log.e("DCU API Call", e.toString());
            }*/

            return "API Call Complete";
        }

        protected void onPostExecute(String result) {
            Log.i("DCU API Call", "Result: " + result);

            //switchOne.setChecked(suitPwr);
            //switchTwo.setChecked(fanSwth);
            //switchThree.setChecked(o2Swth);
            //switchFour.setChecked(rcaSwth);
            //switchFive.setChecked(auxSwth);
            //switchSix.setChecked(pumpSwth);
        }
    }

    //we have to use multithreading for additional astronauts as we can't update different values from a single call
    //so we use two async tasks to get the job done
    private class GetFromAPI2 extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private boolean suitPwr2; //connect to switches below?
        private boolean fanSwth2;
        private boolean o2Swth2;
        private boolean rcaSwth2;
        private boolean auxSwth2;
        private boolean pumpSwth2;

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

                JSONObject jsonSuitPwer = jsonObject.getJSONObject("0");
                suitPwr2 = jsonSuitPwer.getBoolean("value");

                JSONObject jsonFanSwth = jsonObject.getJSONObject("5");
                fanSwth2 = jsonFanSwth.getBoolean("value");

                JSONObject jsonO2Swth = jsonObject.getJSONObject("1");
                o2Swth2 = jsonO2Swth.getBoolean("value");

                JSONObject jsonrcaSwth = jsonObject.getJSONObject("2");
                rcaSwth2 = jsonrcaSwth.getBoolean("value");

                JSONObject jsonauxSwth = jsonObject.getJSONObject("3");
                auxSwth2 = jsonauxSwth.getBoolean("value");

                JSONObject jsonpumpSwth = jsonObject.getJSONObject("4");
                pumpSwth2 = jsonpumpSwth.getBoolean("value");

            } catch (Exception e) {
                Log.e("API Call2", e.toString());
            }

            return "API Call Complete";
        }

        protected void onPostExecute(String result) {
            Log.i("API Call2", "Result: " + result);

            switchSuitPwr2.setChecked(suitPwr2);
            switchFanSwt2.setChecked(fanSwth2);
            switchO2Swt2.setChecked(o2Swth2);
            switchRca2.setChecked(rcaSwth2);
            switchAux2.setChecked(auxSwth2);
            switchPump2.setChecked(pumpSwth2);
        }
    }
}
