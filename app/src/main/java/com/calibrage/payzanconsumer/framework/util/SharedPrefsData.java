package com.calibrage.payzanconsumer.framework.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by Calibrage19 on 24-10-2017.
 */

public class SharedPrefsData {
    private static SharedPrefsData instance = null;
    private static final String DEFAULTVALUESTRING = "N/A";
    private static final int DEFAULTVALUINT = 0;
    private static final String PICHIT_DATA = "payzan";
    private static String MY_INT_PREF = "myintpref";
    private SharedPreferences payZanSharedPrefs = null;
    private static final String USER_ID = "user_id";
    private static final String PROFILE_PIC = "Profile_pic";
    private static final String WALLET_ID = "wallet_id";
    private static final String WALLET_MONEY = "wallet_money";
    private static final String WALLET_BALANCE = "wallet_balance";
    private static final String USER_NAME = "user_name";
    private static final String USER_DETAILS = "user_details";


    public static SharedPrefsData getInstance(Context context) {

        if (instance == null) {
            instance = new SharedPrefsData();
            instance.getPitchItSharedPrefs(context);
        }
        return instance;
    }

    /*
    SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("pitchitData", Context.MODE_PRIVATE);
        userName = sharedpreferences.getString("username", DEFAULT_VALUE);
        password = sharedpreferences.getString("password", DEFAULT_VALUE);
     */


    private void getPitchItSharedPrefs(Context context) {
        if (this.payZanSharedPrefs == null) {
            this.payZanSharedPrefs = context.getSharedPreferences(PICHIT_DATA, Context.MODE_PRIVATE);
        }
    }

    private void loadFreshPitchItSharedPrefs(Context context) {
        this.payZanSharedPrefs = context.getSharedPreferences(PICHIT_DATA, Context.MODE_PRIVATE);
    }

    public String getStringFromSharedPrefs(String key) {
        return payZanSharedPrefs.getString(key, DEFAULTVALUESTRING);
    }

    public int getIntFromSharedPrefs(String key) {
        return payZanSharedPrefs.getInt(key, DEFAULTVALUINT);
    }

    public void saveUserId(Context context, String userId) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(USER_ID, userId);

            // Commit the edits!
            editor.apply();

        }
    }
    public void saveProfilePic(Context context, String profilePic) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(PROFILE_PIC, profilePic);

            // Commit the edits!
            editor.apply();

        }
    }

    public void saveWalletTYPEID(Context context, int WalID) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putInt(CommonConstants.WID, WalID);

            // Commit the edits!
            editor.apply();

        }
    }

    public int getWalletTYPEID(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                Context.MODE_PRIVATE);
        return profilePref.getInt(CommonConstants.WID, 0);

    }
    public String getUserId(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                Context.MODE_PRIVATE);
        return profilePref.getString(USER_ID, "");

    }

    public void saveUserName(Context context, String userName) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(USER_NAME, userName);

            // Commit the edits!
            editor.apply();

        }
    }

    public String getUserName(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                Context.MODE_PRIVATE);
        return profilePref.getString(USER_NAME, "");

    }
    public void saveUserDetails(Context context, String userDetails) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(USER_DETAILS, userDetails);

            // Commit the edits!
            editor.apply();

        }
    }

    public String getUserDetails(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                Context.MODE_PRIVATE);
        return profilePref.getString(USER_DETAILS, "");

    }

    public void saveWalletId(Context context, String walletId) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(WALLET_ID, walletId);

            // Commit the edits!
            editor.apply();

        }
    }

    public String getWalletId(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                Context.MODE_PRIVATE);
        return profilePref.getString(WALLET_ID, "");

    }

    public void saveWalletIdMoney(Context context, long walletMoney) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putLong(WALLET_MONEY, walletMoney);

            // Commit the edits!
            editor.apply();

        }
    }

    public void saveWalletBalance(Context context, String walletbalance) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(WALLET_BALANCE, walletbalance);

            // Commit the edits!
            editor.apply();

        }
    }

    public String getWalletBalance(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                Context.MODE_PRIVATE);
        return profilePref.getString(WALLET_BALANCE, "");

    }

    public long getWalletIdMoney(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(PICHIT_DATA,
                Context.MODE_PRIVATE);
        return profilePref.getLong(WALLET_MONEY, 0);

    }




    public void updateMultiValue(Context context, List<SharedPrefsBean> sharedPrefsBeans) {
        //getPitchItSharedPrefs(context);
        SharedPreferences.Editor editor = this.payZanSharedPrefs.edit();

        for (SharedPrefsBean eachShrePref : sharedPrefsBeans) {
            if (eachShrePref.getIsInt()) {
                editor.putInt(eachShrePref.getKey(), eachShrePref.getValueInt());
            } else {
                editor.putString(eachShrePref.getKey(), eachShrePref.getValueString());
            }
        }
        editor.commit();
        loadFreshPitchItSharedPrefs(context);
    }

    public void updateStringValue(Context context, String key, String value) {
        //getPitchItSharedPrefs(context);
        SharedPreferences.Editor editor = this.payZanSharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void updateIntValue(Context context, String key, int value) {
        //getPitchItSharedPrefs(context);
        SharedPreferences.Editor editor = this.payZanSharedPrefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void ClearData(Context context) {
        //getPitchItSharedPrefs(context);
        SharedPreferences.Editor editor = this.payZanSharedPrefs.edit();
        editor.clear();
        editor.commit();
    }

/*
SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
 */

}
