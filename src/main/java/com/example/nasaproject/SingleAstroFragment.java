package com.example.nasaproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
Class that allows viewing of a single astronauts telemetry stream
TODO: Allow user to change Fahrenheit to Celsius
TODO: Add more telemetry data points
 */

public class SingleAstroFragment extends Fragment {
    private TextView OutsideP;
    private TextView Fan;
    private TextView O2P;
    private TextView BattCap;
    private TextView Temp;

    // Creating the recycler view and adapter
    private RecyclerView telemetryRowRecyclerView; //adding recycler view for telemetry data list
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private final String URL = "http://192.168.1.10:8002/telemetry";


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_single_astro, container, false);
        //will hold telemetry for astro

        //Commented out to test RecyclerView
//        OutsideP = rootView.findViewById(R.id.outP);
//        Fan = rootView.findViewById(R.id.fan);
//        O2P = rootView.findViewById(R.id.oP);
//        BattCap = rootView.findViewById(R.id.battery);
//        Temp = rootView.findViewById(R.id.outTemp);

        //TODO: finish setting up recycler view for telemetry, may need to be moved; suggestion - use a for-loop to iterate through the object indices to display each data point on a separate row
        //TODO: The following statement causes a crash (R.id.telemetry_recycler_list is null)
//        telemetryRowRecyclerView.findViewById(R.id.telemetry_recycler_list);
        //telemetryRowRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //telemetryRowRecyclerView.setAdapter(new TelemetryRowRecyclerAdapter(this, arrayName));

        new GetFromAPI().execute(URL);

        return rootView;
    }

    private class GetFromAPI extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private int outsideP;
        private int fan;
        private String oxygenpressure;
        private String oxygenflow;
        private String batterycapacity;
        private String outTemp;



        @Override
        protected String doInBackground(String... strings) {
            String request = strings[0];

            Log.i("Api Call", "Request: " + request);

            try {
                Log.i("API Call", "Initiating Call");

                java.net.URL url = new URL(request);
                url.openConnection().setConnectTimeout(6000);

                InputStream in = url.openStream();
                Scanner streamInput = new Scanner(in, StandardCharsets.UTF_8.name());

                jsonResponse = streamInput.nextLine();

                streamInput.close();
                in.close();

                //JSONArray arrayData = new JSONArray(jsonResponse);

                JSONObject jsonObject = new JSONObject(jsonResponse);

//                JSONObject jsonPressure = jsonObject.getJSONObject("8");
                outsideP = jsonObject.getInt("p_sub");
//
//                JSONObject jsonFan = jsonObject.getJSONObject("10");
                fan = jsonObject.getInt("v_fan");
//
//                JSONObject jsonOxygenPressure = jsonObject.getJSONObject("2");
                oxygenpressure = jsonObject.getString("p_o2");
//
//                JSONObject jsonOxygenFlow = jsonObject.getJSONObject("3");
                oxygenflow = jsonObject.getString("rate_o2");
//
//                JSONObject jsonBatteryCap = jsonObject.getJSONObject("4");
                batterycapacity = jsonObject.getString("cap_battery");
//
//                JSONObject jsonOutTemp = jsonObject.getJSONObject("9");
                outTemp = jsonObject.getString("t_sub");


            } catch (Exception e) {
                Log.e("API Call", e.toString());
            }

            return jsonResponse;
        }

        protected void onPostExecute(String result) {
            Log.i("API Call", "Result: " + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            outsideP = jsonObject.getInt("p_sub");
            fan = jsonObject.getInt("v_fan");
            oxygenpressure = jsonObject.getString("p_o2");
            oxygenflow = jsonObject.getString("value");
            batterycapacity = jsonObject.getString("cap_battery");
            outTemp = jsonObject.getString("t_sub");

            OutsideP.setText("Outside Pressure: " + (outsideP) + " psia");
            Fan.setText("Fan Speed: " + (fan) + " rpm");
            O2P.setText("Oxygen Tank Pressure and Usage: " + oxygenpressure + " psia / " + oxygenflow + " psia/min");
            BattCap.setText("Battery Capacity: " + batterycapacity + " amp/hr");
            Temp.setText("Temperature Outside: " + outTemp + "\u00B0" + "F");
        } catch (Exception e) {
            Log.e("API Call", e.toString());
        }
        }
    }
}
