package com.example.nasaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BeaconsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeaconsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView beacon0View, beacon1View, beacon2View, beacon3View;
    private TextView sw_nw, sw_ne, sw_se, nw_ne, nw_se, ne_se;

    public BeaconsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Beacon.
     */

    // TODO: Rename and change types and number of parameters
    public static BeaconsFragment newInstance(String param1, String param2) {
        BeaconsFragment fragment = new BeaconsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_beacons, container, false);

        beacon0View = rootView.findViewById(R.id.beacon0);
        beacon1View = rootView.findViewById(R.id.beacon1);
        beacon2View = rootView.findViewById(R.id.beacon2);
        beacon3View = rootView.findViewById(R.id.beacon3);

        sw_nw = rootView.findViewById(R.id.sw_nw);
        sw_ne = rootView.findViewById(R.id.sw_ne);
        sw_se = rootView.findViewById(R.id.sw_se);
        nw_ne = rootView.findViewById(R.id.nw_ne);
        nw_se = rootView.findViewById(R.id.nw_se);
        ne_se = rootView.findViewById(R.id.ne_se);

        // TODO: 5/24/21 Complete this section with the pinging of the beacons

        return rootView;
    }
}