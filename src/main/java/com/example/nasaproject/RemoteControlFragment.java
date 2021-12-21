package com.example.nasaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoteControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoteControlFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button sendRemoteCommand;
    private Button resetRemoteCommand;
    private TextView commandsLog;

    private static final String TAG = "Remote Controller";

    public RemoteControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemoteControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemoteControlFragment newInstance(String param1, String param2) {
        RemoteControlFragment fragment = new RemoteControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_remote_control, container, false);

        sendRemoteCommand = rootview.findViewById(R.id.btn_issue_command);
        sendRemoteCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner s = rootview.findViewById(R.id.spinner_select_command);
                if ( s.getSelectedItemPosition() == 0 ) {
                    logCommand("Invalid command");
                    Toast.makeText(getContext().getApplicationContext(), "Invalid command selection", Toast.LENGTH_LONG).show();
                    return;
                }

                String command = s.getSelectedItem().toString();

                String urlsafeCommand = command;

                try {
                    urlsafeCommand = URLEncoder.encode(urlsafeCommand, "utf-8");
                } catch (Exception e) {
                    Log.i(TAG, "An exception occurred when encoring the command for URL-safe characters");
                }

                // Sending the command to ARGOS
                String url = AddressManager.getArgosAddress(getContext().getApplicationContext());
                url += "/commands/add/?sender_id=mcc&receiver_id=ml1&command=" + urlsafeCommand;

                StringRequest stringRequest = new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i(TAG, "Successful request...");
                                Log.i(TAG, "Response: " + response);
                                Toast.makeText(getContext().getApplicationContext(), "Command sent", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "An error occurred...");
                        Log.i(TAG, error.toString());
                        Toast.makeText(getContext().getApplicationContext(), "Problems storing the command", Toast.LENGTH_LONG).show();
                    }
                }
                );
                Volley.newRequestQueue(getContext().getApplicationContext()).add(stringRequest);

                // Logging the command to the UI
                logCommand(command);
                s.setSelection(0);
            }
        });

        resetRemoteCommand = rootview.findViewById(R.id.btn_reset_command);
        resetRemoteCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logCommand("Reset command");
                Spinner s = rootview.findViewById(R.id.spinner_select_command);
                s.setSelection(0);
            }
        });

        commandsLog = rootview.findViewById(R.id.textview_command_log);

        //return inflater.inflate(R.layout.fragment_remote_control, container, false);
        return rootview;
    }

    private void logCommand(String command) {
        String currTime = Long.toString(System.currentTimeMillis()/1000);
        currTime = Calendar.getInstance().getTime().toString();
        String newLog = currTime + ": " + command;
        Log.i("Command Logger", newLog);
        newLog += "\n" + commandsLog.getText().toString();
        commandsLog.setText(newLog);
    }
}