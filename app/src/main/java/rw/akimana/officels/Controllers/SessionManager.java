package rw.akimana.officels.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import rw.akimana.officels.Login;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Pref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAMES = "name";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_ACCESS = "access";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_UPDATED_AT = "updated_at";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Create login session
     * */
    public void createLoginSession(String id,String names,String username,String email,String access,String created,String updated){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAMES, names);
        editor.putString(KEY_USER_NAME, username);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ACCESS, access);
        editor.putString(KEY_CREATED_AT, created);
        editor.putString(KEY_UPDATED_AT, updated);
        editor.commit();
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_USER_NAMES, pref.getString(KEY_USER_NAMES, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, null));
        user.put(KEY_USER_ACCESS, pref.getString(KEY_USER_ACCESS, null));
        user.put(KEY_CREATED_AT, pref.getString(KEY_CREATED_AT, null));
        user.put(KEY_UPDATED_AT, pref.getString(KEY_UPDATED_AT, null));

        return user;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
