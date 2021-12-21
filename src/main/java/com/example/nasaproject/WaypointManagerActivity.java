package com.example.nasaproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;

// TODO: 5/20/21 Add a label that allows users to specify the name of the waypoint.
// TODO: 5/20/21 Add a way for the MCC operator to monitor the astronaut's coordinates.
// TODO: 5/20/21 Add an actual background to the map that shows the aerial view of the coordinates.
// TODO: 5/20/21 Complete the selector for presets.
// TODO: 5/20/21 The ratio of the map's sides should be to scale to get the idea of the field.
// TODO: 5/20/21 Create a way to select an active waypoint.
// TODO: 5/20/21 Create a way to edit the waypoints without restarting from scratch.
// TODO: 5/20/21 Switch between GPS and relative positioning.
// TODO: 5/20/21 Show coordinates on the map.
// TODO: 5/20/21 Get other colors/icons for major waypoints (such as base, rover, ...)

public class WaypointManagerActivity extends AppCompatActivity {

    private ImageView moonMap;
    private TextView xCoord;
    private TextView yCoord;
    private TextView xTitle;
    private TextView yTitle;
    private Button setWaypointButton;
    private Button clearWaypointsButton;
    private Button closeMapButton;

    private Button presetLanderButton;
    private Button presetRoverButton;
    private Button presetSampleSiteButton;

    private Spinner presetsSpinner;

    // private int n = 0;
    // private ArrayList<ArrayList<Integer>> coordsArray = new ArrayList<>(2);

    private int waypointRadius = 20;
    private int waypointRadiusBorder = 5;
    private ArrayList<Waypoint> waypoints = new ArrayList<>();
    private int activeWaypoint = -1;

    private WaypointManager waypointManager;

    public void setActiveWaypoint(int activeWaypoint) {
        if ( activeWaypoint >= waypoints.size() ) {
            return;
        }

        if ( waypoints.size() == 0 ) {
            this.activeWaypoint = -1;
            return;
        }

        this.activeWaypoint = activeWaypoint;

        drawWaypoints();
    }

    public int getActiveWaypoint() {
        return activeWaypoint;
    }

    private static final String TAG = WaypointManagerActivity.class.getSimpleName();

    private RecyclerView waypointListRecyclerView;

    private String address;// = "http://192.168.0.107:8123";
    private String eva_id = Long.toString(new Date().getTime());

    // Attributes related to the map.

    private double bottomLatitude = 29.5582d;
    private double topLatitude = 29.55956d;

    private double rightLongitude = -95.0915d;
    private double leftLongitude = -95.09340d;

    private double multiplicationFactor = 10000d;

    // TODO: 3/26/21 Continue working on this (GV)
    public void drawWaypoints() {
        drawWaypoints(null, null);
    }

    public void drawWaypoints(String activeWaypointID) {
        drawWaypoints(activeWaypointID, null);
    }

    public void drawWaypoints(Waypoint candidateWaypoint) {
        drawWaypoints(null, candidateWaypoint);
    }

