package com.example.nasaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText ip_argos_edittext;
    private Button applyArgos_button;

    private EditText ip_xemu0_edittext;
    private Button applyXemu0_button;

    private EditText ip_xemu1_edittext;
    private Button applyXemu1_button;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFrag.
     */

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // ----- Getting the shared preferences -----
        //final SharedPreferences sharedPrefs = getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        //String argos_addr = sharedPrefs.getString(MainActivity.ARGOS_ADDR, getResources().getString(R.string.default_argos_address));
        String argos_addr = AddressManager.getArgosAddress(getContext().getApplicationContext());
        String xemu0_addr = AddressManager.getXemu0Address(getContext().getApplicationContext()); //sharedPrefs.getString(MainActivity.XEMU_0_ADDR, getResources().getString(R.string.default_xemu0_address));
        String xemu1_addr = AddressManager.getXemu1Address(getContext().getApplicationContext()); // sharedPrefs.getString(MainActivity.XEMU_1_ADDR, getResources().getString(R.string.default_xemu1_address));

        // ----- Working on ARGOS -----

        ip_argos_edittext = (EditText) rootView.findViewById(R.id.ip_argos_edittext);
        ip_argos_edittext.setText(argos_addr);

        applyArgos_button = (Button) rootView.findViewById(R.id.applyArgos_button);
        applyArgos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When clicked, the button puts data on shared preferences.
                String new_address = ip_argos_edittext.getText().toString();

                AddressManager.setArgosAddress(new_address, getContext().getApplicationContext());

                Toast.makeText(getContext().getApplicationContext(), "ARGOS: " + new_address, Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: 5/18/21 Add a button for each of the addresses to test the connection.

        //applyArgos_button.setEnabled(false);

        // ----- Working on xEMU-0 -----
        ip_xemu0_edittext = (EditText) rootView.findViewById(R.id.ip_xemu0_edittext);
        ip_xemu0_edittext.setText(xemu0_addr);

        applyXemu0_button = (Button) rootView.findViewById(R.id.applyXemu0_button);
        applyXemu0_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When clicked, the button puts data on shared preferences.
                String new_address = ip_xemu0_edittext.getText().toString();

                AddressManager.setXemu0Address(new_address, getContext().getApplicationContext());

                Toast.makeText(getContext().getApplicationContext(), "xEMU-0: " + new_address, Toast.LENGTH_SHORT).show();

                /* Old
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_XEMU0, ip_xemu0_edittext.getText().toString());
                editor.apply();
                Intent intent = new Intent(SettingsFragment.this.getActivity(), IPFragment.class);
                startActivity(intent);

                Toast.makeText(getContext().getApplicationContext(), "xEmu0 IP saved", Toast.LENGTH_SHORT).show();
                */
            }
        });

        // ----- Working on xEMU-1 -----
        ip_xemu1_edittext = (EditText) rootView.findViewById(R.id.ip_xemu1_edittext);
        ip_xemu1_edittext.setText(xemu1_addr);

        applyXemu1_button = (Button) rootView.findViewById(R.id.applyXemu1_button);
        applyXemu1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When clicked, the button puts data on shared preferences.
                String new_address = ip_xemu1_edittext.getText().toString();

                AddressManager.setXemu1Address(new_address, getContext().getApplicationContext());

                Toast.makeText(getContext().getApplicationContext(), "xEMU-1: " + new_address, Toast.LENGTH_SHORT).show();

                /* Old
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_XEMU1, ip_xemu1_edittext.getText().toString());
                editor.apply();
                Intent intent = new Intent(SettingsFragment.this.getActivity(), IPFragment.class);
                startActivity(intent);

                Toast.makeText(getContext().getApplicationContext(), "xEmu 1 IP saved", Toast.LENGTH_SHORT).show();
                */
            }
        });

        return rootView;
    }

}
