package com.evolve.evolve.EvolveActivities.EvolveUtilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vellapanti on 19/9/15.
 */
public class EvolvePreferences {

    Context context;
    SharedPreferences prefs;

    public EvolvePreferences(Context c) {
        this.context = c;
        prefs = context.getSharedPreferences("Evolve", Context.MODE_PRIVATE);
    }

    public void setId(int id) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("id", id);
        editor.commit();
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", accessToken);
        editor.commit();
    }

    public String getAccessToken() {return prefs.getString("access_token","");}
    public int getId(){return prefs.getInt("id",0);}

}
