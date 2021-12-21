package com.example.nasaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EVAFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EVAFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EVAFragment() {
        // Required empty public constructor
    }

    /* Commands:
    “Hey argos close science sampling”
    “Hey argos navigate to lander”
    “Hey argos save this location”
    “Hey argos go to step 1”
    “Hey argos go to step 2”
    “Hey argos go to step 3”
    “Hey argos go to step 4”
    “Hey argos go to step 5”
    “Hey argos go to step 6”
    “Hey argos go to step 7”
    “Hey argos close toolkit”
    “Hey argos open toolkit”
    “Hey argos start animation”
    “Hey argos stop animation”
    “Hey argos remove all warnings”
    “Hey Argos, cancel navigation”
    Hey argos show telemetry page 2”
    “Hey argos show astronaut 2
     */

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Eva.
     */
    // TODO: Rename and change types and number of parameters
    public static EVAFragment newInstance(String param1, String param2) {
        EVAFragment fragment = new EVAFragment();
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
        return inflater.inflate(R.layout.fragment_eva, container, false);
    }
}