    private void drawWaypoints(String activeWaypointID, Waypoint candidateWaypoint) {
        // Stopping the drawWaypoints if there is nothing to draw or draw on
        if ( moonMap == null || waypoints == null )
            return;

        // Now that we know there is a map and there are waypoints, we can draw

        // TODO: 3/26/21 Continue from here... GV

        Log.i(TAG, "Drawing waypoints...");

        ArrayList<Waypoint> waypoints = waypointManager.getWaypoints();

        waypointManager.logWaypoints();

        for ( Waypoint w : waypoints ) {
            Log.i(TAG, "Waypoint: " + w.toString());
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moonnavtest);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        canvas.drawBitmap(bitmap, 0, 0, null);

        // Drawing the paths between consecutive waypoints
        // If we draw the paths first, the waypoints will show above the lines

        Paint pathPaint = new Paint();
        pathPaint.setColor(getResources().getColor(R.color.waypointPath));
        pathPaint.setStrokeWidth(3);
        pathPaint.setStyle(Paint.Style.STROKE);

        int xOri, yOri, xDest, yDest = 0;

        // Draw the paths between consecutive waypoints
        for ( int i = 0; i < waypoints.size()-1; i++ ) {
            xOri = (int)(waypoints.get(i).xCoord * (double)moonMap.getWidth());
            yOri = (int)(waypoints.get(i).yCoord * (double)moonMap.getHeight());

            xDest = (int)(waypoints.get(i+1).xCoord * (double)moonMap.getWidth());
            yDest = (int)(waypoints.get(i+1).yCoord * (double)moonMap.getHeight());

            canvas.drawLine(xOri,yOri,xDest,yDest,pathPaint);
        }

        // Drawing the path from the last waypoint to the candidate waypoint, if one exists
        if ( candidateWaypoint != null && waypoints.size() > 0 ) {
            pathPaint.setColor(getResources().getColor(R.color.candidatePath));

            xOri = (int)(waypoints.get(waypoints.size()-1).xCoord * (double)moonMap.getWidth());
            yOri = (int)(waypoints.get(waypoints.size()-1).yCoord * (double)moonMap.getHeight());
            xDest = (int)(candidateWaypoint.xCoord * (double)moonMap.getWidth());
            yDest = (int)(candidateWaypoint.yCoord * (double)moonMap.getHeight());

            canvas.drawLine(xOri,yOri,xDest,yDest,pathPaint);
        }

        // Drawing the waypoints

        Paint paint = new Paint();

        // Iterate through the array list and draw every point that is stored - MA
        for ( int i = 0; i < waypoints.size(); i++ ) {
            int x = (int)(waypoints.get(i).xCoord * (double)moonMap.getWidth());
            int y = (int)(waypoints.get(i).yCoord * (double)moonMap.getHeight());

            if ( activeWaypoint == i ) {
                paint.setColor(Color.BLACK);
                canvas.drawCircle(x,y,(waypointRadius + waypointRadiusBorder * 2),paint);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(x,y,(waypointRadius + waypointRadiusBorder),paint);
            }

            if ( i == 0 )
                paint.setColor(getResources().getColor(R.color.firstWaypoint));
            else if ( i == waypoints.size()-1 )
                paint.setColor(getResources().getColor(R.color.finalWaypoint));
            else
                paint.setColor(getResources().getColor(R.color.intermediateWaypoint));

            canvas.drawCircle(x,y,waypointRadius,paint);
        }

        // Drawing the candidate waypoint, if one exists
        if ( candidateWaypoint != null ) {
            paint.setColor(getResources().getColor(R.color.candidateWaypoint));

            int x = (int)(candidateWaypoint.xCoord * (double)moonMap.getWidth());
            int y = (int)(candidateWaypoint.yCoord * (double)moonMap.getHeight());

            canvas.drawCircle(x,y,waypointRadius,paint);
        }
        
        moonMap.setImageDrawable(new BitmapDrawable(getResources(), newBitmap));

        waypointListRecyclerView.setAdapter(new WaypointListRecyclerAdapter(this, waypoints));
    }

    /**
     * This method takes GPS coordinates and converts them into coordinates in the 0-1 range.
     * It is important that the system has reference points for the top-left and bottom-right
     * coordinates delimiting the field.
     * @param latitude Latitude expressed in decimals
     * @param longitude Longitude expressed in decimals
     */
    
    private void setCandidateWaypointFromCoords(double latitude, double longitude) {
        double fieldWidth = Math.abs(leftLongitude - rightLongitude)*multiplicationFactor;
        double fieldHeight = Math.abs(topLatitude - bottomLatitude)*multiplicationFactor;

        Log.i(TAG, "Field width: " + fieldWidth);
        Log.i(TAG, "Field height: " + fieldHeight);

        double lat = latitude;
        double longit = longitude;

        Log.i(TAG, "Setting the following Latitude and Longitude: [" + latitude + "," + longitude + "]");

        double latDiff = Math.abs(topLatitude - lat)*multiplicationFactor;
        double longDiff = Math.abs(leftLongitude - longit)*multiplicationFactor;

        double relLat = latDiff / fieldHeight;
        double relLong = longDiff / fieldWidth;

        Log.i(TAG, "Relative latitude: " + relLat);
        Log.i(TAG, "Relative longitude: " + relLong);

        Log.i(TAG, "Adding the candidate coordinate at [" + relLong + "," + relLat + "]");

        xCoord.setText(Double.toString(relLong).substring(0, 10));
        yCoord.setText(Double.toString(relLat).substring(0, 10));

        Waypoint candidateWaypoint = new Waypoint();
        candidateWaypoint.xCoord = relLong;
        candidateWaypoint.yCoord = relLat;
        drawWaypoints(candidateWaypoint);
    }

