package com.example.nasaproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public class UIAFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView uiaHeader;
    private TextView uiaTimer;
    private TextView emu1;
    private TextView emu2;
    private TextView ev1Supply;
    private TextView ev2Supply;
    private TextView ev1Waste;
    private TextView ev2Waste;
    private TextView ev1Oxygen;
    private TextView ev2Oxygen;
    private TextView o2Vent;
    private TextView depressPump;
    private TextView o2SupPres1;
    private TextView o2SupPres2;
    private TextView o2SupOut1;
    private TextView o2SupOut2;
    private TextView o2SupPresData1;
    private TextView o2SupPresData2;
    private TextView o2SupOutData1;
    private TextView o2SupOutData2;

    private Switch switchOne;
    private Switch switchTwo;
    private Switch switchThree;
    private Switch switchFour;
    private Switch switchFive;
    private Switch switchSix;
    private Switch switchSeven;
    private Switch switchEight;
    private Switch switchA;
    private Switch switchB;

    private final String host1 = "http://192.168.1.191:3000/api/simulation/";
    final String logTag = "UIA State";
    View myView;
    protected Handler handler = new Handler();

    public void useAPI(String URL) {
        System.gc();
        new GetFromAPI().execute(URL);
    }

    public UIAFragment() {
        //required empty public constructor
    }

    public static UIAFragment newInstance(String param1, String param2) {
        UIAFragment fragment = new UIAFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return myView = inflater.inflate(R.layout.fragment_uia, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        uiaHeader = myView.findViewById(R.id.uia_header);
        uiaTimer = myView.findViewById(R.id.uia_timer);
        emu1 = myView.findViewById(R.id.emu1);
        emu2 = myView.findViewById(R.id.emu2);
        ev1Supply = myView.findViewById(R.id.ev1);
        ev2Supply = myView.findViewById(R.id.ev2);
        ev1Waste = myView.findViewById(R.id.ev1w);
        ev2Waste = myView.findViewById(R.id.ev2w);
        ev1Oxygen = myView.findViewById(R.id.ev1o);
        ev2Oxygen = myView.findViewById(R.id.ev2o);
        o2Vent = myView.findViewById(R.id.o2vent);
        depressPump = myView.findViewById(R.id.depressPump);
        o2SupPres1 = myView.findViewById(R.id.o2sp1);
        o2SupPres2 = myView.findViewById(R.id.o2sp2);
        o2SupPresData1 = myView.findViewById(R.id.o2sp1Data);
        o2SupPresData2 = myView.findViewById(R.id.o2sp2Data);
        o2SupOut1 = myView.findViewById(R.id.o2so1);
        o2SupOut2 = myView.findViewById(R.id.o2so2);
        o2SupOutData1 = myView.findViewById(R.id.o2so1Data);
        o2SupOutData2 = myView.findViewById(R.id.o2so2Data);

        switchOne = myView.findViewById(R.id.switch1);
        switchTwo = myView.findViewById(R.id.switch2);
        switchThree = myView.findViewById(R.id.switch3);
        switchFour = myView.findViewById(R.id.switch4);
        switchFive = myView.findViewById(R.id.switch5);
        switchSix = myView.findViewById(R.id.switch6);
        switchSeven = myView.findViewById(R.id.switch7);
        switchEight = myView.findViewById(R.id.switch8);
        switchA = myView.findViewById(R.id.switchA);
        switchB = myView.findViewById(R.id.switchB);

        new GetFromAPI().execute(host1);

        //TODO: update them
        //TODO: every cycle of a certain amount of time

        //switch for emu1 -CG
        switchA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format


                try {

                    String url = host1 + "newcontrols?emu1=";
                    if (switchA.isChecked()) {
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
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });


        //switch for emu2 -CG
        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format

                try {


                String url = host1 + "newcontrols?emu2=";
                if (switchB.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for EV1 supply -CG
        switchOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?ev1_supply=";
                if (switchOne.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for EV2 supply -CG
        switchTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?ev2_supply=";
                if (switchTwo.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for EV1 waste -CG
        switchThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?ev1_waste=";
                if (switchThree.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for EV2 waste -CG
        switchFour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?ev2_waste=";
                if (switchFour.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for EV1 oxygen -CG
        switchFive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?emu1_O2=";
                if (switchFive.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for EV2 oxygen -CG
        switchSix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?emu2_O2=";
                if (switchSix.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for oxygen vent -CG
        switchSeven.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?O2_vent=";
                if (switchSeven.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //switch for depress pump -CG
        switchEight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                try {


                String url = host1 + "newcontrols?depress_pump=";
                if (switchEight.isChecked()) {
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "true";
                }else {
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
                }catch (Exception e) {
                    Log.e("Error", e.toString());
                    Toast toast = Toast.makeText(getContext().getApplicationContext(), "Error Connecting", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                new GetFromAPI().execute(host1);

                handler.postDelayed(this, 20000);
            } catch (Exception e) {
                Log.e("API Call", e.toString());
            }
        }
    };

    //The class we use to get the telemetry data from our local telemetry server
    private class GetFromAPI extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private boolean ev1s;
        private boolean ev2s;
        private boolean ev1w;
        private boolean ev2w;
        private boolean ev1o;
        private boolean ev2o;
        private boolean o2V;
        private boolean depressP;
        private String uiaTime;
        private boolean switchAy;
        private boolean switchBee;
        private String o2suppres1;
        private String o2suppres2;
        private String o2supout1;
        private String o2supout2;

        @Override
        protected String doInBackground(String... strings){
            String request = strings[0];

            Log.i("Api Call","Request: " + request);

            try {
                Log.i("API Call", "Initiating Call");

                java.net.URL url = new URL(request);
                url.openConnection().setConnectTimeout(6000);

                InputStream in = url.openStream();
                Scanner streamInput = new Scanner(in, StandardCharsets.UTF_8.name());

                jsonResponse = streamInput.nextLine();

                streamInput.close();
                in.close();


                JSONObject jsonObject = new JSONObject(jsonResponse);

                //the json data does not come as a json array so we use jsonObjects to get each value in each object
                //not the jsonobject out of an array which was the original approach
                JSONObject jsonEv1s = jsonObject.getJSONObject("6");
                ev1s = jsonEv1s.getBoolean("value");

                JSONObject jsonEv2s = jsonObject.getJSONObject("7");
                ev2s = jsonEv2s.getBoolean("value");

                JSONObject jsonEv1w = jsonObject.getJSONObject("8");
                ev1w = jsonEv1w.getBoolean("value");

                JSONObject jsonEv2w = jsonObject.getJSONObject("9");
                ev2w = jsonEv2w.getBoolean("value");

                JSONObject jsonEv1o = jsonObject.getJSONObject("10");
                ev1o = jsonEv1o.getBoolean("value");

                JSONObject jsonEv2o = jsonObject.getJSONObject("11");
                ev2o = jsonEv2o.getBoolean("value");

                JSONObject jsono2v = jsonObject.getJSONObject("14");
                o2V = jsono2v.getBoolean("value");

                JSONObject jsonDepressP = jsonObject.getJSONObject("15");
                depressP = jsonDepressP.getBoolean("value");

                JSONObject jsonUiaTime = jsonObject.getJSONObject("1");
                uiaTime = jsonUiaTime.getString("value");

                JSONObject jsonSwitchA = jsonObject.getJSONObject("2");
                switchAy = jsonSwitchA.getBoolean("value");

                JSONObject jsonSwitchB = jsonObject.getJSONObject("3");
                switchBee = jsonSwitchB.getBoolean("value");

                JSONObject jsonO2SP1 = jsonObject.getJSONObject("4");
                o2suppres1 = jsonO2SP1.getString("value");

                JSONObject jsonO2SP2 = jsonObject.getJSONObject("5");
                o2suppres2 = jsonO2SP2.getString("value");

                JSONObject jsonO2SO1 = jsonObject.getJSONObject("12");
                o2supout1 = jsonO2SO1.getString("value");

                JSONObject jsonO2SO2 = jsonObject.getJSONObject("13");
                o2supout2 = jsonO2SO2.getString("value");

            }catch(Exception e){
                Log.e("API Call", e.toString());
            }

            return "API Call Complete";
        }
        protected void onPostExecute(String result) {
            Log.i("API Call", "Result: " + result);

            switchOne.setChecked(ev1s);
            switchTwo.setChecked(ev2s);
            switchThree.setChecked(ev1w);
            switchFour.setChecked(ev2w);
            switchFive.setChecked(ev1o);
            switchSix.setChecked(ev2o);
            switchSeven.setChecked(o2V);
            switchEight.setChecked(depressP);
            uiaTimer.setText(uiaTime);
            switchA.setChecked(switchAy);
            switchB.setChecked(switchBee);
            o2SupPresData1.setText(o2suppres1);
            o2SupPres2.setText(o2suppres2);
            o2SupOutData1.setText(o2supout1);
            o2SupOutData2.setText(o2supout2);
        }
    }
}