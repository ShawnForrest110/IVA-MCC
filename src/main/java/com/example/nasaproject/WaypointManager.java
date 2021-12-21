package com.example.nasaproject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WaypointManager {
    public WaypointManager(Context applicationContext, WaypointManagerActivity parentActivity, boolean compressCoordinates) {
        Log.i(TAG, "Starting the WaypointManager");

        myWaypoints = new ArrayList<>();
        this.applicationContext = applicationContext;
        this.parentActivity = parentActivity;

        loadWaypoints();

        this.compressCoordinates = compressCoordinates;
    }

    private ArrayList<Waypoint> myWaypoints;
    private Context applicationContext;
    private WaypointManagerActivity parentActivity;
    private final String TAG = "WaypointManager-1.0";

    /*
    Temporary fix note
    The compressedCoordinates is a way to handle the issue with ARGOS. Currently the system only
    stores the x coordinate, so we will have to fit both in there. We will do that by adding x and y
    together in this format: x|y.
     */
    private boolean compressCoordinates;

    public void addWaypoint(Waypoint waypoint) {
        Log.i(TAG, "Adding waypoint: " + waypoint.toString());

        Waypoint waypointToStore = new Waypoint();

        waypointToStore.id = waypoint.id;

        // Scaling the X and Y coordinates
        waypointToStore.xCoord = waypoint.xCoord * 10d;
        waypointToStore.yCoord = waypoint.yCoord * 10d;

        myWaypoints.add(waypointToStore);

        String url;

        if ( compressCoordinates ) { // TODO: 5/19/21 Temporary fix
            String compressedCoords = Double.toString(waypointToStore.xCoord) + "|" + Double.toString(waypointToStore.yCoord);
            url = AddressManager.getArgosAddress(applicationContext) + "/waypoints/addwaypoint?waypoint_id=" + waypointToStore.id + "&eva_id=TEST-EVA&x_coord=" + compressedCoords + "&y_coord=" + compressedCoords + "&description=" + waypointToStore.yCoord;
        } else {
            url = AddressManager.getArgosAddress(applicationContext) + "/waypoints/addwaypoint?waypoint_id=" + waypointToStore.id + "&eva_id=TEST-EVA&x_coord=" + waypointToStore.xCoord + "&y_coord=" + waypointToStore.yCoord + "&description=" + waypointToStore.yCoord;
        }

        Log.i(TAG, "Request to add waypoint: " + url);

        try {
            // Adding the waypoint to ARGOS
            StringRequest stringRequest = new StringRequest(
                    Request.Method.PUT,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Location Test", "Successful request! Now do something...");
                            Log.i("Location Test", "Response: " + response);
                            parentActivity.drawWaypoints();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Location Test", "An error occurred...");
                    Log.i("Location Test", error.toString());
                    parentActivity.drawWaypoints();
                }
            }
            );
            Volley.newRequestQueue(applicationContext).add(stringRequest);

            //Log.i(TAG, "New waypoint: " + waypointToStore);
            //drawWaypoints();
        } catch (Exception e) {
            Log.i(TAG, "An exception occurred trying to save the coordinates.");
        }
    }

    public void clearWaypoints() {
        String url = AddressManager.getArgosAddress(applicationContext) + "/waypoints/clearall";

        Log.i(TAG, "Clearing the waypoints");

        myWaypoints.clear();

        try {
            Log.i(TAG, "Call: " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PATCH,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "Successful connection...");
                            Log.i(TAG, "Response: " + response.toString());
                            Toast.makeText(applicationContext, "Waypoints cleared", Toast.LENGTH_LONG).show();
                            parentActivity.drawWaypoints();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, "Something did not work...");
                            Log.i(TAG, "Error: " + error.toString());
                            parentActivity.drawWaypoints();
                        }
                    }
            );

            Volley.newRequestQueue(applicationContext).add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e(TAG, "There was a problem clearing the waypoints in ARGOS");
            Toast.makeText(applicationContext, "There was a problem clearing the waypoints in ARGOS", Toast.LENGTH_LONG).show();
        }
    }

    private void loadWaypoints() {
        Log.i(TAG, "Loading the current waypoints already in the system");

        String url = AddressManager.getArgosAddress(applicationContext) + "/waypoints/list";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Loading waypoints");
                        Log.i(TAG, response.toString());
                        for ( int i = 0; i < response.length(); i++ ) {
                            try {
                                Waypoint w = new Waypoint();
                                JSONObject waypoint = response.getJSONObject(i);
                                w.id = waypoint.getString("waypoint_id");
                                if ( compressCoordinates ) { // TODO: 5/19/21 Temporary fix
                                    String compressedCoords = waypoint.getString("x_coord");
                                    //Log.i("Decompressing coords", "Coordinates: " + compressedCoords);
                                    String[] coords = compressedCoords.split("\\|");
                                    w.xCoord = Double.parseDouble(coords[0]);
                                    w.yCoord = Double.parseDouble(coords[1]);
                                } else {
                                    w.xCoord = waypoint.getDouble("x_coord");
                                    w.yCoord = waypoint.getDouble("y_coord");
                                }
                                Log.i(TAG, "Loaded waypoint: " + w);
                                myWaypoints.add(w);
                            } catch (Exception e) {
                                Log.e(TAG, "Problems loading the waypoint");
                                Log.i("X", e.toString());
                            }
                        }
                        Log.i(TAG, "The WaypointManager was initialized with " + myWaypoints.size() + " waypoints");
                        parentActivity.drawWaypoints();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Connection error when trying to get the current waypoints...");
                        Log.i(TAG, "Error: " + error.toString());
                        parentActivity.drawWaypoints();
                    }
                }
        );

        Volley.newRequestQueue(applicationContext).add(jsonArrayRequest);
    }

    // When returning the waypoint, we have to format them so that the X and Y coordinates are in the range 0-1
    // In ARGOS, they are in the range 0-10 because of the Magic Leap 1
    public ArrayList<Waypoint> getWaypoints() {
        ArrayList<Waypoint> toReturn = new ArrayList<>();

        for ( Waypoint w : myWaypoints ) {
            Waypoint newWaypoint = new Waypoint();
            newWaypoint.id = w.id;
            newWaypoint.xCoord = w.xCoord / 10d;
            newWaypoint.yCoord = w.yCoord / 10d;
            toReturn.add(newWaypoint);
        }

        return toReturn;
    }

    public void logWaypoints() {
        Log.i(TAG, "--- List of logged waypoints - count: " + myWaypoints.size());
        for ( Waypoint w : myWaypoints )
            Log.i(TAG, "Logged Waypoint: " + w.toString());
    }

    public int count() {
        return myWaypoints.size();
    }
}