    /**
     * Clears the waypoints and restores the original image.
     */
    private void clearWaypoints() {
        // Remove all waypoints from the server and locally
        Log.i(TAG, "Clearing waypoints");

        // Removing the values within the TextViews
        Log.i(TAG, "Clearing the UI");
        xCoord.setText("");
        yCoord.setText("");

        // Clearing the waypoint manager
        waypointManager.clearWaypoints();
    }

    private void storeCoordinateReferences() {
        String url = AddressManager.getArgosAddress(getApplicationContext());
        url += "/coordsrefs/set/?min_lat="+bottomLatitude+"&max_lat="+topLatitude+"&min_long="+rightLongitude+"&max_long="+leftLongitude;
        Log.i(TAG, "Call: " + url);

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Successful request...");
                        Log.i(TAG, "Response: " + response);
                        Toast.makeText(getApplicationContext(), "Coordinate References Ready", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "An error occurred...");
                Log.i(TAG, error.toString());
                Toast.makeText(getApplicationContext(), "Problems storing coordinate refs", Toast.LENGTH_LONG).show();
            }
        }
        );
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_manager); //old ContentView used for getting coordinates in last version
        //setContentView(new CustomView(this)); //use this contentView or one above?

        waypointManager = new WaypointManager(getApplicationContext(), this, true);

        storeCoordinateReferences();

        //Context context = getApplicationContext();
        //SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        address = AddressManager.getArgosAddress(getApplicationContext()); //sharedPreferences.getString(MainActivity.ARGOS_ADDR, getResources().getString(R.string.default_argos_address));
        Log.i(TAG, "Address from preferences: " + address);

        this.moonMap = (ImageView)this.findViewById(R.id.moonMap);
        //this.moonMap.setImageResource(R.drawable.moonnavtest);
        this.xCoord = (TextView)this.findViewById(R.id.xPos);
        this.yCoord = (TextView)this.findViewById(R.id.yPos);
        this.xTitle = (TextView)this.findViewById(R.id.xView);
        this.yTitle = (TextView)this.findViewById(R.id.yView);

        this.setWaypointButton = (Button)this.findViewById(R.id.setWaypointBtn);
        this.clearWaypointsButton = (Button)this.findViewById(R.id.clearWaypointsBtn);
        this.closeMapButton = (Button)this.findViewById(R.id.closeMapBtn);

        closeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //CustomView customView = new CustomView(this); //is this CustomView needed? -CG I don't think so. -GV
        setWaypointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("submitBtn", "I just clicked the button...");

                Waypoint newWaypoint = new Waypoint();
                newWaypoint.id = "waypoint-" + waypointManager.count();
                newWaypoint.xCoord = Double.parseDouble(xCoord.getText().toString());
                newWaypoint.yCoord = Double.parseDouble(yCoord.getText().toString());

                waypointManager.addWaypoint(newWaypoint);

                xCoord.setText("");
                yCoord.setText("");
            }
        });

        clearWaypointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waypointManager.clearWaypoints();
                clearWaypoints();
            }
        });

        // TODO: 4/2/21 Complete the preset buttons
        presetLanderButton = findViewById(R.id.preset_base);
        presetLanderButton.setText("Lander\nLat: 29.5586574\nLong: -95.0920556");
        presetLanderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //double bottomLatitude = 29.558d;
                //double topLatitude = 29.560d;

                //double rightLongitude = -95d;
                //double leftLongitude = -96d;

                //double multiplicationFactor = 10000d;

                setCandidateWaypointFromCoords(29.5586574, -95.0920556);

                /*double fieldWidth = Math.abs(leftLongitude - rightLongitude)*multiplicationFactor;
                double fieldHeight = Math.abs(topLatitude - bottomLatitude)*multiplicationFactor;

                Log.i(TAG, "Field width: " + fieldWidth);
                Log.i(TAG, "Field height: " + fieldHeight);

                // TODO: Turn this into a method
                double lat = 29.5586574;
                double longit = -95.0920556;

                double latDiff = Math.abs(topLatitude - lat)*multiplicationFactor;
                double longDiff = Math.abs(leftLongitude - longit)*multiplicationFactor;

                double relLat = latDiff / fieldHeight;
                double relLong = longDiff / fieldWidth;

                Log.i(TAG, "Relative latitude: " + relLat);
                Log.i(TAG, "Relative longitude: " + relLong);

                Log.i(TAG, "Adding the candidate coordinate at [" + relLong + "," + relLat + "]");

                xCoord.setText(Double.toString(relLong).substring(0, 10));
                yCoord.setText(Double.toString(relLat).substring(0, 10));

                Waypoint candidateWaypoint = new Waypoint();
                candidateWaypoint.xCoord = relLong;
                candidateWaypoint.yCoord = relLat;
                drawWaypoints(candidateWaypoint);*/
            }
        });

        presetRoverButton = findViewById(R.id.preset_rover);
        presetRoverButton.setText("Rover\nLat: 29.5594941\nLong: -95.0932222");
        presetRoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lat: 29.5594941,
                // Long: -95.0932222

                setCandidateWaypointFromCoords(29.5594941, -95.0932222);

                /*double fieldWidth = Math.abs(leftLongitude - rightLongitude)*multiplicationFactor;
                double fieldHeight = Math.abs(topLatitude - bottomLatitude)*multiplicationFactor;

                Log.i(TAG, "Field width: " + fieldWidth);
                Log.i(TAG, "Field height: " + fieldHeight);

                double lat = 29.5594941;
                double longit = -95.0932222;

                double latDiff = Math.abs(topLatitude - lat)*multiplicationFactor;
                double longDiff = Math.abs(leftLongitude - longit)*multiplicationFactor;

                double relLat = latDiff / fieldHeight;
                double relLong = longDiff / fieldWidth;

                Log.i(TAG, "Relative latitude: " + relLat);
                Log.i(TAG, "Relative longitude: " + relLong);

                Log.i(TAG, "Adding the candidate coordinate at [" + relLong + "," + relLat + "]");

                xCoord.setText(Double.toString(relLong).substring(0, 10));
                yCoord.setText(Double.toString(relLat).substring(0, 10));

                Waypoint candidateWaypoint = new Waypoint();
                candidateWaypoint.xCoord = relLong;
                candidateWaypoint.yCoord = relLat;
                drawWaypoints(candidateWaypoint);*/
            }
        });

        presetSampleSiteButton = findViewById(R.id.preset_samplesite);
        presetSampleSiteButton.setText("Selenology Site\nLat: 29.5586044\nLong: -95.0919444");
        presetSampleSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lat: 29.5586044,
                // Long: -95.0919444

                setCandidateWaypointFromCoords(29.5586044, -95.0919444);

                /*
                double fieldWidth = Math.abs(leftLongitude - rightLongitude)*multiplicationFactor;
                double fieldHeight = Math.abs(topLatitude - bottomLatitude)*multiplicationFactor;

                Log.i(TAG, "Field width: " + fieldWidth);
                Log.i(TAG, "Field height: " + fieldHeight);

                double lat = 29.5586044;
                double longit = -95.0919444;

                double latDiff = Math.abs(topLatitude - lat)*multiplicationFactor;
                double longDiff = Math.abs(leftLongitude - longit)*multiplicationFactor;

                double relLat = latDiff / fieldHeight;
                double relLong = longDiff / fieldWidth;

                Log.i(TAG, "Relative latitude: " + relLat);
                Log.i(TAG, "Relative longitude: " + relLong);

                Log.i(TAG, "Adding the candidate coordinate at [" + relLong + "," + relLat + "]");

                xCoord.setText(Double.toString(relLong).substring(0, 10));
                yCoord.setText(Double.toString(relLat).substring(0, 10));

                Waypoint candidateWaypoint = new Waypoint();
                candidateWaypoint.xCoord = relLong;
                candidateWaypoint.yCoord = relLat;
                drawWaypoints(candidateWaypoint);
                */
            }
        });

        presetsSpinner = findViewById(R.id.presets_spinner);
        presetsSpinner.setEnabled(false);

        //create request queue outside listener - CG
        final RequestQueue requestQueue = Volley.newRequestQueue(this); //for submit button

        // onHoverListener? Maybe later... not working now...
        this.moonMap.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent motionEvent) {
                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();

                Log.i(TAG, "Hovering over [" + x + "," + y + "]");
                return false;
            }
        });

        //gets x & y coordinates to display in textviews & send to octavia - CG
        this.moonMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View customView, MotionEvent motionEvent) { //should CustomView replace myView here? - CG
                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();

                // initialize an ArrayList of ArrayLists - MA
                //coordsArray.add(new ArrayList<Integer>());
                // add x & y coordinates to Array List - MA
                //coordsArray.get(n).add(x);
                //coordsArray.get(n).add(y);

                double imageWidth = moonMap.getWidth();
                double imageHeight = moonMap.getHeight();

                double normalizedX = ((double)(x)/imageWidth);
                double normalizedY = ((double)(y)/imageHeight);

                // Rounding to two decimal places
                normalizedX = (double)(Math.round(normalizedX*100d)/100d);
                normalizedY = (double)(Math.round(normalizedY*100d)/100d);

                Log.i("Image", "Clicked on coords x and y " + x + ", " + y + " - " + moonMap.getWidth() + "x" + moonMap.getHeight());

                //Log.i("Testing","List " + n + ":" + coordsArray.get(n).get(0) + ' ' + coordsArray.get(n).get(1));

                //display coordinates in textviews - CG
                //xCoord.setText(String.valueOf(x) + " | Norm: " + normalizedX);
                //yCoord.setText(String.valueOf(y) + " | Norm: " + normalizedY);
                xCoord.setText(Double.toString(normalizedX));
                yCoord.setText(Double.toString(normalizedY));

                Waypoint candidateWaypoint = new Waypoint();
                candidateWaypoint.xCoord = normalizedX;
                candidateWaypoint.yCoord = normalizedY;
                drawWaypoints(candidateWaypoint);

                /*/ GV Testing - Start
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moonnavtest);

                Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(newBitmap);

                canvas.drawBitmap(bitmap, 0, 0, null);

                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#ff0000")); // Red

                //created a new Paint for the line between points - MA
                Paint paint2 = new Paint();

                paint2.setColor(Color.GREEN);

                paint2.setStrokeWidth(3);

                paint2.setStyle(Paint.Style.STROKE);

                //iterate through the array list and draw every point that is stored - MA
                for(int i =0; i<coordsArray.size(); i++){
                    canvas.drawCircle(coordsArray.get(i).get(0),coordsArray.get(i).get(1),25, paint);
                }

                //only try and draw a line between points if there is more than 1 point
                if(coordsArray.size() >=2){
                    canvas.drawLine(coordsArray.get(n-1).get(0),coordsArray.get(n-1).get(1),coordsArray.get(n).get(0),coordsArray.get(n).get(1),paint2);
                }

                //canvas.drawCircle(x, y, 25, paint);
                //moonMap.setImageBitmap(bitmap); // TODO: Continue from here
                moonMap.setImageDrawable(new BitmapDrawable(getResources(), newBitmap));
                // GV Testing - End

                //submit button will send coordinates in a json object to octavia; not finished - CG
                /*submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //create url for octavia
                        //String url = "https://localhost:3000/api/...rest of url

                        Log.i("SubmitBtn", "Testing...");

                        //string vars for each property in the array
                        String id;
                        String loc_id;
                        String coords;
                        String description;
                        String title;
                        String images;
                        String owner;
                        String staticx;
                    }
                });*/

                //increase the first coordArray index - MA
                //n = n+1;

                return false;
            }



            //onTouchEvent to create circle as waypoint on map; not finished - CG
            /*@Override //how do I get CustomView into activity? As line 44 or inner class in onTouchEvent? -CG
            public boolean onTouchEvent(MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (surfaceHolder.getSurface().isValid()) {
                        Canvas canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.YELLOW);
                        canvas.drawCircle(event.getX(), event.getY(), 50, paint);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    Log.i("Image2", "Waypoint created");
                }
                return false;
            }*/

            //onTouchEvent to handle drawing a path; not finished
            /*@Override
            public boolean onTouchEvent(MotionEvent event)
            {
                int x = (int)event.getX();
                int y = (int)event.getY();
                Log.i("Image2", "coords x and y " + x + " , " + y);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                }

                return false;
            }*/

        });

        waypointListRecyclerView = findViewById(R.id.waypoints_list);
        waypointListRecyclerView.setHasFixedSize(true);
        waypointListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        waypointListRecyclerView.setAdapter(new WaypointListRecyclerAdapter(this, waypoints));
    }
}