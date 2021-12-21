package com.example.nasaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

// Probably this is not used anywhere in the application.

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IPFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView argos_ip;
    TextView xEmu0_ip;
    TextView xEmu1_ip;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME ="argos";

    private static final String SHARED_PREF_XEMU0 = "xo";
    private static final String KEY_XEMU0 = "xemu0";

    private static final String SHARED_PREF_XEMU1 = "x1";
    private static final String KEY_XEMU1 = "xemu1";

    public IPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IpActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static IPFragment newInstance(String param1, String param2) {
        IPFragment fragment = new IPFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_ip, container, false);

        argos_ip = (TextView)rootView.findViewById(R.id.Argos_ip);
        xEmu0_ip = (TextView)rootView.findViewById(R.id.xEmu0_ip);
        xEmu1_ip = (TextView)rootView.findViewById(R.id.xEmu1_ip);

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_XEMU0,Context.MODE_PRIVATE);
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_XEMU1,Context.MODE_PRIVATE);

        String ip_argos = sharedPreferences.getString(KEY_NAME, null);
        String ip_xemu0 = sharedPreferences.getString(KEY_XEMU0, null);
        String ip_xemu1 = sharedPreferences.getString(KEY_XEMU1, null);

        if (ip_argos != null){
            argos_ip.setText("Argos IP - "+ip_argos);
        }

        if (ip_xemu0 != null){
            xEmu0_ip.setText("xEmu 0 IP -" +ip_xemu0);
        }

        if (ip_xemu1 != null){
            xEmu1_ip.setText("xEmu 1 IP - "+ip_xemu1);
        }


        return rootView;
    }
}








