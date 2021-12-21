package com.example.nasaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * This class manages the shared preferences related to the addresses for ARGOS and the xEMUs.
 */

public class AddressManager {
    public static String getArgosAddress(Context applicationContext) {
        if ( applicationContext == null ) return null;

        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentAddress = prefs.getString(ARGOS_ADDRESS, applicationContext.getResources().getString(R.string.default_argos_address));

        Log.i(TAG, "Returning ARGOS address: " + currentAddress);

        return currentAddress;
    }

    public static void setArgosAddress(String newAddress, Context applicationContext) {
        if ( newAddress == null || applicationContext == null ) return;

        // TODO: Make sure the address is valid

        Log.i(TAG, "Setting ARGOS address: " + newAddress);

        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ARGOS_ADDRESS, newAddress);
        editor.apply();
    }

    public static String getXemu0Address(Context applicationContext) {
        if ( applicationContext == null ) return null;

        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentAddress = prefs.getString(XEMU_0_ADDRESS, applicationContext.getResources().getString(R.string.default_xemu0_address));

        Log.i(TAG, " Returning xEMU-0 address: " + currentAddress);

        return currentAddress;
    }

    public static void setXemu0Address(String newAddress, Context applicationContext) {
        if ( newAddress == null || applicationContext == null ) return;

        // TODO: Make sure the address is valid

        Log.i(TAG, "Setting xEMU-0 address: " + newAddress);

        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(XEMU_0_ADDRESS, newAddress);
        editor.apply();
    }

    public static String getXemu1Address(Context applicationContext) {
        if ( applicationContext == null ) return null;

        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentAddress = prefs.getString(XEMU_1_ADDRESS, applicationContext.getResources().getString(R.string.default_xemu1_address));

        Log.i(TAG, " Returning xEMU-1 address: " + currentAddress);

        return currentAddress;
    }

    public static void setXemu1Address(String newAddress, Context applicationContext) {
        if ( newAddress == null || applicationContext == null ) return;

        // TODO: Make sure the address is valid

        Log.i(TAG, "Setting xEMU-1 address: " + newAddress);

        SharedPreferences prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(XEMU_1_ADDRESS, newAddress);
        editor.apply();
    }

    public static final String PREFS_NAME = "ARGOS";
    public static final String ARGOS_ADDRESS = "ARGOS_ADDRESS";
    public static final String XEMU_0_ADDRESS = "XEMU_0_ADDRESS";
    public static final String XEMU_1_ADDRESS = "XEMU_1_ADDRESS";

    private static final String TAG = "AddressManager";
}
